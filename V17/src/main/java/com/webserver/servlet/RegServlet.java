package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * 处理用户注册业务
 */
public class RegServlet extends HttpServlet{
    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("开始处理注册...");
        //1获取用户在注册页面上输入的注册信息
        //这里getParameter方法传入的参数值应当与注册页面表单中输入框name属性值一致
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        String nickname = request.getParameters("nickname");
        String ageStr = request.getParameters("age");
        System.out.println(username + password + nickname + ageStr);
        /*
            添加对用户输入的信息的验证工作
            要求：
            如果用户名、密码、昵称和年龄为null,或者年龄输入的不是一个数字（正则表达式）
            则直接设置response响应一个注册失败的提示
            该页面放在webapps/myweb目录下，名字为reg_input_error.html
            然后加一个超链接返回到到注册页面
            只有验证通过了，才进行下面的注册操作
         */
        if (username == null || password == null || nickname == null || ageStr==null||ageStr.matches("\\D+")) {
            File file = new File("./webapps/myweb/reg_input_error.html");
            response.setEntity(file);
            return;
        }
        int age = Integer.parseInt(ageStr);
        //2将该用户信息写入user.dat文件保存
        try (
                RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");
        ) {
            /*
                先读取user.dat文件中现有的所有数据，将每条记录的用户名读取出来并与
                当前注册用户的用户名对比，如果已经存在，则响应have_user.html
                否则才执行下面的语句
             */
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data, "UTF-8").trim();
                if(name.equals(username)){
                    File file = new File("./webapps/myweb/have_user.html");
                    response.setEntity(file);
                    return;
                }
            }

            raf.seek(raf.length());
            byte[] data = username.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            data = password.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            data = nickname.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            raf.writeInt(age);
            System.out.println("注册完毕！！！");
            //3将注册成功页面设置到response的正文上
            File file = new File("./webapps/myweb/reg_success.html");
            response.setEntity(file);
        } catch (IOException e) {
        }

        //3响应用户注册结果页面（成功或失败页面）
        System.out.println("处理注册完毕！！！");
    }
}
