from rest_framework import viewsets,views, permissions
from rest_framework.response import Response
from .models import SneakersInfo
from .serializers import SneakersSerializer
from rest_framework.decorators import api_view,action
from rest_framework import status
from sneakers_scraper.scraping_helper.sneakers_main import SneakersScraper
from sneakers_scraper.security.custom_jwt_auth import CustomJWTAuthentication
from sneakers_scraper.security.permissions import IsAdminOrReadOnly



class SneakersViewSet(viewsets.ModelViewSet):
    queryset=SneakersInfo.objects.all()
    serializer_class=SneakersSerializer   
    permission_classes = [IsAdminOrReadOnly]
    authentication_classes = [CustomJWTAuthentication]

    def get_access_token_from_cookies(self, request):
        return request.COOKIES.get('access_token', None)



    def initial(self, request, *args, **kwargs):
        super().initial(request, *args, **kwargs)
        access_token = self.get_access_token_from_cookies(request)
        if access_token:
            request.META['HTTP_AUTHORIZATION']=f'Bearer {access_token}'
        
            


    @action(detail=False, methods=['get'])
    def get_sneakers_by_category(self, request):
        access_token = self.get_access_token_from_cookies(request)
       
        try:
            category = request.query_params.get('category') or "Sneakers"


            if category is not None:
                category=category.title().replace("%20"," ").replace("+"," ") 

            pages = request.query_params.get('pages') or 1

            pages_total_products_count=int(pages)*16
            database_sneakers_by_category=SneakersInfo.objects.filter(category=category)
            # Check if existing data can be used
            if database_sneakers_by_category.exists():
                if len(database_sneakers_by_category)>=pages_total_products_count//2:
                    #print("found",pages_total_products_count//2, len(database_sneakers_by_category))
                    data = SneakersSerializer(SneakersInfo.objects.filter(category=category), many=True).data
                    return Response(data)
                
            # Scrape data
            df = SneakersScraper(category=category).scrape_and_serialize(0, int(pages))
            
            # Get existing records based on a unique identifier (e.g., 'product_id')
            existing_ids =SneakersInfo.objects.filter(category=category).values_list('product_id', flat=True)
            
            # Add new records to the database if they don't already exist
            new_records = []
            for record in df.to_dict(orient='records'):
                if record['product_id'] not in existing_ids:
                    serializer = SneakersSerializer(data=record)
                    if serializer.is_valid():
                        serializer.save()
            
                # Retrieve and return all data for the category
            data = SneakersSerializer(SneakersInfo.objects.filter(category=category), many=True).data

            return Response(data)

            

        except Exception as e:
            return Response({"error": str(e)}, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=False, methods=['get'])
    def search_by_model_name(self, request, model_name):
        access_token = self.get_access_token_from_cookies(request)
        try:
            sneakers_data = SneakersInfo.objects.filter(title__icontains=model_name)
            return Response(SneakersSerializer(sneakers_data, many=True).data)
        
        except Exception as e:
            return Response({"error": "Bad Request"}, status=status.HTTP_400_BAD_REQUEST)