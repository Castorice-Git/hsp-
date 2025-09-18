package com.hspedu.qqcommon;

/**
 * 表示消息类型有哪些
 */
public interface MessageType {
    //1.在接口中定义一些常量
    //2.不同常量的值,表示不同的消息类型
    String MESSAGE_LOGIN_SUCCEED = "1";//表示登陆成功
    String MESSAGE_LOGIN_FAIL = "2";//表示登录失败
    String MESSAGE_COMM_MES = "3";//普通信息对象
    String MESSAGE_GET_ONLINE_FRIEND = "4";//请求获取用户在线列表
    String MESSAGE_RETURN_ONLINE_FRIEND = "5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";//客户端请求退出
    String MESSAGE_TO_ALL_MEG = "7";//群发消息
    String MESSAGE_FILE_MEG = "8";//传输文件
    String MESSAGE_OFFLINE = "9";//离线留言
}
