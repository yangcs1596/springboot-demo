package com.example.demo.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Copyright (c) 2011,　福建福诺移动通信技术有限公司 All rights reserved。
 * 文件名称：HttpRequestProxy.java 描 述：处理HTTP请求（集中处理GET提交出现乱码的情况） 注意：在请求前请先设置回传的编码
 *
 * @author Administrator
 * @version 1.0 修改记录： 修改时间： 修 改 人： 修改内容：
 * @date 2011-11-6 下午01:57:21
 */
public class HttpRequestProxy {
    /**
     * 连接超时
     */
    private static String connectTimeOut = "300000";
    /**
     * 读取数据超时
     */
    private static String readTimeOut = "10000";
    /**
     * 请求编码
     */
    private static String requestEncoding = "GBK";
    private static final Logger log = LoggerFactory
            .getLogger(HttpRequestProxy.class);

    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map parameters,
                               String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter
                    .hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                if (null != element.getValue()) {
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpRequestProxy.requestEncoding));
                }
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            // System.setProperty("sun.net.client.defaultConnectTimeout", String
            // .valueOf(HttpRequestProxy.connectTimeOut));//
            // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String
            // .valueOf(HttpRequestProxy.readTimeOut)); //
            // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(Integer
                    .parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            log.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    /**
     * <pre>
     * 发送不带参数的GET的HTTP请求
     * </pre>
     *
     * @param reqUrl HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");
            if (paramIndex > 0) {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1,
                        reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++) {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0) {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1,
                                string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value,
                                HttpRequestProxy.requestEncoding));
                        params.append("&");
                    }
                }
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            // System.setProperty("sun.net.client.defaultConnectTimeout", String
            // .valueOf(HttpRequestProxy.connectTimeOut));//
            // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String
            // .valueOf(HttpRequestProxy.readTimeOut)); //
            // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(Integer
                    .parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            log.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    @SuppressWarnings("unchecked")
    public static String doPost(String reqUrl, Map parameters,
                                String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter
                    .hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                if (null != element.getValue()) {
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpRequestProxy.requestEncoding));
                }
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(Integer
                    .parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            log.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }


    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    @SuppressWarnings("unchecked")
    public static String doPost(String reqUrl, Map parameters, String requestEncoding,
                                String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter
                    .hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                if (null != element.getValue()) {
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            requestEncoding));
                }
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(Integer
                    .parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            log.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }


    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map parameters,
                                String recvEncoding, Map headMap) {
        HttpURLConnection url_con = null;
        String responseContent = "";
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter
                    .hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                if (null != element.getValue()) {
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpRequestProxy.requestEncoding));
                }
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            Set set = headMap.entrySet();// 用接口实例接口
            Iterator iter = set.iterator();
            while (iter.hasNext()) {// 遍历二次,速度慢
                Entry entry = (Entry) iter.next();
                url_con.setRequestProperty((String) entry.getKey(), entry.getValue().toString());
                // // 因为指针判断下一个有没有值 iter.next是当前对象 但是 m.get(iter.next())是下一个值
            }
            // System.setProperty("sun.net.client.defaultConnectTimeout", String
            // .valueOf(HttpRequestProxy.connectTimeOut));//
            // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String
            // .valueOf(HttpRequestProxy.readTimeOut)); //
            // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(Integer
                    .parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            log.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent.trim();
    }

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    @SuppressWarnings("unchecked")
    public static String doPost(String reqUrl, String strXML, String Encoding) {
        String data = null;
        HttpURLConnection con = null;
        try {
            String readLine;
            URL dataUrl = new URL(reqUrl);
            con = (HttpURLConnection) dataUrl.openConnection();
            con.setRequestProperty("msgname", "JzptUserQuery");
            con.setRequestProperty("msgversion", "1.0.0");
            con.setRequestProperty("transactionid",
                    "9904000000000003721209241622330001001");
            con.setRequestProperty("sendareacode", "000001");
            con.setRequestProperty("sendaddress", "99040000");
            con.setRequestProperty("recvareacode", "000001");
            con.setRequestProperty("recvaddress", "99010000");
            con.setRequestProperty("Proxy-Connection", "Keep-Alive");
            con.setRequestProperty("content-type", "text/xml;charset="
                    + Encoding);
            // System.setProperty("sun.net.client.defaultConnectTimeout", String
            // .valueOf(HttpRequestProxy.connectTimeOut));//
            // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String
            // .valueOf(HttpRequestProxy.readTimeOut)); //
            // （单位：毫秒）jdk1.4换成这个,读操作超时
            con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            con.setReadTimeout(Integer.parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            // con.setRequestProperty("method", "POST"); // 设置提交方式
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream os = con.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(strXML.getBytes(Encoding));
            dos.flush();
            dos.close();
            StringBuffer responseBuffer = new StringBuffer();
            BufferedReader responseReader;
            // 处理响应流，必须与服务器响应流输出的编码一致
            responseReader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), Encoding));
            while ((readLine = responseReader.readLine()) != null) {
                responseBuffer.append(readLine).append("");
            }
            responseReader.close();
            data = responseBuffer.toString();
            con.disconnect();
        } catch (Exception ex) {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                log.error("Http代理 关闭连接失败", e);
            }
            data = null;
            log.error("网络故障" + ex.getMessage());
        }
        return data;
    }

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, String strXML, String Encoding,
                                Map headMap) {
        String data = null;
        HttpURLConnection con = null;
        try {
            String readLine;
            URL dataUrl = new URL(reqUrl);
            con = (HttpURLConnection) dataUrl.openConnection();
            con.setRequestProperty("Proxy-Connection", "Keep-Alive");
            con.setRequestProperty("content-type", "text/xml;charset="
                    + Encoding);
            // System.setProperty("sun.net.client.defaultConnectTimeout", String
            // .valueOf(HttpRequestProxy.connectTimeOut));//
            // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String
            // .valueOf(HttpRequestProxy.readTimeOut)); //
            // （单位：毫秒）jdk1.4换成这个,读操作超时
            con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            con.setReadTimeout(Integer.parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            Set set = headMap.entrySet();// 用接口实例接口
            Iterator iter = set.iterator();
            while (iter.hasNext()) {// 遍历二次,速度慢
                Entry entry = (Entry) iter.next();
                con.setRequestProperty((String) entry.getKey(), entry.getValue().toString());
                // // 因为指针判断下一个有没有值 iter.next是当前对象 但是 m.get(iter.next())是下一个值
            }
            con.setRequestProperty("method", "POST"); // 设置提交方式
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream os = con.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(strXML.getBytes(Encoding));
            dos.flush();
            dos.close();
            StringBuffer responseBuffer = new StringBuffer();
            BufferedReader responseReader;
            // 处理响应流，必须与服务器响应流输出的编码一致
            responseReader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), Encoding));
            while ((readLine = responseReader.readLine()) != null) {
                responseBuffer.append(readLine).append("");
            }
            responseReader.close();
            data = responseBuffer.toString();
            con.disconnect();
        } catch (Exception ex) {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                log.error("Http代理 关闭连接失败", e);
            }
            data = null;
            log.error("网络故障" + ex.getMessage());
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    public static void doDown(String reqUrl, Map parameters,
                              String recvEncoding, FileOutputStream out) {
        HttpURLConnection url_con = null;
        InputStream in = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter
                    .hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                if (null != element.getValue()) {
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpRequestProxy.requestEncoding));
                }
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            // System.setProperty("sun.net.client.defaultConnectTimeout", String
            // .valueOf(HttpRequestProxy.connectTimeOut));//
            // （单位：毫秒）jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", String
            // .valueOf(HttpRequestProxy.readTimeOut)); //
            // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(Integer
                    .parseInt(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(Integer
                    .parseInt(HttpRequestProxy.readTimeOut));// （单位：毫秒）jdk
            // 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            in = url_con.getInputStream();
            StreamUtils.copy(in, out);
        } catch (IOException e) {
            log.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
    }


    @SuppressWarnings("unchecked")
    public static String doPost(String reqUrl, Map parameters,
                                String recvEncoding, int connectTimeOut, int readTimeOut) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter
                    .hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                if (null != element.getValue()) {
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpRequestProxy.requestEncoding));
                }
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(connectTimeOut);// （单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(readTimeOut);// （单位：毫秒）jdk
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            log.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    /**
     * @return 连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static String getConnectTimeOut() {
        return HttpRequestProxy.connectTimeOut;
    }

    /**
     * @return 读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static String getReadTimeOut() {
        return HttpRequestProxy.readTimeOut;
    }

    /**
     * @return 请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static String getRequestEncoding() {
        return requestEncoding;
    }

    /**
     * @param connectTimeOut 连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static void setConnectTimeOut(String connectTimeOut) {
        HttpRequestProxy.connectTimeOut = connectTimeOut;
    }

    /**
     * @param readTimeOut 读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static void setReadTimeOut(String readTimeOut) {
        HttpRequestProxy.readTimeOut = readTimeOut;
    }

    /**
     * @param requestEncoding 请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static void setRequestEncoding(String requestEncoding) {
        HttpRequestProxy.requestEncoding = requestEncoding;
    }
}
