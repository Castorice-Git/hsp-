package com.hspedu.qqclent.service;

import java.util.HashMap;

/**
 * 管理客户端连接到服务器的线程的类
 */
@SuppressWarnings({"all"})
public class ManageClientConnectServerThread {
    //把多个线程放入HashMap集合,key表示用户ID,value表示ID对应的线程
    private static HashMap<String,ClientConnectServerThread> hashMap = new HashMap<>();
    //将某个线程加入到集合
    public static void addClientConnectServerThread (String userId, ClientConnectServerThread thread) {
        hashMap.put(userId,thread);
    }
    //可以通过userId得到一个线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hashMap.get(userId);
    }
}
