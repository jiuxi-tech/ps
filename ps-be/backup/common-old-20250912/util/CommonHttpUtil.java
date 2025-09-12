package com.jiuxi.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.bean.FormFile;
import com.jiuxi.common.bean.InputStreamRequestBody;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: CommonHttpUtil
 * @Description: CommonHttpUtil工具类，封装了常用的 同步请求，异步请求，json请求，下载请求，预览请求，同时支持http,https等
 * @Author: 杨攀
 * @Date: 2021/12/24 9:35
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class CommonHttpUtil {


    private static final Logger LOGGER    = LoggerFactory.getLogger (CommonHttpUtil.class);


    /**
     * @Description: 构建 Http 的url
     * @param @param ip
     * @param @param port
     * @param @param applicationName  应用名，如： topinfo-demo
     * @param @param path 请求接口地址，如： /sys/demo/download
     * @param @return 参数
     * @return String
     */
    public static String buidHttpUrl(String ip, String port,String applicationName, String path) {
        StringBuffer sb = new StringBuffer ();
        sb.append ("http://").append (ip).append (":").append (port);

        if(StrUtil.isNotBlank (applicationName)) {
            sb.append ("/").append (applicationName);
        }

        if(StrUtil.isNotBlank (path)) {
            sb.append (path);
        }

        String url = sb.toString ();
        sb.setLength (0);
        sb = null;

        return url;
    }


    /**
     * @Description: 构建 Https 的url
     * @param @param ip
     * @param @param port
     * @param @param applicationName  应用名，如： topinfo-demo
     * @param @param path 请求接口地址，如： /sys/demo/download
     * @param @return 参数
     * @return String
     */
    public static String buidHttpsUrl(String ip, String port, String applicationName, String path) {
        StringBuffer sb = new StringBuffer ();
        sb.append ("https://").append (ip).append (":").append (port);

        if(StrUtil.isNotBlank (applicationName)) {
            sb.append ("/").append (applicationName);
        }

        if(StrUtil.isNotBlank (path)) {
            sb.append (path);
        }

        String url = sb.toString ();
        sb.setLength (0);
        sb = null;

        return url;
    }


    /**
     * 拼接url和请求参数.
     *
     * @param url       url
     * @param paramsMap key value
     * @return String url
     */
    private static String urlJoint(String url, Map<String, String> paramsMap) throws UnsupportedEncodingException {

        StringBuilder endUrl = new StringBuilder(url);
        if (null != paramsMap) {
            boolean isFirst = true;
            Set<Map.Entry<String, String>> entrySet = paramsMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                if (isFirst && !url.contains("?")) {
                    isFirst = false;
                    endUrl.append("?");
                } else {
                    endUrl.append("&");
                }
                endUrl.append(entry.getKey());
                endUrl.append("=");
                endUrl.append(entry.getValue());
                //endUrl.append(URLEncoder.encode (entry.getValue (), "UTF-8"));
            }
        }
        return endUrl.toString();
    }

    /**
     * 同步的Get请求.
     * @author 杨攀
     * @date 2021/12/24 9:42
     * @param okHttpClient  客户端实例
     * @param url  请求路径
     * @param paramsMap 请求参数
     * @return java.lang.String
     */
    public static String getSync(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap) throws IOException {
        return getSync(okHttpClient, url, paramsMap, null);
    }

    /**
     * 添加头信息
     * @author 杨攀
     * @date 2023/10/11 13:29
     * @param headersMap
     * @param requestBuilder
     * @return void
     */
    private static void requestAddHeader(Map<String, String> headersMap, Request.Builder requestBuilder) {
        if (null != headersMap) {
            // 在这对添加的头进行遍历
            for (Map.Entry<String, String> map : headersMap.entrySet()) {

                String key = map.getKey();
                String value;
                //判断值是否是空的
                if (map.getValue() == null) {
                    value = "";
                } else {
                    value = map.getValue();
                }
                //把key和value添加到头信息中
                requestBuilder.addHeader(key, value);
            }
        }
    }

    /**
     * 同步的Get请求.
     * @author 杨攀
     * @date 2021/12/24 9:42
     * @param okHttpClient  客户端实例
     * @param url  请求路径
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @return java.lang.String
     */
    public static String getSync(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, Map<String, String> headersMap) throws IOException {

        //拼接url和请求参数
        String toUrl = urlJoint(url, paramsMap);

        LOGGER.debug("------请求:" + toUrl);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(toUrl);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        final Request request = requestBuilder.build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            // 返回值为response
            response = call.execute();
            // 将response转化成String
            if (response.isSuccessful()) {
                // 将response转化成String
                String  responseStr = response.body().string();
                LOGGER.debug("------请求返回:" + responseStr);
                return responseStr;
            }
        }catch (Exception e){
            throw e;
        }

        return null;
    }


    /**
     * 添加 post 请求的参数
     * @author 杨攀
     * @date 2023/10/11 13:58
     * @param paramsMap
     * @param formBody
     * @return void
     */
    private static void requestAddPostParam(Map<String, String> paramsMap, FormBody.Builder formBody) {
        if (null != paramsMap) {
            // 在这对添加的参数进行遍历
            for (Map.Entry<String, String> map : paramsMap.entrySet()) {

                String key = map.getKey();
                String value;
                //判断值是否是空的
                if (map.getValue() == null) {
                    value = "";
                } else {
                    value = map.getValue();
                }
                //把key和value添加到formBody中
                formBody.add(key, value);
            }
        }
    }

    /**
     * 异步的Post请求. body方式
     * @author 杨攀
     * @date 2023/1/5 20:30
     * @param okHttpClient
     * @param url       请求路径
     * @param json      请求参数
     * @param headersMap 请求头信息
     * @param callback 回调
     * @return void
     */
    public  static void postBodyAsync(OkHttpClient okHttpClient, String url, String json, Map<String, String> headersMap, Callback callback) throws IOException{

        LOGGER.debug("------请求:" + url);

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=UTF-8"));

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        if (null != headersMap) {
            // 在这对添加的头进行遍历
            for (Map.Entry<String, String> map : headersMap.entrySet()) {

                String key = map.getKey();
                String value;
                //判断值是否是空的
                if (map.getValue() == null) {
                    value = "";
                } else {
                    value = map.getValue();
                }
                //把key和value添加到头信息中
                requestBuilder.addHeader(key, value);
            }
        }

        // 创建一个Request
        Request request = requestBuilder.build();

        Call call = okHttpClient.newCall(request);
        // 返回值为response
        call.enqueue(callback);

    }

    /**
     * 异步的Post请求. 表单
     * @author 杨攀
     * @date 2023/1/5 20:30
     * @param okHttpClient
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @param callback  回调
     * @return void
     */
    public  static void postAsync(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, Callback callback) throws IOException{
         postAsync(okHttpClient, url, paramsMap, null, callback);
    }

    /**
     * 异步的Post请求. 表单
     * @author 杨攀
     * @date 2023/1/5 20:30
     * @param okHttpClient
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @param callback 回调
     * @return void
     */
    public  static void postAsync(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, Map<String, String> headersMap, Callback callback) throws IOException{

        LOGGER.debug("------请求:" + url);

        FormBody.Builder formBody = new FormBody.Builder();
        // 添加 post 请求的参数
        requestAddPostParam(paramsMap, formBody);

        RequestBody requestBody = formBody.build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        Request request = requestBuilder.build();

        Call call = okHttpClient.newCall(request);

        if (callback != null) {
            // 返回值为response
            call.enqueue(callback);
        } else {
            // 默认处理
            call.enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LOGGER.error("------请求异常:{}" , e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        // 将response转化成String
                        String  responseStr = response.body().string();
                        LOGGER.debug("------请求返回:{}" ,responseStr);
                    }else{
                        LOGGER.error("------请求返回Code:{}" ,response.code());
                    }
                }
            });
        }
    }


    /**
     * 同步的Post请求. 表单
     *
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @return responseStr 返回字符串
     */
    public  static String postSync(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap) throws IOException{
        return postSync(okHttpClient, url, paramsMap, null);
    }

    /**
     * 同步的Post请求. 表单
     *
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @return responseStr 返回字符串
     */
    public  static String postSync(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, Map<String, String> headersMap) throws IOException{

        LOGGER.debug("------请求:" + url);

        FormBody.Builder formBody = new FormBody.Builder();

        // 添加 post 请求的参数
        requestAddPostParam(paramsMap, formBody);

        RequestBody requestBody = formBody.build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        final Request request = requestBuilder.build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            // 返回值为response
            response = call.execute();
            // 将response转化成String
            if (response.isSuccessful()) {
                // 将response转化成String
                String  responseStr = response.body().string();
                LOGGER.debug("------请求返回:" + responseStr);
                return responseStr;
            }
        }catch (Exception e){
            throw e;
        }
        return null;
    }


    /**
     * 同步的Post请求. JSON
     *
     * @param url       请求路径
     * @param json 请求参数
     * @return responseStr 返回字符串
     */
    public  static String postSync(OkHttpClient okHttpClient, String url, String json) throws IOException{
        return postSync(okHttpClient, url, json, null);
    }

    /**
     * 同步的Post请求. JSON
     *
     * @param url       请求路径
     * @param json 请求参数
     * @param headersMap 请求头信息
     * @return responseStr 返回字符串
     */
    public  static String postSync(OkHttpClient okHttpClient, String url, String json, Map<String, String> headersMap) throws IOException{

        LOGGER.debug("------请求:" + url);

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=UTF-8"));

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        final Request request = requestBuilder.build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            // 返回值为response
            response = call.execute();
            // 将response转化成String
            if (response.isSuccessful()) {
                // 将response转化成String
                String  responseStr = response.body().string();
                LOGGER.debug("------请求返回:" + responseStr);
                return responseStr;
            }
        }catch (Exception e){
            throw e;
        }
        return null;
    }

    /**
     * 同步文件上传
     * @author 杨攀
     * @date 2022/6/22 11:25
     * @param okHttpClient
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @param files  上传的文件
     * @return java.lang.String
     */
    public  static String postSyncMultipartFile(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, List<FormFile> files) throws IOException{
        return postSyncMultipartFile(okHttpClient, url, paramsMap, files, null);
    }

    /**
     * 同步文件上传
     * @author 杨攀
     * @date 2022/6/22 11:27
     * @param okHttpClient
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @param files  上传的文件
     * @param headersMap 请求头信息
     * @return java.lang.String
     */
    public  static String postSyncMultipartFile(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, List<FormFile> files, Map<String, String> headersMap) throws IOException{

        // 创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 设置类型是  multipart/form-data 表单
        builder.setType(MultipartBody.FORM);

        // 添加参数
        if (null != paramsMap) {
            // 在这对添加的参数进行遍历
            for (Map.Entry<String, String> map : paramsMap.entrySet()) {

                String key = map.getKey();
                String value;
                //判断值是否是空的
                if (map.getValue() == null) {
                    value = "";
                } else {
                    value = map.getValue();
                }
                //把key和value添加到formBody中
                builder.addFormDataPart(key, value);
            }
        }

        // 添加文件信息
        for (FormFile file : files){
            if(file.getFile() == null){
                // 添加上传的文件  File
                builder.addFormDataPart(file.getParameterName(), // 参数名称
                        file.getFileName(), // 文件名称
                        InputStreamRequestBody.create(file.getInputStream(), MediaType.parse(guessMimeType(file.getFileName()))));
            }else {
                // 添加上传的文件  byte
                builder.addFormDataPart(file.getParameterName(), // 参数名称
                        file.getFileName(), // 文件名称
                        RequestBody.create(file.getFile(), MediaType.parse(guessMimeType(file.getFileName()))));
            }
        }

        MultipartBody requestBody = builder.build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        final Request request = requestBuilder.build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            // 返回值为response
            response = call.execute();
            // 将response转化成String
            if (response.isSuccessful()) {
                // 将response转化成String
                String  responseStr = response.body().string();
                LOGGER.debug("------请求返回:" + responseStr);
                return responseStr;
            }
        }catch (Exception e){
            throw e;
        }
        return null;
    }

    /**
     * 根据文件名猜测 MimeType
     * @author 杨攀
     * @date 2022/6/22 10:29
     * @param fileName
     * @return java.lang.String
     */
    private static String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 下载文件
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param paramsMap 请求参数
     * @param fileName 下载后的文件名称，如： QQ.exe
     * @param httpServletResponse
     * @return void
     *
     *
     * Accept-Encoding值
     *      gzip　　表明实体采用GNU zip编码
     *      compress 表明实体采用Unix的文件压缩程序
     *      deflate　　表明实体是用zlib的格式压缩的
     *      identity　　表明没有对实体进行编码。当没有Content-Encoding header时， 就默认为这种情况
     */
    public  static void download(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, String fileName, HttpServletResponse httpServletResponse) throws IOException {
        download(okHttpClient, url, paramsMap, fileName, null, httpServletResponse);
    }

    /**
     * 下载文件
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param paramsMap 请求参数
     * @param fileName 下载后的文件名称，如： QQ.exe
     * @param httpServletResponse
     * @return void
     *
     *
     * Accept-Encoding值
     *      gzip　　表明实体采用GNU zip编码
     *      compress 表明实体采用Unix的文件压缩程序
     *      deflate　　表明实体是用zlib的格式压缩的
     *      identity　　表明没有对实体进行编码。当没有Content-Encoding header时， 就默认为这种情况
     */
    public  static void download(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, String fileName, Map<String, String> headersMap, HttpServletResponse httpServletResponse) throws IOException {

        //拼接url和请求参数
        String toUrl = urlJoint(url, paramsMap);

        LOGGER.debug("------请求:" + toUrl);

        Request.Builder requestBuilder = new Request.Builder();
        // 请求后关闭链接
        requestBuilder.addHeader("Connection", "close");
        requestBuilder.url(toUrl);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        final Request request = requestBuilder.build();

        // execute 同步请求
        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            InputStream inputStream = null;
            ServletOutputStream outputStream = null;
            try {

                // 得到输入流 inputStream
                inputStream = response.body().byteStream();

                // 设置文件类型
                httpServletResponse.setContentType ("applicatoin/octet-stream");
                // 设置文件名
                httpServletResponse.setHeader ("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

                outputStream = httpServletResponse.getOutputStream ();
                byte[] buffer = new byte[256 * 1024];
                int len;
                while((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();

            }catch (IOException e){
                throw e;
            }finally {
                IoUtil.close(inputStream);
                IoUtil.close(outputStream);
            }
        }else{
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("文件下载失败");
        }
    }

    /**
     * 预览文件
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param paramsMap 请求参数
     * @param fileName 下载后的文件名称，如： QQ.exe
     * @param httpServletResponse
     * @return void
     *
     *
     * Accept-Encoding值
     *      gzip　　表明实体采用GNU zip编码
     *      compress 表明实体采用Unix的文件压缩程序
     *      deflate　　表明实体是用zlib的格式压缩的
     *      identity　　表明没有对实体进行编码。当没有Content-Encoding header时， 就默认为这种情况
     */
    public  static void preview(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, String fileName, HttpServletResponse httpServletResponse) throws IOException {
        preview(okHttpClient, url, paramsMap, fileName, null, httpServletResponse);
    }

    /**
     * 预览文件
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param paramsMap 请求参数
     * @param fileName 下载后的文件名称，如： QQ.exe
     * @param httpServletResponse
     * @return void
     *
     *
     * Accept-Encoding值
     *      gzip　　表明实体采用GNU zip编码
     *      compress 表明实体采用Unix的文件压缩程序
     *      deflate　　表明实体是用zlib的格式压缩的
     *      identity　　表明没有对实体进行编码。当没有Content-Encoding header时， 就默认为这种情况
     */
    public  static void preview(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, String fileName, Map<String, String> headersMap, HttpServletResponse httpServletResponse) throws IOException {

        //拼接url和请求参数
        String toUrl = urlJoint(url, paramsMap);

        LOGGER.debug("------请求:" + toUrl);

        Request.Builder requestBuilder = new Request.Builder();
        // 请求后关闭链接
        requestBuilder.addHeader("Connection", "close");
        requestBuilder.url(toUrl);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        final Request request = requestBuilder.build();

        // execute 同步请求
        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            InputStream inputStream = null;
            ServletOutputStream outputStream = null;
            try {

                // 得到输入流 inputStream
                inputStream = response.body().byteStream();

                String mimeType = FileUtil.getMimeType(fileName);
                if(null == mimeType){
                    // 设置文件类型
                    httpServletResponse.setContentType ("applicatoin/octet-stream");
                    // 设置文件名
                    httpServletResponse.setHeader ("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

                }else {
                    // 设置文件类型
                    httpServletResponse.setContentType (mimeType);
                    // 设置文件名
                    httpServletResponse.setHeader("Content-Disposition", "inline; filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
                }

                outputStream = httpServletResponse.getOutputStream ();
                byte[] buffer = new byte[256 * 1024];
                int len;
                while((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();

            }catch (IOException e){
                throw e;
            }finally {
                IoUtil.close(inputStream);
                IoUtil.close(outputStream);
            }
        }else{
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("文件预览失败");
        }
    }

    /**
     * 下载文件 json
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param json json请求参数
     * @param fileName 下载后的文件名称，如： QQ.exe
     * @param httpServletResponse
     * @return void
     *
     *
     * Accept-Encoding值
     *      gzip　　表明实体采用GNU zip编码
     *      compress 表明实体采用Unix的文件压缩程序
     *      deflate　　表明实体是用zlib的格式压缩的
     *      identity　　表明没有对实体进行编码。当没有Content-Encoding header时， 就默认为这种情况
     */
    public  static void download(OkHttpClient okHttpClient, String url, String json, String fileName, Map<String, String> headersMap, HttpServletResponse httpServletResponse) throws IOException {

        LOGGER.debug("------请求:" + url);

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=UTF-8"));

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 请求后关闭链接
        requestBuilder.addHeader("Connection", "close");

        // 创建一个Request
        final Request request = requestBuilder.build();

        // execute 同步请求
        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            InputStream inputStream = null;
            ServletOutputStream outputStream = null;
            try {

                // 得到输入流 inputStream
                inputStream = response.body().byteStream();
                // 设置文件类型
                httpServletResponse.setContentType ("applicatoin/octet-stream");
                // 设置文件名
                httpServletResponse.setHeader ("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

                outputStream = httpServletResponse.getOutputStream ();
                byte[] buffer = new byte[256 * 1024];
                int len;
                while((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();

            }catch (IOException e){
                throw e;
            }finally {
                IoUtil.close(inputStream);
                IoUtil.close(outputStream);
            }
        }else{
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("文件下载失败");
        }
    }


    /**
     * 预览文件 json
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param json json请求参数
     * @param fileName 下载后的文件名称，如： QQ.exe
     * @param httpServletResponse
     * @return void
     *
     *
     * Accept-Encoding值
     *      gzip　　表明实体采用GNU zip编码
     *      compress 表明实体采用Unix的文件压缩程序
     *      deflate　　表明实体是用zlib的格式压缩的
     *      identity　　表明没有对实体进行编码。当没有Content-Encoding header时， 就默认为这种情况
     */
    public  static void preview(OkHttpClient okHttpClient, String url, String json, String fileName, Map<String, String> headersMap, HttpServletResponse httpServletResponse) throws IOException {

        LOGGER.debug("------请求:" + url);

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=UTF-8"));

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 请求后关闭链接
        requestBuilder.addHeader("Connection", "close");

        // 创建一个Request
        final Request request = requestBuilder.build();

        // execute 同步请求
        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            InputStream inputStream = null;
            ServletOutputStream outputStream = null;
            try {

                // 得到输入流 inputStream
                inputStream = response.body().byteStream();

                String mimeType = FileUtil.getMimeType(fileName);
                if(null == mimeType){
                    // 设置文件类型
                    httpServletResponse.setContentType ("applicatoin/octet-stream");
                    // 设置文件名
                    httpServletResponse.setHeader ("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

                }else {
                    // 设置文件类型
                    httpServletResponse.setContentType (mimeType);
                    // 设置文件名
                    httpServletResponse.setHeader("Content-Disposition", "inline; filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
                }

                outputStream = httpServletResponse.getOutputStream ();
                byte[] buffer = new byte[256 * 1024];
                int len;
                while((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();

            }catch (IOException e){
                throw e;
            }finally {
                IoUtil.close(inputStream);
                IoUtil.close(outputStream);
            }
        }else{
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("文件预览失败");
        }
    }

    /**
     * 下载文件
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param paramsMap 请求参数
     * @return InputStream
     */
    public  static InputStream download(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap) throws IOException {
        return download(okHttpClient, url, paramsMap, null);
    }

    /**
     * 下载文件
     * @author 杨攀
     * @date 2021/12/24 9:48
     * @param okHttpClient
     * @param url 下载地址
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @return InputStream
     */
    public  static InputStream download(OkHttpClient okHttpClient, String url, Map<String, String> paramsMap, Map<String, String> headersMap) throws IOException {

        //拼接url和请求参数
        String toUrl = urlJoint(url, paramsMap);

        LOGGER.debug("------请求:" + toUrl);

        Request.Builder requestBuilder = new Request.Builder();
        // 为了获取下载文件的长度，指定文件的不需要对实体进行编码
        //requestBuilder.addHeader("Accept-Encoding", "identity")
        // 请求后关闭链接
        requestBuilder.addHeader("Connection", "close");
        requestBuilder.url(toUrl);

        // 添加头信息
        requestAddHeader(headersMap, requestBuilder);

        // 创建一个Request
        final Request request = requestBuilder.build();

        // execute 同步请求
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            // 得到输入流 inputStream
            return  response.body().byteStream();
        }
        return null;
    }

    /**
     * 下载文件
     * @author 杨攀
     * @date 2022/1/24 17:22
     * @param inputStream
     * @param fileName
     * @param response
     * @return void
     */
    public  static void download(InputStream inputStream, String fileName, HttpServletResponse response) {

        // 数据写会前端
        OutputStream outputStream = null;
        try {
            // 设置文件类型
            response.setContentType ("applicatoin/octet-stream");
            // 设置文件名
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

            outputStream = response.getOutputStream();
            byte[] buffer = new byte[256 * 1024];
            int point;
            while (-1 != (point = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            LOGGER.error("附件下载失败！失败原因:{}", e);
            throw new RuntimeException("附件下载失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
    }


    /**
     * 预览文件
     * @author 杨攀
     * @date 2022/1/24 17:22
     * @param inputStream
     * @param fileName
     * @param response
     * @return void
     */
    public  static void preview(InputStream inputStream, String fileName, HttpServletResponse response) {

        // 数据写会前端
        OutputStream outputStream = null;
        try {
            // 设置文件类型
            String mimeType = FileUtil.getMimeType(fileName);
            if(null == mimeType){
                // 设置文件类型
                response.setContentType ("applicatoin/octet-stream");
                // 设置文件名
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

            }else {
                // 设置文件类型
                response.setContentType (mimeType);
                // 设置文件名
                response.setHeader("Content-Disposition", "inline; filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
            }

            outputStream = response.getOutputStream();
            byte[] buffer = new byte[256 * 1024];
            int point;
            while (-1 != (point = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            LOGGER.error("附件预览失败！失败原因", e);
            throw new RuntimeException("附件预览失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
    }
}
