package com.hspedu.qqframe;

import com.hspedu.qqserver.service.QQServer;

/**
 * 该类创建一个QQServer对象,相当于启动后台服务器
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
        System.out.println("服务器关闭");
    }
}
