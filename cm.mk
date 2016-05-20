$(call inherit-product, device/motorola/quark/full_quark.mk)

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Enhanced NFC
$(call inherit-product, vendor/cm/config/nfc_enhanced.mk)

# Overlay
DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay

PRODUCT_RELEASE_NAME := Moto Maxx
PRODUCT_NAME := nx_quark
NX_MODEL_NAME := Moto Maxx
