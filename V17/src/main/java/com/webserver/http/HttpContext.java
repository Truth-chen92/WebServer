package com.webserver.http;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类维护所有HTTP协议中固定不变的内容
 */
public class HttpContext {
    //保存所有Content-Type头的值与资源后缀的对应关系
    private static Map<String,String> mimeMapping=new HashMap<>();
    static {
        //初始化所有静态资源
        initMimeMapping();
    }

    private static void initMimeMapping(){
//        mimeMapping.put("html","text/html");
//        mimeMapping.put("css","text/css");
//        mimeMapping.put("js","application/javascript");
//        mimeMapping.put("jpg","image/jpeg");
//        mimeMapping.put("png","image/png");
//        mimeMapping.put("gif","image/gif");
       try{
//           SAXReader reader=new SAXReader();
//           Document doc=reader.read("./config/web.xml");
//           Element root=doc.getRootElement();
//           List<Element> list=root.elements("mime-mapping");
           List<Element> list=new SAXReader().read("./config/web.xml").getRootElement().elements("mime-mapping");
           for(Element ele:list) mimeMapping.put(ele.elementText("extension"),ele.elementText("mime-type"));

           System.out.println("个数为"+mimeMapping.size());
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    /**
     * 根据资源后缀名获取对应的Content-Type的值
     * @param ext 资源后缀名
     * @return    Content-Type头的值
     */
    public static String getMimeTypes(String ext){
        return mimeMapping.get(ext);
    }
}
