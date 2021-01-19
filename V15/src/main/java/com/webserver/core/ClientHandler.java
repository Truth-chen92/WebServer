package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.LoginServlet;
import com.webserver.servlet.RegServlet;

import java.io.File;
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
        try {
            //解析請求
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            //處理請求
            String path = request.getRequestURI();
            //首先根据请求路径判断是否为请求业务
            if ("/myweb/regUser".equals(path)) {
                RegServlet servlet=new RegServlet();
                servlet.service(request,response);
            } else if("/myweb/loginUser".equals(path)){
                LoginServlet loginServlet=new LoginServlet();
                loginServlet.service(request,response);
            }else {
                File file = new File("./webapps" + path);
                //判断用户请求的资源是否存在
                if (!file.exists() || !file.isFile()) {
                    file = new File("./webapps/root/404.html");
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                }
                response.setEntity(file);
            }
                //告知浏览器我们服务器是谁
                response.putHeaders("Sever", "WebServer");
                //响应客户端
                response.flush();

                //單獨捕獲空請求異常，不做任何處理，目的儘忽略處理工作
            }catch(EmptyRequestException e){
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                //最終交互完畢後與客戶端斷開連接
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


}

