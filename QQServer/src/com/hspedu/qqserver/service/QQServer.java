package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器,监听端口9999,等待客户端的连接并保持通讯
 */
@SuppressWarnings({"all"})
public class QQServer {

    private ServerSocket serverSocket;
    //创建一个集合,用于存放多个用户,如果是这些用户登录,就判定为合法
    //也可以使用ConcurrentHashMap,可以处理高并发的集合,没有线程安全问题
    //ConcurrentHashMap处理的线程安全,即线程同步处理,在多线程情况下是安全的
    private static HashMap<String, User> validUsers = new HashMap<>();
    private static ConcurrentHashMap<String,ArrayList<Message>> chm = new ConcurrentHashMap<>();
    static {//在静态代码块初始化validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("400", new User("400", "123456"));
        validUsers.put("500", new User("500", "123456"));

    }
    //验证用户是否合法的方法
    private boolean checkUser(String userId,String pwd) {
        User user = validUsers.get(userId);
        if (user == null) {//说明没有userId在validUsers中
            return false;
        }
        if (!pwd.equals(user.getPassword())) {
            return false;
        } else  {
            return true;
        }
    }

    public QQServer() {
        //端口可以写在配置文件中
        System.out.println("服务器在9999端口监听...");
        //启动推送新闻线程
        new Thread(new SendNewsToAllService()).start();
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                //和某个客户端连接后,继续监听,所以使用while循环
                Socket socket = serverSocket.accept();//如果没有客户端连接,线程就会阻塞
                //获取socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();//读取客户端发送的User对象
                //创建一个Message对象,准备回复客户端
                Message msg = new Message();
                //验证
                if (checkUser(u.getUserId(),u.getPassword())) {//合法
                    msg.setMegType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将Message对象发送回去
                    oos.writeObject(msg);
                    //创建线程,与客户端保持通信
                    ServerConnectClientThread thread = new ServerConnectClientThread(socket, u.getUserId());
                    thread.start();
                    //将线程对象放入集合中
                    ManageServerConnectClientThread.addClientThread(u.getUserId(), thread);
                    //登录启动线程后,检查有没有离线留言,没有就直接退出方法
                } else {//不合法
                    System.out.println("用户" + u.getUserId() + "登录失败");
                    msg.setMegType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(msg);
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //当服务器退出循环时,说明端口不再监听,所以要关闭ServerScoket
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
