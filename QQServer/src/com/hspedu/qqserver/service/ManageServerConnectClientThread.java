package com.hspedu.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

@SuppressWarnings({"all"})
public class ManageServerConnectClientThread {
    private static HashMap<String,ServerConnectClientThread> hashMap = new HashMap<>();
    public static void addClientThread(String userId,ServerConnectClientThread thread) {
        hashMap.put(userId,thread);
    }
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hashMap.get(userId);
    }

    public static HashMap<String, ServerConnectClientThread> getHashMap() {
        return hashMap;
    }

    //编写方法,可以返回在线用户列表
    public static String getOnlineUser() {
        //集合遍历,便利hashmap的key
        Iterator<String> iterator = hashMap.keySet().iterator();
        String onlineUser = "";
        while (iterator.hasNext()) {
            onlineUser += iterator.next().toString() + " ";
        }
        return onlineUser;
    }
    //删除集合中某个线程
    public static void removeServerConnetClient(String userId) {
        hashMap.remove(userId);
    }

}
