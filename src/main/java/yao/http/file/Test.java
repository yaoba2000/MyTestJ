package yao.http.file;

/**
 * Created by Administrator on 2017-7-19.
 */

import okhttp3.*;
import yao.http.utils.HttpCallerUtils;
import yao.util.HandleUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {


    public static void main(String[] args) throws Exception {
//        getReq();
//        postReq();
//        try {
//            for (int i = 0; i < 10; i++)
//                postJson();
//        } catch (Exception ex) {
//            System.out.println("调用接口失败!");
//        }

        accessImageAndFile();
    }

    public static void getReq() throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params = null;
//        params.put("user","yao");
//        params.put("password","123456");
        byte[] ret = HttpCallerUtils.getStream("http://127.0.0.1:8765/source/a.png", params);

        //byte[] ret = HttpProxy.get("http://192.168.1.111:8765/images/006.jpg");
        //写出文件
        System.out.println(System.getProperty("user.dir"));//打印user.dir
        String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + "a.png";
        FileOutputStream fos = new FileOutputStream(writePath);
        fos.write(ret);
        fos.close();
    }

    public static void postReq() throws Exception {
        String url = "http://192.168.90.181:8914/home/yaoyan/uploadfile/2017-09-w3.json";
        OkHttpClient okHttpClient = new OkHttpClient();
        ///tcps/zndd/tcps/ZNDD_X/other
        RequestBody body = new FormBody.Builder()
                .add("cityName", "tcps")
                .add("typeNo", "ZNDD_01")
                .add("minData", "month")
                .add("filename", "2017-01.json")
                .build();

        Request request = new Request.Builder()
                .url(url)
//                .post(body)
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.code());
//            System.out.println(response.body().string());
            String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + "2017-09-w3.json";
            FileOutputStream fos = new FileOutputStream(writePath);
            fos.write(response.body().bytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postJson() {
        String json = "{\"vid\":\"00000000000005004\",\"channel\":1,\"cmd_flag\":0,\"net_addr\":\"rtmp://192.168.20.22/oflaDemo/dp5002\"}";
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.90.107:8910/orderInfo")
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //打印服务端返回结果
                System.out.println(response.body().string());

            }
        } catch (IOException e) {
            System.out.println("发送请求失败!");
//            e.printStackTrace();
        }

    }


    public static void accessImageAndFile() {
        // TODO Auto-generated method stub
        OkHttpClient okClient = new OkHttpClient();
        Request request = new Request.Builder()
//                .url("http://192.168.90.181:8914/home/yaoyan/uploadfile/a.png")
                .url("http://192.168.90.107:7777/source/")
                .header("Range","bytes=0-100")
                .get()
                .build();
        try {
            okClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Call arg0, Response arg1) throws IOException {
                    // TODO Auto-generated method stub
                    byte[] buffer = arg1.body().bytes();
//                    OutputStream oStream = new FileOutputStream("D:\\test\\downfile\\a.png");
//                    oStream.write(buffer);
//                    oStream.close();
                    System.out.println(HandleUtil.BytesToHexString(buffer));
                }

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    // TODO Auto-generated method stub
                    System.out.println("获取服务器数据失败");
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
    }

}
