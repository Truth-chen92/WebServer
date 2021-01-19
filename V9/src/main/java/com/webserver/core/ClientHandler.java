package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
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
        try {
            //解析請求
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response=new HttpResponse(socket);
            //處理請求
            String path=request.getUri();
            File file=new File("./webapps"+path);
            //判断用户请求的资源是否存在
            if(file.exists()&&file.isFile()) {
                response.setEntity(file);
                Map<String,String>mimeMapping=new HashMap<>();
                mimeMapping.put("html","text/html");
                mimeMapping.put("css","text/css");
                mimeMapping.put("js","application/javascript");
                mimeMapping.put("jpg","image/jpeg");
                mimeMapping.put("png","image/png");
                mimeMapping.put("gif","image/gif");
                int index=file.getName().lastIndexOf(".")+1;
                String ext=file.getName().substring(index);
                String mime=mimeMapping.get(ext);
                response.putHeaders("Content-type",mime);
                response.putHeaders("Content-Length",file.length()+"");
            }else{
                file = new File("./webapps/root/notfound.html");
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                response.setEntity(file);
                response.putHeaders("Content-type","text/html");
                response.putHeaders("Content-Length",file.length()+"");
            }
            //响应客户端
            response.flush();
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

