
from .models import SneakersInfo
from rest_framework import serializers

class SneakersSerializer(serializers.ModelSerializer):
    class Meta:
        model = SneakersInfo
        fields = '__all__'