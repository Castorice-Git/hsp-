package com.hspedu.qqclent.service;

import com.hspedu.qqclent.utils.Utility;
import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 该类完成用户登录验证和用户注册等功能
 */
@SuppressWarnings({"all"})
public class UserClientService {
    private User u = new User();//可能在其他地方使用User信息,所以做成成员属性
    private Socket socket;//因为socket可能在其他地方使用,所以做成属性

    public boolean checkUser(String userId, String pwd) {//根据userId和pwd到服务器验证该用户是否合法
        boolean b = false;
        //创建User对象
        u.setUserId(userId);
        u.setPassword(pwd);
        //连接到服务端,发送User对象
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送user对象

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());//接收服务器回送的Message对象
            Message msg = (Message) ois.readObject();

            if (msg.getMegType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {

                //创建一个与服务器保持通信的线程 -> 创建一个类ClientConnectServerThread
                ClientConnectServerThread thread = new ClientConnectServerThread(socket);
                //启动客户端的线程
                thread.start();
                //为了客户端的后续扩展,使用集合保存管理线程
                ManageClientConnectServerThread.addClientConnectServerThread(u.getUserId(), thread);
                b = true;
            } else {
                //如果登录失败,我们就不能启动和服务器通信的线程,关闭socket
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    //向服务器请求在线用户列表
    public void onlineFriendList() {
        //发送一个Message类型 MESSAGE_GET_ONLINE_FRIEND
        try {
            Message message = new Message();
            message.setSender(u.getUserId());
            message.setMegType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
            //得到当前线程的socket对应的ObjectOutputStream对象
           ObjectOutputStream oos =
                   new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
           oos.writeObject(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //编写退出客户端的方法并给服务器发送退出系统的Message对象
    public void logout() {
        try {
            Message message = new Message();
            message.setSender(u.getUserId());
            message.setMegType(MessageType.MESSAGE_CLIENT_EXIT);
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId() + " 退出系统");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
