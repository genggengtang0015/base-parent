package org.zxs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 依据文件头获取经纬度网格信息，传入的经纬度按此网格信息取近似值
 *
 */
public class LatitudeLongitudeUtil {
	private final static Log log = LogFactory.getLog(LatitudeLongitudeUtil.class);
	
	
	 /** 
     * 读取文件，获取经纬度的网格信息 
	 * @throws Exception 
     */  
    public static Map<String, Double> readFileByLines(String fileName) throws Exception {  
    		Map<String, Double> latLonGridMap = new HashMap<>();
            File filePath=new File(fileName);
            boolean flag = filePath.exists();
            if(flag){
            	BufferedReader reader = null;  
            	try { 
		            InputStreamReader ips =new InputStreamReader(new FileInputStream(filePath), "GBK"); //gb2312
		            reader =new BufferedReader(ips);
		            String tempString = null;  
		            StringBuilder information = new StringBuilder("");
		            int line = 1;  
		            	// 一次读入一行，直到读入null为文件结束  
		            while ((tempString = reader.readLine()) != null) {  
		            		// 显示行号  
		            	if(line==5){
		            		information.append(tempString);
		            			break;
		            	}
		            	line++; 
		            }  
		            String gridInformation = information.toString();
		            reader.close(); 
		            	
		            String[] temps = gridInformation.split(" ");
		            String longitudeInterval = "0"; //经度格距
		            String latitudeInterval ="0"; //纬度格距
		            String startLongitude = "0"; //起始经度
		            String endLongitude = "0"; //终止经度
		            	
		            String startLatitude = "0"; //起始纬度
		            String endLatitude = "0"; //终止纬度
		            	
		            if(null !=temps && temps.length==8){
		            		longitudeInterval = temps[0];
		            		latitudeInterval = temps[1]; //纬度格距
		            		startLongitude = temps[2];
		            		endLongitude = temps[3];
		            		startLatitude = temps[4];
		            		endLatitude = temps[5];
		            }
		            	
		            double longitudeInt = Double.parseDouble(longitudeInterval);
		            double latitudeIn = Double.parseDouble(latitudeInterval);
		            double startLon = Double.parseDouble(startLongitude);
		            double endLon = Double.parseDouble(endLongitude);
		            double startLat = Double.parseDouble(startLatitude);
		            double endLat = Double.parseDouble(endLatitude);
		           
		            latLonGridMap.put("longitudeInterval", longitudeInt);
		            latLonGridMap.put("latitudeInterval", latitudeIn );
		            latLonGridMap.put("startLongitude", startLon );
		            latLonGridMap.put("endLongitude", endLon );
		            latLonGridMap.put("startLatitude", startLat );
		            latLonGridMap.put("endLatitude", endLat );            
		            	
		        }
            	catch (Exception e) {  
		        	log.info("readFileByLines()出错："+e.getMessage());
		        	throw e;
		        } finally {  
		            if (reader != null) {  
		                try {  
		                    reader.close();  
		                } catch (IOException e1) { 
		                	throw e1;
		                }  
		            }  
		        }  
		  }else{
		        log.info("文件不存在："+fileName);
		  }
        return latLonGridMap;
    }  
	
	
	
	/**
	 * 依据网格信息，经纬度在网格中的近似值
	 * @throws Exception 
	 */

