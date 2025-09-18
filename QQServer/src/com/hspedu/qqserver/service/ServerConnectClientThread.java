package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqserver.CheckOffline;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

@SuppressWarnings({"all"})
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;//连接客户端的ID

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //因为需要与客户端保持通信,所以使用while循环控制
        while (true) {

            try {
                System.out.println("服务器和客户端" + userId + "保持通讯...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message) ois.readObject();
                //根据message类型,做相应的逻辑处理
                if (msg.getMegType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    //客户端在线用户列表
                    System.out.println("用户 " + msg.getSender() + " 要在线用户列表");
                    String onlineUser = ManageServerConnectClientThread.getOnlineUser();
                    //构建一个Message对象,返回给客户端
                    Message message = new Message();
                    message.setMegType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);
                    message.setContent(onlineUser);
                    message.setGetter(message.getSender());
                    //返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                } else if (msg.getMegType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //根据getterId获取对应线程
                    if (CheckOffline.checkOffline(msg)) {
                        //检查getter是否在线
                        ServerConnectClientThread getterThread = ManageServerConnectClientThread.getServerConnectClientThread(msg.getGetter());
                        ObjectOutputStream oos = new ObjectOutputStream(getterThread.getSocket().getOutputStream());
                        oos.writeObject(msg);//转发,提示如果客户不在线,可以保存到数据库,这样就可以实现离线留言
                    } else {
                        msg.setContent(msg.getGetter() + " 不在线,已转为离线留言");
                        msg.setMegType(MessageType.MESSAGE_OFFLINE);
                        ServerConnectClientThread senderThread = ManageServerConnectClientThread.getServerConnectClientThread(msg.getSender());
                        ObjectOutputStream oos = new ObjectOutputStream(senderThread.getSocket().getOutputStream());
                        oos.writeObject(msg);
                    }
                } else if (msg.getMegType().equals(MessageType.MESSAGE_TO_ALL_MEG)) {
                    //遍历hashmap,得到所有线程的socket
                    HashMap<String, ServerConnectClientThread> hashMap = ManageServerConnectClientThread.getHashMap();
                    Iterator<String> iterator = hashMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        //取出在线用户的ID
                        String onlineUserId = iterator.next();
                        if (!onlineUserId.equals(msg.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(hashMap.get(onlineUserId).getSocket().getOutputStream());
                            oos.writeObject(msg);
                        }
                    }
                } else if (msg.getMegType().equals(MessageType.MESSAGE_FILE_MEG)) {
                    //根据getterId获取对应线程并转发文件内容
                    ObjectOutputStream oos =
                            new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClientThread(msg.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(msg);
                } else if (msg.getMegType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println("用户 " + msg.getSender() + " 退出系统");
                    //将客户端对应的线程从集合中删除
                    Thread.sleep(1);//不停滞会报EOF异常
                    ManageServerConnectClientThread.removeServerConnetClient(msg.getSender());
                    socket.close();//关闭链接
                    break;//退出循环

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

//            switch分支
//            try {
//                System.out.println("服务器和客户端" + userId + "保持通讯...");
//                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//                Message msg = (Message) ois.readObject();
//                //根据message类型,做相应的逻辑处理
//                switch (msg.getMegType()) {
//                    case MessageType.MESSAGE_GET_ONLINE_FRIEND:
//                        //客户端在线用户列表
//                        System.out.println("用户 " + msg.getSender() + " 要在线用户列表");
//                        String onlineUser = ManageServerConnectClientThread.getOnlineUser();
//                        //构建一个Message对象,返回给客户端
//                        Message message = new Message();
//                        message.setMegType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);
//                        message.setContent(onlineUser);
//                        message.setGetter(message.getSender());
//                        //返回给客户端
//                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                        oos.writeObject(message);
//                    case MessageType.MESSAGE_CLIENT_EXIT:
//                        System.out.println("用户 " + msg.getSender() + " 退出系统");
//                        //将客户端对应的线程从集合中删除
//                        Thread.sleep(1);//不停滞会报EOF异常
//                        ManageServerConnectClientThread.removeServerConnetClient(msg.getSender());
//                        socket.close();//关闭链接
//                        break;//退出循环
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }

        }
    }

}
