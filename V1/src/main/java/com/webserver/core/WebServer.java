package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主類
 */
public class WebServer {
    private ServerSocket serverSocket;

    /**
     * 構造器，用於初始化
     */
    public WebServer(){
        try {
            System.out.println("正在啟動服務端...");
            serverSocket=new ServerSocket(8088);
            System.out.println("服務端啟動完畢！");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 服務端開始工作的方法
     */
    public void start(){
        try {
            System.out.println("等待客戶端連接...");
            Socket socket=serverSocket.accept();
            System.out.println("一個客戶端連接了！");
            /*
                如果讀取客戶端發送過來的內容是亂碼，通常是因為瀏覽器地址欄中
                輸入的是https協議！！！
                要使用http協議才可以：
                http://localhost:8088
             */
            //通過socket獲取輸入流，讀取客戶端發送過來的內容
            InputStream in=socket.getInputStream();
            int d;
            while ((d=in.read())!=-1){
                System.out.print((char)d);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer server=new WebServer();
        server.start();
    }
}
