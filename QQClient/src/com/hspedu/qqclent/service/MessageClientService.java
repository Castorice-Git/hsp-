package com.hspedu.qqclent.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类提供消息相关服务
 */
@SuppressWarnings({"all"})
public class MessageClientService {

    public void SendMessageToOne(String content, String senderId, String getterId) {
        //构建Message对象
        Message message = new Message();
        message.setMegType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());//把发送时间也封装到Message对象
        System.out.println("\n" + senderId + " 对 " + getterId + " 说:" + content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void SendMessageToAll(String content, String senderId) {
        Message message = new Message();
        message.setMegType(MessageType.MESSAGE_TO_ALL_MEG);
        message.setContent(content);
        message.setSender(senderId);
        message.setSendTime(new java.util.Date().toString());
        System.out.println("\n" + senderId + " 对你说:" + content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void SendOfflineMessageToOne(String senderId, String getterId, String content) {
        ConcurrentHashMap<String, ArrayList<Message>> chm = CheckOfflineMessage.getChm();
        Message message = new Message();
        message.setMegType(MessageType.MESSAGE_OFFLINE);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        System.out.print("\n" + "离线留言:" + senderId + " 对 " + getterId + " 说:" + content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
