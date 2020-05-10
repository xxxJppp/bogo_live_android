package com.bogokj.xianrou.util;


public class Event {

    public static class DialogEvent {
        public int action;
    }

    public static class VideoEvent{
        public String[] data;
        public int action;
    }

    public static class PrivateChatEvent{
        public int action;
    }

    public static class LeftVideoTouchChangeEvent{
        public int action;
    }
    public static class CommonEvent{
        public int action;
    }

    public static class LiveRtcEvent{
        public int action;
        public String rtcUserId;
    }

    public static class ShortVideoPlayer{

        public static final int SHORT_VIDEO_REPLY_COMMENT = 1;

        public int action;
        public Object data;
    }
    public static class SendGiftEvent{

        public String giftToken;
        public String isOutTime;
        public int count;
//        public GiftBean sendGiftBean;
    }

    public static class OnTouchShortVideoPlayerPageChange{
    }

    public static class OnTouchLivePlayerPageChange{
    }

    public static class OnTouchLiveFinish{
    }

    public static class OnTouctVideoFinish{
    }
}
