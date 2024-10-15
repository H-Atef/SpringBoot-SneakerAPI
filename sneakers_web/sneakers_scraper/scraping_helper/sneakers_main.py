import requests
import json
import threading
import datetime
import pandas as pd
import os



class SneakersScraper:
    def __init__(self, category="Sneakers"):
        self.category = category.replace("%20"," ").replace("+"," ").title()
        self.url_params={
            "api_key": "9K8X7f4T5Y",
            "q": "",
            "sortBy": "created",
            "sortOrder": "desc",
            "restrictBy[quantity]": "1|",
            "restrictBy[product_type]": self.category,
            "startIndex": None,
            "maxResults": 16,
            "items": "true",
            "pages": "true",
            "categories": "true",
            "suggestions": "true",
            "queryCorrection": "true",
            "suggestionsMaxResults": "3",
            "pageStartIndex": "0",
            "pagesMaxResults": "0",
            "categoryStartIndex": "0",
            "categoriesMaxResults": "0",
            "facets": "false",
            "facetsShowUnavailableOptions": "false",
            "recentlyViewedProducts": "8390400966908,7750623789308,8759786995964,8759786995964,8759715856636,8759729979644,8759786995964",
            "recentlyAddedToCartProducts": "",
            "recentlyPurchasedProducts": "",
            "ResultsTitleStrings": "1",
            "ResultsDescriptionStrings": "0",
            "page": None,
            "tab": "products",
            "collection": "all-products",
            "timeZoneName": "Africa/Cairo",
            "output": "jsonp",
            "callback": "jQuery22408613008649417873_1727086206921",
            "_": "1727086206922"
        }
        self.url='https://searchserverapi.com/getresults'
        self.all_items = []

    def scrape_items_thread(self,start_index=0,num_pages=1):
        
        temp_params=self.url_params.copy()
        temp_params["startIndex"]=start_index
        temp_params["page"]=num_pages

        driver = requests.get(self.url,params=temp_params)
        page_source = driver.text
        start_b_index = page_source.index('(') + 1
        end_b_index = page_source.rindex(')')
        json_data = page_source[start_b_index:end_b_index]

        data = json.loads(json_data)
        items = data.get('items', [])

        for item in items:
            item_summary = {
                "product_id": item.get("product_id"),
                "title": item.get("title"),
                "link": 'https://www.footcourt-eg.com'+item.get("link"),
                "price": item.get("price"),
                "list_price": item.get("list_price"),
                "quantity": item.get("quantity"),
                "product_code": item.get("product_code"),
                "image_link": item.get("image_link"),
                "vendor": item.get("vendor"),
                "discount": item.get("discount"),
                "category": self.category
            }
            self.all_items.append(item_summary)
        driver.close()

    def run_threads(self,start_index=0,num_pages=1):
        thread_list = []
        for i in range(1,num_pages + 1):
          
            thread = threading.Thread(target=self.scrape_items_thread,args=((i-1)*16,i))
            thread_list.append(thread)
            thread.start()
        

        for thread in thread_list:
            thread.join()

      


    def scrape_and_serialize(self,start_index=0,num_pages=1)->pd.DataFrame:

        self.run_threads(start_index,num_pages)
        csv_filename = f'./sneakers_scraper/scraping_helper/csv_outputs/tmp_{datetime.datetime.now().strftime("%Y%m%d%H%M%S")}_{self.category}.csv'
        DataProcessor.save_to_csv(self.all_items,csv_filename)
        DataSerializer.serialize(self.all_items)
        DataSerializer.append_to_json(self.all_items)

        return DataProcessor.data_to_dataframe(self.all_items)
        




class DataProcessor:
    
    @staticmethod
    def data_to_dataframe(data)->pd.DataFrame:
        df = pd.DataFrame(data)
        df=DataProcessor.transform_data(df)
        return df


    @staticmethod
    def save_to_csv(data,file_name):

        if not os.path.exists('./sneakers_scraper/scraping_helper/csv_outputs'):
            os.makedirs('./sneakers_scraper/scraping_helper/csv_outputs')

        df = DataProcessor.data_to_dataframe(data)
        df.to_csv(file_name, index=False)

    @staticmethod
    def transform_data(df:pd.DataFrame)->pd.DataFrame:
        try:
            if df.empty:
                return df
            
            df['price']= df['price'].apply(float)
            df['list_price'] = df['list_price'].apply(float)
            df['discount'] = df['discount'].apply(float)
            df['quantity'] = df['quantity'].apply(int)
            return df
        except Exception as e:
            print("Converting data process failed!")
            return df

class DataSerializer:
    @staticmethod
    def serialize(data):
        if not os.path.exists('./sneakers_scraper/scraping_helper/json_outputs'):
            os.makedirs('./sneakers_scraper/scraping_helper/json_outputs')

        current_date = datetime.datetime.now().strftime("%Y%m%d%H%M%S")
        filename = f"./sneakers_scraper/scraping_helper/json_outputs/tmp_{current_date}.json"
        with open(filename, 'w', encoding='UTF-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=4)
        return filename

    @staticmethod
    def deserialize(filename):
        with open(filename, 'r', encoding='UTF-8') as f:
            return json.load(f)

    @staticmethod
    def append_to_json(new_data): 
        try:
            with open("./sneakers_scraper/scraping_helper/json_outputs/sneakers_main.json", 'r', encoding='UTF-8') as f:
                all_data = json.load(f)
                all_data.extend(new_data)
            with open("./sneakers_scraper/scraping_helper/json_outputs/sneakers_main.json", 'w', encoding='UTF-8') as f:
                json.dump(all_data, f, ensure_ascii=False, indent=4)

        except Exception as e:

            with open("./sneakers_scraper/scraping_helper/json_outputs/sneakers_main.json", 'w', encoding='UTF-8') as f:
                json.dump(new_data, f, ensure_ascii=False, indent=4)
        




# s=SneakersScraper()
# s.scrape_and_serialize(0,20)
