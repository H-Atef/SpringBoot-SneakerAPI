from django.urls import path,include
from . import views
from rest_framework.routers import DefaultRouter


router=DefaultRouter()

router.register("sneakers-info",views.SneakersViewSet, basename='sneakers-info')


urlpatterns = [

    path('find-by-category/', views.SneakersViewSet.as_view({'get': 'get_sneakers_by_category'}), name='get_sneakers_by_category'),
    path('find-by-model-name/<str:model_name>/', views.SneakersViewSet.as_view({'get': 'search_by_model_name'}), name='search_by_model_name')

    
]+router.urls