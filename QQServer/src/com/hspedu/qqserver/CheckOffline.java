package com.hspedu.qqserver;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.User;
import com.hspedu.qqserver.service.ManageServerConnectClientThread;
import com.hspedu.qqserver.service.ServerConnectClientThread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings({"all"})
public class CheckOffline {
    //离线留言
    //客户端A向客户端B发送离线信息
    //1.客户端A将Message对象封装好发送到服务器管理离线留言的集合中ConcurrentHashMap<getterId,ArrayList<Message>>
    //2.在用户登录后检测ConcurrentHashMap中是否有对应ID
    //3.离线留言发送完毕后从ConcurrentHashMap中删除对应ID
    private static HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHashMap();

    public static boolean checkOffline(Message msg) {
        Iterator<String> onlineList = hm.keySet().iterator();
        boolean check = false;
        while (onlineList.hasNext()) {
            if (msg.getGetter().equals(onlineList.next())) {
                check = true;
                break;
            }
        }
        return check;
    }
}
