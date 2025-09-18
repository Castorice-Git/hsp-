package com.hspedu.qqclent.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

@SuppressWarnings({"all"})
public class ClientConnectServerThread extends Thread {
    //线程需要一个socket
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread需要一直在后台和服务器通信,所以使用while循环控制
        while (true) {

            try {
                System.out.println("客户端线程,等待读取从服务器发送的消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象,线程会阻塞
                Message msg = (Message) ois.readObject();
                //判断Message类型,然后做对应的处理
                if (msg.getMegType().equals(MessageType.MESSAGE_RETURN_ONLINE_FRIEND)) {
                    //取出用户列表并显示
                    String[] s = msg.getContent().split(" ");
                    System.out.println("\n======在线用户列表======");
                    for (int i = 0; i < s.length; i++) {
                        System.out.println("用户: " + s[i]);
                    }
                } else if (msg.getMegType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //把从服务器转发的消息显示到控制台
                    System.out.println("\n" + msg.getSender() + " 对 " + msg.getGetter() + " 说:" + msg.getContent());
                } else if (msg.getMegType().equals(MessageType.MESSAGE_TO_ALL_MEG)) {
                    System.out.println("\n" + msg.getSender() + " 对大家说:" + msg.getContent());
                } else if (msg.getMegType().equals(MessageType.MESSAGE_FILE_MEG)) {
                    System.out.println("\n" + msg.getSender() + " 给 你 发送文件: " + msg.getSrc() + " 到 你 的目录 " + msg.getDest() + " 中");
                    FileOutputStream fos = new FileOutputStream(msg.getDest());
                    fos.write(msg.getFileBytes());
                    fos.close();
                    System.out.println("\n" + "文件保存成功");
                } else if(msg.getMegType().equals(MessageType.MESSAGE_OFFLINE)) {
                    System.out.print("\n" + msg.getContent());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //为了更方便的得到socket,提供get方法
    public Socket getSocket() {
        return socket;
    }
}
