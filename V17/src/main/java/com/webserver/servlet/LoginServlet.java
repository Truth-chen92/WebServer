package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet{
    public void service(HttpRequest request, HttpResponse response) {
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        try(
                RandomAccessFile raf=new RandomAccessFile("user.dat","r");
        ){
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data, "UTF-8").trim();
                raf.read(data);
                String mima=new String(data,"UTF-8").trim();
                if(name.equals(username)&&mima.equals(password)){
                    File file = new File("./webapps/myweb/login_success.html");
                    response.setEntity(file);
                    return;
                }
            }
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
        }catch (IOException e){
        }
    }
}
