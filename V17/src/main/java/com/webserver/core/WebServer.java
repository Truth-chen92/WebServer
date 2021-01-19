package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer主類
 */
public class WebServer {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    /**
     * 構造器，用於初始化
     */
    public WebServer(){

        try {
            System.out.println("正在啟動服務端...");
            serverSocket=new ServerSocket(8088);
            threadPool=Executors.newFixedThreadPool(8088);
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
            while (true){
                System.out.println("等待客戶端連接...");
                Socket socket=serverSocket.accept();
                System.out.println("一個客戶端連接了！");
                //啟動一個線程來處理該客戶端的交互工作
                ClientHandler handler=new ClientHandler(socket);
                threadPool.execute(handler);
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
