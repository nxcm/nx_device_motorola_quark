$(call inherit-product, device/motorola/quark/full_quark.mk)

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Enhanced NFC
$(call inherit-product, vendor/cm/config/nfc_enhanced.mk)

# Overlay
DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay

PRODUCT_RELEASE_NAME := Moto MAXX
PRODUCT_NAME := nx_quark
NX_MODEL_NAME := Quark

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME=quark_retbr \
    BUILD_FINGERPRINT=motorola/quark_retbr/quark_umts:5.0.2/LXG22.33-12.13/13:user/release-keys \
    PRIVATE_BUILD_DESC="quark_retbr-user 5.0.2 LXG22.33-12.13 13 release-keys"
