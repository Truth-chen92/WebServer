package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 每個客戶端連接後都會啟動一個線程來完成與該客戶的交互
 * 交互過程遵循HTTP協議的一問一答的要求，分三步進行處理
 * 1：解析請求
 * 2：處理請求
 * 3：響應客戶端
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }
    public void run() {
        try{
            //解析請求
            /*
                讀取客戶端發送過來的第一行字符串（CRLF結尾）
             */
            InputStream in=socket.getInputStream();
            int d;
            char cur='a',pre='a';//cur表示本次讀取的字符，pre表示上次讀取的字符
            StringBuilder builder=new StringBuilder();
            while ((d=in.read())!=-1){
                cur=(char)d;
                //如果上次讀取的是回車符，本次讀取的是換行符
                if(pre==13&&cur==10)break;
                builder.append(cur);
                pre=cur;
            }
            String line=builder.toString().trim();
            System.out.println("請求行："+line);
            //請求行中的請求方式
            //抽象路徑部分
            //協議版本
            String[]data=line.split("\\s");
            System.out.println(data[0]);
            System.out.println(data[1]);
            System.out.println(data[2]);
            //處理請求
            
            //響應客戶端

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            //最終交互完畢後與客戶端斷開連接
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
