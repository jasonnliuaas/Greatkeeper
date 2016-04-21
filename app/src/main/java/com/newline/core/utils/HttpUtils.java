package com.newline.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.newline.C;

@SuppressWarnings("deprecation")
public class HttpUtils {

    private static boolean isDebug = false;
    public static final String UTF8 = "utf-8";
    public static final String GBK = "gbk";
    public static final String GB2312 = "gb2312";
    public static final String ISO88591 = "ISO-8859-1";
    public static int READ_TIMEOUT = 10000;
    public static int CONNECT_TIMEOUT = 10000;

    /**
     * post请求数据
     * 
     * @param connectURL
     * @param param
     * @param charset
     * @return
     */
    public static String doPost(String connectURL, String param, String charset) {
        Log.d(C.TAG, "Post URL:" + connectURL);
        Log.d(C.TAG, "Post Param:" + param);
        ByteArrayOutputStream byteArrayOut = null;
        URL url = null;
        HttpURLConnection httpPost = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            url = new URL(connectURL);
            httpPost = (HttpURLConnection) url.openConnection();
            
            httpPost.setRequestMethod("POST");
            httpPost.setDoInput(true);
            httpPost.setDoOutput(true);
            httpPost.setUseCaches(false);
            httpPost.setConnectTimeout(CONNECT_TIMEOUT);
            httpPost.setReadTimeout(READ_TIMEOUT);
            httpPost.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpPost.connect();
            out = httpPost.getOutputStream();
            out.write(param.getBytes(charset));
            out.flush();
            in = httpPost.getInputStream();
            byteArrayOut = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int l = 0;
            while ((l = in.read(buf)) != -1) {
                byteArrayOut.write(buf, 0, l);
            }
            byte[] bytes = byteArrayOut.toByteArray();
            return new String(bytes, charset);
        } catch (Exception e) {
            if(!isDebug){
                Log.e(C.TAG, "Http post request error.", e);
            }else{
                e.printStackTrace();
            }
        } finally {
            close(byteArrayOut);
            close(out);
            close(in);
            if (httpPost != null) {
                httpPost.disconnect();
            }
        }
        return null;
    }
    
    public static String parseParam(HashMap<String, String> params){
        if(params == null || params.isEmpty()){
            return "";
        }
        
        StringBuffer paramBuf = new StringBuffer();
        for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            String value = params.get(key);
            paramBuf.append("&").append(key).append("=").append(value);
        }
        return paramBuf.substring(1);
    }

    
    /**
     * Get请求数据
     * 
     * @param connectURL
     * @param charset
     * @return
     */
    public static String doGet(String connectURL, String charset) {
        Log.d(C.TAG, "Get URL:" + connectURL);
        ByteArrayOutputStream byteArrayOut = null;
        URL url = null;
        HttpURLConnection httpGet = null;
        InputStream in = null;
        try {
            url = new URL(connectURL);
            httpGet = (HttpURLConnection) url.openConnection();
            httpGet.setConnectTimeout(CONNECT_TIMEOUT);
            httpGet.setReadTimeout(READ_TIMEOUT);
            httpGet.connect();
            in = httpGet.getInputStream();
            byteArrayOut = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int l = 0;
            while ((l = in.read(buf)) != -1) {
                byteArrayOut.write(buf, 0, l);
            }
            byte[] bytes = byteArrayOut.toByteArray();
            return new String(bytes, charset);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(C.TAG, "Http get request error, URL = " + connectURL, e);
        } finally {
            close(byteArrayOut);
            close(in);
            if (httpGet != null) {
                httpGet.disconnect();
            }
        }
        return null;
    }

    private static void close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (Exception e) {
            if(!isDebug){
                Log.e(C.TAG, "Close stream error.", e);
            }else{
                e.printStackTrace();
            }
        }
    }
    
    public static String deUnicode(String unicodeStr) {
        try {
            StringBuilder sb = new StringBuilder();
            int i = -1, pos = 0;

            while ((i = unicodeStr.indexOf("\\u", pos)) != -1) {
                sb.append(unicodeStr.substring(pos, i));
                if (i + 5 < unicodeStr.length()) {
                    pos = i + 6;
                    sb.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                }
            }
            sb.append(unicodeStr.substring(pos));
            return sb.toString();
        } catch (Exception e) {
            if(!isDebug){
                Log.e(C.TAG, "Decode unicode string error.", e);
            }else{
                e.printStackTrace();
            }
        }
        return unicodeStr;
    }
   
    
    public static String doPostWithBitmap(String connectURL, Map<String, String> params, String bitmapParamName, Bitmap bitmap){
        try {
            
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(connectURL);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            bitmap.compress(CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();

            if(params != null && !params.isEmpty()){
                for (String key : params.keySet()) {
                    entity.addPart(key, new StringBody(params.get(key)));
                }
            }

            entity.addPart(bitmapParamName, new ByteArrayBody(data, "temp.jpg"));

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse = reader.readLine();
            return sResponse;
        } catch (Exception e) {
            Log.e(C.TAG, "Http post with bitmap request error, URL = " + connectURL, e);
        }
        return null;
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        isDebug = true;
        String token = "51642e103a9c4069d49007ae18703834afe1e8ac";
        
        String url = "http://test.greatkeeper.com/?c=api&";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("a", "getLoading");
        params.put("v", "1.0");
        params.put("token", token);
        url += parseParam(params);
        
        String result = doGet(url, UTF8);
        System.out.println(deUnicode(result));
    }

}
