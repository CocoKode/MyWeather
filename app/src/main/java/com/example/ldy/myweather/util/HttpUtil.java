package com.example.ldy.myweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by LDY on 2016/9/14.
 */
public class HttpUtil {
    //和服务器交互，发送http请求，得到未解析字符串response，并在实现了
    //HttpCallbackListener接口的具体类中回调onfinish和onErroe方法进行处理

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        //开启一个新线程来处理耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    //首先设定请求方式为get
                    connection.setRequestMethod("GET");
                    //设置连接超时和读取数据超时
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //不设置请求头，获得输入流
                    InputStream in = connection.getInputStream();
                    //再套InputStreamReader和BufferedReader读入数据
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    //新建一个StringBuilder存储未解析的数据。  ps：当对string进行多次操作的时候优先选用StringBuilder
                    StringBuilder response = new StringBuilder();
                    //读取数据
                    String line;
                    //readLine会自动一行行地读下去
                    if ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    //回调listener的方法
                    if (listener != null) {
                        //listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
                //关闭连接
                finally {
                    if (connection != null)
                        connection.disconnect();
                }

            }
        }).start();
    }

}
