package com.hspedu.qqclent.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.*;

/**
 * 该类完成文件传输服务
 */
@SuppressWarnings({"all"})
public class FileClientService {
    /**
     *
     * @param src 源文件路径
     * @param dest 接收路径
     * @param sendId 发送者
     * @param getterId 接收者
     */
    public void sendFileToOne(String src, String dest, String sendId, String getterId) {
        //读取src文件,封装到Message对象
        Message message = new Message();
        message.setMegType(MessageType.MESSAGE_FILE_MEG);
        message.setSender(sendId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        //读取文件
        FileInputStream fis = null;
        byte[] fileByte = new byte[(int)new File(src).length()];

        try {
            fis = new FileInputStream(src);
            fis.read(fileByte);//将src文件读入到程序的字节数组
            message.setFileBytes(fileByte);//将文件对应的字节数组设置到message
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //关闭
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //提示信息
        System.out.println("\n" + "你 给 " + getterId + " 发送文件: " + src + " 到对方的 " + dest + " 目录");
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sendId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
