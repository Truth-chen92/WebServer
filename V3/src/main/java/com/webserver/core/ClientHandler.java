package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            String line=readline();
            System.out.println("請求行："+line);
            String[]data=line.split("\\s");
            //請求行中的請求方式
            String method=data[0];
            //抽象路徑部分
            String uri=data[1];
            //協議版本
            String portocol=data[2];
            System.out.println(method);
            System.out.println(uri);
            System.out.println(portocol);
            //解析消息頭
            Map<String,String>headers=new HashMap<>();
            while (true){
                line=readline();
                if(line.isEmpty()) break;
                String[]str=line.split(": ");
                headers.put(str[0],str[1]);
                System.out.println(line);
            }
            System.out.println(headers);
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
    private String readline() throws IOException {
        /*
            同一個socket對象，無論調用多少次getInputStream方法
            獲取到的輸入流都是同一個
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
        return builder.toString().trim();

    }
}
