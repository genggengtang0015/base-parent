package org.zxs.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CommonUtil  {
	private final static Log log = LogFactory.getLog(CommonUtil.class);
	
	/**
	 * 通过HTTP请求获取JSON数据信息
	 */
	 public static JSONArray httpsRequest(String jsapiUrl) {
//		 JSON.parseObject(text)
		// TODO Auto-generated method stub
		 log.info("获取HTTP请求数据：" + jsapiUrl);
		 String result = "";
	        BufferedReader in = null;
	        try {
	            URL realUrl = new URL(jsapiUrl);
	            // 打开和URL之间的链接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            connection.setConnectTimeout(120000); // 设置2分钟超时
	            // 建立实际的链接
	            connection.connect();
	            // 获取响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            
	            // 定义 BufferedReader输入流来读取URL的响相应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	            JSONArray jsonArray =JSONArray.parseArray(result);
				return jsonArray;
	        } catch (Exception e) {
//	            System.out.println("发送GET请求出现异常" + e);
	            log.error(e.getMessage(), e);
	            return new JSONArray();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	            	log.error(e2.getMessage(), e2);
	            }
	        }
	        
	        
	        
	        
	}
	 
	 /**
		 * 通过HTTP请求获取JSON字符串, 且超时设置10分钟
	 * @throws IOException 
		 */
	 public static String doGetUrl(String jsapiUrl) throws IOException {
			return doGetUrl(jsapiUrl, 600000);
	}
	 
	public static String byteToHex(final byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash)
	        {
	            formatter.format("%02x", b);
	        }
	        String result = formatter.toString();
	        formatter.close();
	        return result;
	    }

	/**
	 * 通过HTTP请求获取JSON字符串
	 * @throws IOException 
	 */
	 public static String doGetUrl(String jsapiUrl, int timeout) throws IOException {
//		 JSON.parseObject(text)
		// TODO Auto-generated method stub
		 String result = "";
	        BufferedReader in = null;
	        try {
	            URL realUrl = new URL(jsapiUrl);
	            // 打开和URL之间的链接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            
	            // 设置超时
	            connection.setConnectTimeout(timeout); 
	            connection.setReadTimeout(timeout);
	            // 建立实际的链接
	            connection.connect();
	            // 获取响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	            return result;
	        } 
	        // 使用finally块来关闭输入流
	        finally {
                if (in != null) {
                    in.close();
                }
	        }
	        
			
	}

	/**
	 * 返回String类型
	*/
	public static String stringHttpsRequest(String jsapiUrl) {
		// TODO Auto-generated method stub
		 String result = "";
	        BufferedReader in = null;
	        try {
	            URL realUrl = new URL(jsapiUrl);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            connection.setConnectTimeout(600000); // 设置10分钟超时
	            // 建立实际的链接
	            connection.connect();
	            // 获取有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常" + e);
	            log.error(e.getMessage(), e);
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	            	log.error(e2.getMessage(), e2);
	            }
	        }
	        
	       
	        
		return result;
	}
	
	/**
	 * 根据url、编码方式获取页面内容,90秒超时
	 * 返回String类型
	 * @throws IOException 
	*/
	public static String getHttpResponseWithCharset(String jsapiUrl, String charSet) throws IOException {
		return getHttpResponseWithCharset(jsapiUrl, charSet, 90000);
	}
	
	/**
	 * 根据url、编码方式获取页面内容
	 * 返回String类型
	 * @throws IOException 
	*/
	public static String getHttpResponseWithCharset(String jsapiUrl, String charSet, int timeout) throws IOException {
		 String result = "";
	        BufferedReader in = null;
	        try {
	            URL realUrl = new URL(jsapiUrl);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            
	            // 设置超时
	            connection.setConnectTimeout(timeout); 
	            connection.setReadTimeout(timeout);
	            // 建立实际的链接
	            connection.connect();
	            // 获取有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream(),charSet));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line+"\r\n";
	            }
	        } 
	        // 使用finally块来关闭输入流
	        finally {
                if (in != null) {
                    in.close();
                }
	            
	        }
	        
	       
	        
		return result;
	}
	
	/**
	 * 根据url、编码方式获取页面内容
	 * 返回String类型
	 * @throws IOException 
	*/
	public static InputStream getHttpStream(String jsapiUrl) throws IOException {
        URL realUrl = new URL(jsapiUrl);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的链接
        connection.connect();
        return connection.getInputStream();
	}
	
	private final static int BUFFER_SIZE = 4096; 
	/**
	 * inputstream转byte数组
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static final byte[] input2byte(InputStream inStream)  
            throws IOException {  
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[BUFFER_SIZE];  
        int rc = 0;  
        while ((rc = inStream.read(buff, 0, BUFFER_SIZE)) > 0) {  
            swapStream.write(buff, 0, rc);  
        }  
        byte[] in2b = swapStream.toByteArray();  
        return in2b;  
    } 
	
	public static final  String formMysqlDate(Date date){
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		  String strDate = sdf.format(date); 
		return strDate;
	}

	
	/** 
	 * @Description: 获取客户端IP地址   
	 * @param @return     
	 * @return String    
	 * @throws 
	 */  
	public static String getIpAddr(HttpServletRequest request) {   
	       String ip = request.getHeader("x-forwarded-for");   
	       if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("Proxy-Client-IP");   
	       }   
	       if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("WL-Proxy-Client-IP");   
	       }
	       if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("HTTP_CLIENT_IP");   
	       }
	       if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("HTTP_X_FORWARDED_FOR");   
	       }
	       
	       if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getRemoteAddr();   
	           if(ip.equals("127.0.0.1")){     
	               //根据网卡取本机配置的IP     
	               InetAddress inet=null;     
	               try {     
	                   inet = InetAddress.getLocalHost();     
	               } catch (UnknownHostException e) {     
	                   log.error(e.getMessage(), e);   
	               }     
	               ip= inet.getHostAddress();     
	           }  
	       }   
	       // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
	       if(ip != null && ip.length() > 15){    
	           if(ip.indexOf(",")>0){     
	               ip = ip.substring(0,ip.indexOf(","));     
	           }     
	       }     
	       return ip;   
	}  
	
	/**
	 * 将异常信息转化成string
	 * @param e
	 * @return
	 */
	public static String exception2String(Throwable e){
		StringWriter sw = new StringWriter();   
        PrintWriter pw = new PrintWriter(sw, true);   
        e.printStackTrace(pw);   
        pw.flush();   
        sw.flush();   
        return sw.toString(); 
	}
	
	/**
	 * 将父类属性值赋予子类
	 * @param father
	 * @param child
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static<F, C extends F> void fatherToChild (F father, C child) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{  
        Class<? extends Object> fatherClass = father.getClass();  
        Field ff[] = fatherClass.getDeclaredFields();  
        for(int i=0;i<ff.length;i++){  
            Field f=ff[i];//取出每一个属性，如deleteDate  
            Method m = fatherClass.getMethod("get"+upperHeadChar(f.getName()));//方法getDeleteDate  
                Object obj=m.invoke(father);//取出属性值              
                f.set(child,obj);
        }  
    }  
    /** 
    * 首字母大写，in:deleteDate，out:DeleteDate 
    */  
    private static String upperHeadChar(String in){  
        String head=in.substring(0,1);  
        String out=head.toUpperCase()+in.substring(1,in.length());  
        return out;  
    } 
    
    /** 
     * 获取当前记录数 
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度 
     *  
     * @param date 
     * @return 
     */  
    public static int getSeason(Date date) {  
        int season = 0;  
  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        int month = c.get(Calendar.MONTH);  
        switch (month) {  
        case Calendar.JANUARY:  
        case Calendar.FEBRUARY:  
        case Calendar.MARCH:  
            season = 1;  
            break;  
        case Calendar.APRIL:  
        case Calendar.MAY:  
        case Calendar.JUNE:  
            season = 2;  
            break;  
        case Calendar.JULY:  
        case Calendar.AUGUST:  
        case Calendar.SEPTEMBER:  
            season = 3;  
            break;  
        case Calendar.OCTOBER:  
        case Calendar.NOVEMBER:  
        case Calendar.DECEMBER:  
            season = 4;  
            break;  
        default:  
            break;  
        }  
        return season;  
    }  
	
//	/**
//	 * 子类复制父类属性值
//	 * @param bo
//	 * @param so
//	 * @throws IllegalAccessException
//	 */
//	public static <B, S extends B> void copy(B bo, S so) throws IllegalAccessException {
//        try {
//            Class bc = bo.getClass();
//            if (bo == null || so == null) {
//                return;
//            }
//            DeserializeBeanInfo deserializeBeanInfo = DeserializeBeanInfo.computeSetters(so.getClass());
//            List<FieldInfo> getters = TypeUtils.computeGetters(bo.getClass(), null);
//            List<FieldInfo> setters = deserializeBeanInfo.getFieldList();
//            Object v;
//            FieldInfo getterfield;
//            FieldInfo setterfidld;
//            for (int j = 0; j < getters.size(); j++) {
//                getterfield = getters.get(j);
//                for (int i = 0; i < setters.size(); i++) {
//                    setterfidld = setters.get(i);
//                    if (setterfidld.getName().compareTo(getterfield.getName()) == 0) {
//                        v = getterfield.getMethod().invoke(bo);
//                        setterfidld.getMethod().invoke(so, v);
//                        break;
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    
    /**
     * MD5加密
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(s.getBytes("utf-8"));
        return byteToHex(bytes);
    }
}

