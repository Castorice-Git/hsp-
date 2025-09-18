package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
@SuppressWarnings({"all"})
public class SendNewsToAllService extends Thread {
    private HashMap<String,ServerConnectClientThread> hm = ManageServerConnectClientThread.getHashMap();
    private boolean cheackOnlineUser() {
        if (!hm.equals(null)) {
            return true;
        }else {
            System.out.println("目前无在线用户");
            return false;
        }
    }
    @Override
    public void run() {
        //为了可以多次推送消息,使用while循环
        while (true) {
            Message message = new Message();
            System.out.print("请输入想要推送的内容/消息[输入exit退出推送消息功能]:");
            String news = Utility.readString(100);
            if (news.equals("exit")) {
                break;
            }
            message.setMegType(MessageType.MESSAGE_TO_ALL_MEG);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new java.util.Date().toString());
            System.out.println("服务器推送消息给所有人 说:" + news);
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                //获取在线用户ID
                String onlineUser = iterator.next();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineUser).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

    }
}
