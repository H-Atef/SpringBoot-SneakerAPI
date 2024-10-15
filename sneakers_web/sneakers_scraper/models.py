from django.db import models


class SneakersInfo(models.Model):
    product_id = models.CharField(max_length=255)
    title = models.CharField(max_length=255)
    link = models.CharField(max_length=255)
    price = models.DecimalField(max_digits=10, decimal_places=3)
    list_price = models.DecimalField(max_digits=10, decimal_places=3)
    quantity = models.IntegerField()
    product_code = models.CharField(max_length=255)
    image_link = models.CharField(max_length=255)
    vendor = models.CharField(max_length=255)
    discount = models.DecimalField(max_digits=5, decimal_places=2)
    category = models.CharField(max_length=255)



