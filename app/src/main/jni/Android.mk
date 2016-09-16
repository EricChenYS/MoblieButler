LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := feedback
LOCAL_SRC_FILES := feedback.c
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)
