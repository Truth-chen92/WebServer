package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 響應對象，當前類的每一個實例用於表示服務端發送給客戶端的一個標準的
 * HTTP響應內容
 * 每個響應由三部分構成：狀態行，響應頭，響應正文
 */
public class HttpResponse {
    //狀態行相關信息
    private int statusCode = 200;//状态代码默认200
    private String statusReason = "OK";//状态描述默认OK
    //響應頭相關信息
    private Map<String, String> headers = new HashMap<>();
    //響應正文相關信息
    private File entity;

    private Socket socket;

    public HttpResponse(Socket socket) {
        this.socket = socket;
    }

    /**
     * 將當前響應對象內容以標準的HTTP響應格式發送給客戶端
     */
    public void flush() {
        sendStatusLine();
        sendHeaders();
        sendContent();
    }

    /**
     * 發送狀態行
     */
    private void sendStatusLine() {
        System.out.println("开始发送状态行！");
        try {
            OutputStream out = socket.getOutputStream();
            String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//發送一個回車符
            out.write(10);//發送一個換行符
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("状态行结束！");
    }

    /**
     * 發送響應頭
     */
    private void sendHeaders() {
        System.out.println("开始发送响应头！");
        try {
            OutputStream out = socket.getOutputStream();
            //遍历headers，将所有响应头发送出去
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> e : set) {
                String name = e.getKey();
                String value = e.getValue();
                String line = name + ": " + value;
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("响应头" + line);
            }
            //單獨發送CRLF表示響應頭發送完畢
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("响应头结束！");
    }

    /**
     * 發送響應正文
     */
    private void sendContent() {
        System.out.println("开始发送响应正文！");
        if (entity != null) {
            try (
                    FileInputStream fis = new FileInputStream(entity);
            ) {
                OutputStream out = socket.getOutputStream();
                int len;//實際每次讀取到的字節數
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("正文结束！");
    }

    public File getEntity() {
        return entity;
    }

    /**
     * 设置响应正文的文件，设置的同时会根据该文件添加两个响应头：Content-Type和Content-Length
     *
     * @param entity
     */
    public void setEntity(File entity) {
        this.entity = entity;
        int index = entity.getName().lastIndexOf(".") + 1;
        String ext = entity.getName().substring(index);
        String mime = HttpContext.getMimeTypes(ext);
        putHeaders("Content-Type", mime);
        putHeaders("Content-Length", entity.length() + "");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void putHeaders(String name, String value) {
        this.headers.put(name, value);
    }
}
