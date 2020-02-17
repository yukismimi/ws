package client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpClient {

    /**
     * 禁止实例化
     */
    private MyHttpClient() {
        throw new IllegalStateException("工具类禁止实例化");
    }

    private static final String CHARSET_NAME = "UTF-8";

    public static String doPost(String uri, String xml) {
        try{
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//设置请求方式为POST
            conn.setDoOutput(true);//允许写出
            conn.setDoInput(true);//允许读入

            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("SOAPAction", "");//必须加 表示soap请求

            conn.setReadTimeout(5000); // 读取超时
            conn.setConnectTimeout(5000); // 服务器响应超时
            conn.setUseCaches(false); // 不使用缓存
            conn.connect();//连接
            OutputStream out = conn.getOutputStream(); // 获取输出流对象
            conn.getOutputStream().write(xml.getBytes("UTF-8")); // 将要提交服务器的SOAP请求字符流写入输出流
            out.flush();
            out.close();

            // 通过connection连接，获取输入流
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                // 封装输入流is，并指定字符集
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                String result = sbf.toString();
                return result;
            }
        }catch (Exception e){

        }
        return "end";
    }

    public static void main(String[] args) {

        String requestXml = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <SOAP-ENV:Header/>\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <method:sayHelloWorldFrom xmlns:method=\"http://example/\">\n" +
                "            <sayHelloWorldFromRequest>Yuki</sayHelloWorldFromRequest>\n" +
                "        </method:sayHelloWorldFrom>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        String result = MyHttpClient.doPost("http://localhost:8088/ws_war_exploded/services/HelloWorld", requestXml);
        System.out.println(result);
    }
}
