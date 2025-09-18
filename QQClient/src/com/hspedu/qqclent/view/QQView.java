package com.hspedu.qqclent.view;

import com.hspedu.qqclent.service.FileClientService;
import com.hspedu.qqclent.service.MessageClientService;
import com.hspedu.qqclent.service.UserClientService;
import com.hspedu.qqclent.utils.Utility;

/**
 * 客户端的菜单界面
 */
@SuppressWarnings({"all"})
public class QQView {
    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统......");
    }
    private boolean loop = true;//控制菜单显示
    private String key = "";//接收键盘输入
    private UserClientService ucs = new UserClientService();//用于登录服务器
    private MessageClientService mcs = new MessageClientService();//对象用户的私聊或群聊
    private FileClientService fcs = new FileClientService();//该对象用于传输文件
    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("============欢迎登录网络通信系统============");
            System.out.println("\t\t\t 1 登录系统");
            System.out.println("\t\t\t 9 退出登录");
            System.out.print("请输入你的选择:");
            key = Utility.readString(1);//读取字符串,长度限制为1

            //根据用户的输入,来处理不同的逻辑
            switch (key) {
                case "1" :
                    System.out.print("请输入你的用户名:");
                    String userId = Utility.readString(50);
                    System.out.print("请输入你的密码:");
                    String userPassword = Utility.readString(50);
                    //需要到服务器验证该用户是否合法
                    //编写一个类 UserClientService[用户登录/注册]
                    if (ucs.checkUser(userId,userPassword)) {
                        System.out.println("欢迎(用户 " + userId + ")登录成功!");
                        while (loop) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("\n============网络通信系统二级菜单(用户 " + userId + ")============");
                            System.out.println("\t\t\t 1 显示在线用户列表");
                            System.out.println("\t\t\t 2 群发消息");
                            System.out.println("\t\t\t 3 私聊消息");
                            System.out.println("\t\t\t 4 发送文件");
                            System.out.println("\t\t\t 9 退出系统");
                            System.out.print("请输入你的选择:");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1" :
                                    ucs.onlineFriendList();
                                    break;
                                case "2" :
                                    System.out.print("请输入想对大家说的话:");
                                    String contentToAll = Utility.readString(100);
                                    //调用一个方法,将内容封装成Message对象发送给服务器
                                    mcs.SendMessageToAll(contentToAll,userId);
                                    break;
                                case "3" :
                                    System.out.print("请选择想要私聊的用户(在线):");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入内容:");
                                    String content = Utility.readString(100);
                                    mcs.SendMessageToOne(content,userId,getterId);
                                    break;
                                case "4" :
                                    System.out.print("请选择你的文件发送用户(在线):");
                                    getterId = Utility.readString(50);
                                    System.out.print("请输入你需要发送文件的路径(形式:d:\\xx.jpg):");
                                    String src = Utility.readString(50);
                                    System.out.print("请确定文件需要保存的路径(形式d:\\xx.jpg):");
                                    String dest = Utility.readString(50);
                                    fcs.sendFileToOne(src,dest,userId,getterId);
                                    break;
                                case "9" :
                                    ucs.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("============登录失败============");
                    }
                    break;
                case "9" :
                    System.out.println("============退出登录============");
                    loop = false;
                    break;
            }
        }
    }
}
