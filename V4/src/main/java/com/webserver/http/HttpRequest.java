package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 請求對象
 * 該類的每一個實例用於表示HTTP的一個請求
 * 每個請求由三部分構成：請求行、消息頭、消息正文
 */
public class HttpRequest {
    private Socket socket;
    private Map<String,String> headers=new HashMap<>();
    //請求行相關信息
    //請求行中的請求方式
    private String method;
    //抽象路徑部分
    private String uri;
    //協議版本
    private String portocol;
    //消息頭相關信息
    //消息正文相關信息
    public HttpRequest(Socket socket){
        this.socket=socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }

    private void parseRequestLine(){
        System.out.println("解析请求行...");
        try {
            String line=readline();
            System.out.println("請求行："+line);
            String[]data=line.split("\\s");
            method=data[0];
            uri=data[1];
            portocol=data[2];
            System.out.println(method);
            System.out.println(uri);
            System.out.println(portocol);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("解析完毕...");
    }

    private void parseHeaders(){
        System.out.println("解析消息头..");
        try {
            while (true){
                String line=readline();
                if(line.isEmpty()) break;
                String[]str=line.split(": ");
                headers.put(str[0],str[1]);
                System.out.println(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(headers);

        System.out.println("解析完毕..");
    }

    private void parseContent(){
        System.out.println("解析正文..");

        System.out.println("解析完毕..");
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

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getPortocol() {
        return portocol;
    }
}
