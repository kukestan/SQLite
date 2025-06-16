################################################################################
# @file  Android.mk
# @brief Rules for compiling the source files
################################################################################
ifneq ($(TARGET_NO_TELEPHONY), true)

LOCAL_PATH:= $(call my-dir)

# ==========================================================================

include $(CLEAR_VARS)

LOCAL_JAVA_LIBRARIES := telephony-common

LOCAL_MODULE := Database-static
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, src/java/)
LOCAL_PRODUCT_MODULE := true

include $(BUILD_STATIC_JAVA_LIBRARY)

# ==========================================================================
endif # TARGET_NO_TELEPHONY
