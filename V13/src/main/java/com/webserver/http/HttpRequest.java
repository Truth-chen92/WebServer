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
    private Map<String, String> headers = new HashMap<>();
    //請求行中的請求方式
    private String method;
    //抽象路徑部分
    private String uri;
    //協議版本
    private String protocol;
    //用来保存uri中的请求部分
    private String requestURI;
    //用来保存uri中的参数部分
    private String queryString;
    //用来保存每一组参数。key:存参数名，value:存参数值
    private Map<String, String> parameters = new HashMap<>();

    public HttpRequest(Socket socket) throws EmptyRequestException {
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }

    //請求行相關信息
    private void parseRequestLine() throws EmptyRequestException {
        System.out.println("解析请求行...");
        try {
            String line = readline();
            //如果讀取請求行內容返回的空串，說明本次為空請求
            if (line.isEmpty())
                throw new EmptyRequestException();
            System.out.println("請求行：" + line);
            String[] data = line.split("\\s");
            method = data[0];
            uri = data[1];
            protocol = data[2];
            //进一步解析uri
            parseUri();

            System.out.println(method);
            System.out.println(uri);
            System.out.println(protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("解析完毕...");
    }

    /**
     * 进一步对uri进行拆分解析，因为uri可能包含参数
     */
    private void parseUri() {
        System.out.println("进一步解析uri...");
        /*
            uri可能存在两种情况：含有参数，不含有参数
            含有参数（uri中包含"?"）：
            首先按照"?"将uri拆分为两部分，第一部分赋值给requestURI
            第二部分赋值给queryString
            然后再将queryString（uri的参数部分）进行进一步拆分，来得到每一组参数
            首先将queryString按照"&"拆分，再按"="拆分出参数名与参数值
            保存到Map中
            不含有参数(没有"?")
            则直接将uri赋值给requestURI
         */
        if(uri.contains("?")) {
            String[] data = uri.split("\\?");
            requestURI = data[0];
            if (data.length > 1) {
                queryString = data[1];
                data = queryString.split("&");
                for (String para : data) {
                    String[] arr = para.split("=");
                    if(arr.length>1) {
                        parameters.put(arr[0], arr[1]);
                    }else{
                        parameters.put(arr[0],null);
                    }
                }
            }
        }else{
            requestURI=uri;
        }
        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
        System.out.println("进一步解析uri完毕！！！");
    }

    //消息頭相關信息
    private void parseHeaders() {
        System.out.println("解析消息头..");
        try {
            while (true) {
                String line = readline();
                if (line.isEmpty()) break;
                String[] str = line.split(": ");
                headers.put(str[0], str[1]);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(headers);

        System.out.println("解析完毕..");
    }

    //消息正文相關信息
    private void parseContent() {
        System.out.println("解析正文..");

        System.out.println("解析完毕..");
    }

    private String readline() throws IOException {
        /*
            同一個socket對象，無論調用多少次getInputStream方法
            獲取到的輸入流都是同一個
         */
        InputStream in = socket.getInputStream();
        int d;
        char cur = 'a', pre = 'a';//cur表示本次讀取的字符，pre表示上次讀取的字符
        StringBuilder builder = new StringBuilder();
        while ((d = in.read()) != -1) {
            cur = (char) d;
            //如果上次讀取的是回車符，本次讀取的是換行符
            if (pre == 13 && cur == 10) break;
            builder.append(cur);
            pre = cur;
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

    public String getProtocol() {
        return protocol;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }
}
