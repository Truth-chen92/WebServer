package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        try {
            //解析請求
            HttpRequest request = new HttpRequest(socket);
            //處理請求

            //響應客戶端
            /*
                發送一個標準的HTTP響應，將剛才寫好的頁面：
                ./webapps/myweb/index.html
                響應的格式
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                10010100100111001......
             */
            File file=new File("./webapps/myweb/index.html");
            OutputStream out=socket.getOutputStream();
            String line="HTTP/1.1 200 OK";
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//發送一個回車符
            out.write(10);//發送一個換行符
            //發送響應頭
            line="Content-Type: text/html";
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);

            line="Content-Length: "+file.length();
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
            //單獨發送CRLF表示響應頭發送完畢
            out.write(13);
            out.write(10);

            //發送響應正文
            FileInputStream fis=new FileInputStream(file);
            int len;//實際每次讀取到的字節數
            byte[]data=new byte[1024*10];
            while ((len=fis.read(data))!=-1){
                out.write(data,0,len);
            }
            System.out.println("響應發送完畢！");

        //單獨捕獲空請求異常，不做任何處理，目的儘忽略處理工作
        }catch(EmptyRequestException e){
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

