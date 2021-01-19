本版本通过解析tomcat提供的web.xml文件将HttpContext中保存所有资源后缀与对应的Content-Type值
的Map属性mimeMapping初始化完毕，使得里面保存所有的类型1000多种

实现：
1：在项目目录下新建目录config
2：在tomcat提供的web.xml文件拷贝到config目录下
3：在WebServer项目的配置文件pom.xml文件中加入dom4j的jar包导入
3：重构com.webserver.http包中的HttpContext类的方法initMimeMapping
   方法中将原来固定想mimeMapping中添加6组键值对的操作删除。改为通过解析web.xml文件初始化
   使用dom4j解析config/web.xml文件。将跟标签下所有名为</mime-mapping>的子标签获取到，
   并将每个</mime-mapping>中的子标签：
   <extension>中间的文本作为key，<mime-type>中间的文本作为value保存到mimeMapping这个Map中完成
   初始化工作，初始化后这个Map的size应当有1011个