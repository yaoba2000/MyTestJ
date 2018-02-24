package yao.http.file;

import yao.http.utils.SecureChatSslContextFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017-8-7.
 */
public class HttpRequestTest {


    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
//        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
// 创建SSLContext对象，并使用我们指定的信任管理器初始化


// 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = SecureChatSslContextFactory.getClientContext().getSocketFactory();

            URL url = new URL(requestUrl);
            //https请求
//            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
//            httpUrlConn.setSSLSocketFactory(ssf);
            //普通Http请求
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
// 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)){
                httpUrlConn.connect();
            }


// 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
// 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

//// 将返回的输入流转换成字符串
//            InputStream inputStream = httpUrlConn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String str = null;
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            bufferedReader.close();
//            inputStreamReader.close();
//// 释放资源
//            inputStream.close();
//            inputStream = null;
//            httpUrlConn.disconnect();
//            System.out.println("返回的数据：" + buffer.toString());
//// jsonObject = JSONObject.fromObject(buffer.toString());
            byte[] ret = readStream(httpUrlConn);
            System.out.println(System.getProperty("user.dir"));//打印user.dir
            String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" +  File.separatorChar + "a.png";
            FileOutputStream fos = new FileOutputStream(writePath);
            fos.write(ret);
            fos.close();
        } catch (ConnectException ce) {
            System.out.println("Weixin server connection timed out.");
        } catch (Exception e) {
            System.out.println("https request error:{}" + e);
        }
        return buffer.toString();
    }

    private static byte[] readStream(HttpURLConnection conn) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = conn.getInputStream();
            byte[] buf = new byte[1024];
            int num = -1;
            bos = new ByteArrayOutputStream();
            while((num = is.read(buf, 0 , buf.length)) != -1 ){
                bos.write(buf, 0, num);
            }
        }
        finally {
            if (bos != null) {
                bos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        HttpRequestTest.httpRequest("http://127.0.0.1:8765/source/a.png","GET","names=whf&abc=123");
    }
}
