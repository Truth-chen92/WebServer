package com.webserver.core;

import com.webserver.http.HttpRequest;

import java.io.IOException;
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
            HttpRequest request=new HttpRequest(socket);
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

