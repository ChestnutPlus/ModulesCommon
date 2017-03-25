package com.chesnut.Common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年11月30日17:05:55
 *     desc  : Http基本请求： Get, Post
 *     thanks To:
 *     dependent on:
 * </pre>
 */
public class HttpUtils {

    /**
     * 阻塞的Post请求
     * @param strUrlPath 请求地址
     * @param params 请求参数
     * @return 结果
     */
    public static String submitPostData(String strUrlPath,Map<String, String> params) {

        byte[] data = getRequestData(params, "UTF-8").toString().getBytes();//获得请求体
        try {

            //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = new URL(strUrlPath);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(1500);          //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码

            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }

        } catch (IOException e) {
            //e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }

    /**
     * 拼接Get参数
     * @param url   地址
     * @param params    参数
     * @return
     */
    public static String assemblyGetParam(String url, Map<String, String> params) {
        StringBuilder s = new StringBuilder();
        s.append(url).append("?");
        for (String temp:
                params.keySet()){
            s.append(temp).append("=").append(params.get(temp)).append("&");
        }
        s.deleteCharAt(s.length()-1);
        return s.toString();
    }

    /**
     * 阻塞的Get请求
     * @param apiUrl    请求地址
     * @param map       请求参数
     * @return          返回结果
     */
    public static String submitGetData(String apiUrl,Map<String,String> map){

        StringBuilder s = new StringBuilder();
        s.append(apiUrl).append("?");

        for (String temp:
                map.keySet()){
            s.append(temp).append("=").append(map.get(temp)).append("&");
        }
        s.deleteCharAt(s.length()-1);

        URL url;
        try {
            url = new URL(s.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(5000);//将读超时设置为指定的超时，以毫秒为单位。用一个非零值指定在建立到资源的连接后从 Input 流读入时的超时时间。如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。
            con.setDoInput(true);//指示应用程序要从 URL 连接读取数据。
            con.setRequestMethod("GET");//设置请求方式
            if(con.getResponseCode() == 200){//当请求成功时，接收数据（状态码“200”为成功连接的意思“ok”）
                InputStream is = con.getInputStream();
                return dealResponseResult(is);
            }
            else {
                System.out.println("HttpGetResponseCode:"+con.getResponseCode());
                return "{\"success\":0}";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "{\"success\":0}";
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "{\"success\":0}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"success\":0}";
        }
    }

    /**
     * 封装请求体信息
     * @param params params请求体内容，
     * @param encode encode编码格式
     * @return StringBuffer
     */
    private static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * 处理服务器的响应结果（将输入流转化成字符串）
     * @param inputStream inputStream服务器的响应输入流
     * @return 返回结果
     */
    private static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
}
