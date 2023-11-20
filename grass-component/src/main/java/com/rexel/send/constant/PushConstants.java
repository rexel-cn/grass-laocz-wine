package com.rexel.send.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName PushConstants
 * @Description
 * @Author 孟开通
 * @Date 2022/11/11 15:20
 **/
public class PushConstants {
    public static final String PUSH_MESSAGE_SEND = "0";
    public static final String PUSH_MAILBOX_SEND = "1";
    public static final String PUSH_SMS_SEND = "2";
    public static final String PUSH_DING_DING_SEND = "3";

    public static List<String> gsSend() {
        return new ArrayList<>(Arrays.asList(PUSH_MESSAGE_SEND, PUSH_MAILBOX_SEND));
    }
}
