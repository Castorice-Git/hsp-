package com.hspedu.qqclent.service;

import com.hspedu.qqcommon.Message;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
@SuppressWarnings({"all"})
public class CheckOfflineMessage {
    private static ConcurrentHashMap<String, ArrayList<Message>> chm = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<Message>> getChm() {
        return chm;
    }
}