	public static Map<String, Double> latLonApproximate( String filePath, double latitude, double longitude) throws Exception{
		Map<String, Double> latLonMap = new HashMap<>();
		Map<String, Double> latlonInitial = LatitudeLongitudeUtil.readFileByLines(filePath);
		
		double longitudeInterval = 1d; //经度格距
		double latitudeInterval =1d; //纬度格距
		double startLongitude = 0d; //起始经度
		double endLongitude = 0d; //终止经度
         	
		double startLatitude = 0d; //起始纬度
		double endLatitude = 0d; //终止纬度
		if(null!=latlonInitial && !latlonInitial.isEmpty()){
			 longitudeInterval = latlonInitial.get("longitudeInterval"); //经度格距
			 latitudeInterval = latlonInitial.get("latitudeInterval"); //纬度格距   ----- 间隔
			 startLongitude =  latlonInitial.get("startLongitude");  //起始经度
			 endLongitude = latlonInitial.get("endLongitude"); 		//终止经度
			 startLatitude =  latlonInitial.get("startLatitude");  //起始纬度    
			 endLatitude = latlonInitial.get("endLatitude"); //终止纬度        
			 
			 //纬度计算:  startLatitude ~ endLatitude 递减
			 BigDecimal latGrid =BigDecimal.valueOf(latitudeInterval ).abs() ; //1 小格
			 BigDecimal latHalfGrid = latGrid.multiply(BigDecimal.valueOf(0.5)); //半格 
			 if (startLatitude<latitude || endLatitude>latitude ) {
				 log.info("输入的纬度值超过范围,取值为0.0。LatitudeLongitudeUtil.latLonApproximate()读取文件里的纬度范围是：起始纬度="+startLatitude+", 终止纬度="+endLatitude+"输入纬度="+latitude);
				 latLonMap.put("latitude", 0D);
			 }else{
				 BigDecimal bgLat = new BigDecimal(latitude).abs();  //原值纬度
				 BigDecimal[]  wholeRemainderNum = bgLat.divideAndRemainder(latGrid);  //取整数，余数
				 BigDecimal wholeNum = wholeRemainderNum[0]; //取整数（格子数）
				 BigDecimal remainderNum = wholeRemainderNum[1]; //余数
				// remainderNum = remainderNum.abs();
				 double lat = 0d;
				 if (remainderNum.compareTo(BigDecimal.valueOf(0))==1 && remainderNum.compareTo(latHalfGrid)==-1 ) {  //0<x<半格
					 bgLat = wholeNum.multiply(latGrid);
					 lat = bgLat.doubleValue();
				 }else if (remainderNum.compareTo(latHalfGrid)==0 || remainderNum.compareTo(BigDecimal.valueOf(0))==0) {  //整格或半格
					 lat  = bgLat.doubleValue();
				 }else {
					 wholeNum = wholeNum.add(BigDecimal.valueOf(1));
					 bgLat = wholeNum.multiply(latGrid);
					 lat = bgLat.doubleValue();
				 }
				 if(latitude<0){
					 lat = lat*(-1d);
				 }
				 latitude = lat;
				 latLonMap.put("latitude", latitude);
			 }
				 
				 
				// 经度 
				 
				 BigDecimal lonGrid =BigDecimal.valueOf(longitudeInterval ).abs() ; //1 小格  纬度(绝对值）=0.125
				 BigDecimal lonHalfGrid = lonGrid.multiply(BigDecimal.valueOf(0.5)); //半格 =0.0625
				 
				 if(longitude<startLongitude || longitude>endLongitude){
					 log.info("输入的经度值超过范围,取值为0.0。LatitudeLongitudeUtil.latLonApproximate()读取文件里的纬度范围是：起始经度="+startLongitude+", 终止经度="+endLongitude+"输入经度="+longitude);
					 latLonMap.put("longitude", 0D);
				 } else{
					 BigDecimal bgLon = BigDecimal.valueOf(longitude);
					 BigDecimal[] wholeRemainderNum = bgLon.divideAndRemainder(lonGrid);
					 BigDecimal lonWholeNum = wholeRemainderNum[0];  //取整数格(格子数）
					 BigDecimal lonRemainderNum = wholeRemainderNum[1]; //取余数
					 if(lonRemainderNum.compareTo(BigDecimal.valueOf(0))==1 && lonRemainderNum.compareTo(lonHalfGrid)==-1 ){
						 bgLon = lonWholeNum.multiply(lonGrid);
						 longitude = bgLon.doubleValue();
					 }else if(lonRemainderNum.compareTo(BigDecimal.valueOf(0))==0 || lonRemainderNum.compareTo(lonHalfGrid)==0){
						 longitude = bgLon.doubleValue();
					 }else{
						 lonWholeNum = lonWholeNum.add(BigDecimal.valueOf(1));
						 bgLon = lonWholeNum.multiply(lonGrid);
						 longitude = bgLon.doubleValue();
					 }
					 latLonMap.put("longitude", longitude);
				 }
		}
		return latLonMap;
	}


}
