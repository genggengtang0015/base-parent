package org.zxs.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 访问奥震数据交换共享平台数据接口
 * @author tang
 *
 */
public class VisitAzUtils {
	private final static Log log = LogFactory.getLog(VisitAzUtils.class);
	
	/**
	 * 获取奥震数据交换与共享平台数据访问session
	 * @param host ip地址与端口号
	 * @param resourceId 奥震数据资源ID
	 * @param user2Dev 二次开发账号用户名
	 * @param pwd2Dev 二次开发账号密码
	 * @return
	 * @throws Exception
	 */
	public static String getAzSession(String host, String resourceId, String user2Dev, String pwd2Dev) throws Exception{
		Map<String, String> querys = new HashMap<>();
		querys.put("identifier", resourceId);
		Map<String, String> body = new HashMap<>();
		body.put("username", user2Dev);
		body.put("password", pwd2Dev);
		HttpResponse sessionResp = HttpUtils.doPost(host, "/developer-api/sessions", new HashMap<String, String>(), querys, JSON.toJSONString(body));
		String sessionRespBody = EntityUtils.toString(sessionResp.getEntity(), "utf-8");
		
		JSONObject sessionRespEntity = JSON.parseObject(sessionRespBody);
		if(null != sessionRespEntity && sessionRespEntity.containsKey("session")){
			return sessionRespEntity.getString("session");
		}
		return "";
	}
	

	/**
	 * 使用POST访问奥震查询类数据接口
	 * @param session
	 * @param host
	 * @param url
	 * @param tableName
	 * @param columnNames
	 * @param conditions
	 * @param pageMap
	 * @param orderBy
	 * @return
	 * @throws Exception
	 */
	public static String postSearchInteface(String session, String host, String url, String tableName, String[] columnNames, Object[][] conditions, Map<String,String> pageMap, Map<String,String> orderBy) throws Exception{
		Map<String, String> heads = new HashMap<>();
		heads.put("az-session", session);
		Map<String, Object> body = new HashMap<>();
		body.put("table", tableName);
		
		Map<String, String[]> columns = new HashMap<>();
		columns.put("columns", columnNames);
		body.put("values", columns);
		
		if(null != conditions && conditions.length > 0)
			body.put("conditions", conditions);
		
		if(null != pageMap && !pageMap.isEmpty())
			body.put("page", pageMap);
		
		if(null != orderBy && !orderBy.isEmpty())
			body.put("order_by", orderBy);
		
		String bodyStr = JSON.toJSONString(body);
		
		HttpResponse searchResp = HttpUtils.doPost(host, url, heads, new HashMap<String, String>(), bodyStr);
		String searchRespStr = EntityUtils.toString(searchResp.getEntity(), "utf-8");
		return searchRespStr;
	}
	
	public static String postSearchIntefaceGroupBy(String session, String host, String url, String tableName, String[] columnNames, Object[][] conditions,String[] groupBy, Map<String,String> pageMap, Map<String,String> orderBy) throws Exception{
		Map<String, String> heads = new HashMap<>();
		heads.put("az-session", session);
		Map<String, Object> body = new HashMap<>();
		body.put("table", tableName);
		
		Map<String, String[]> columns = new HashMap<>();
		columns.put("columns", columnNames);
		body.put("values", columns);
		
		if(null != conditions && conditions.length > 0)
			body.put("conditions", conditions);
		
		if(null!=groupBy&&groupBy.length>0)
			body.put("group_by", groupBy);
		
		if(null != pageMap && !pageMap.isEmpty())
			body.put("page", pageMap);
		
		if(null != orderBy && !orderBy.isEmpty())
			body.put("order_by", orderBy);
		
		String bodyStr = JSON.toJSONString(body);
		
		HttpResponse searchResp = HttpUtils.doPost(host, url, heads, new HashMap<String, String>(), bodyStr);
		String searchRespStr = EntityUtils.toString(searchResp.getEntity(), "utf-8");
		return searchRespStr;
	}
	
	/**
	 * 使用POST访问奥震查询类数据接口
	 * @param session
	 * @param host
	 * @param url
	 * @param tableName
	 * @param columnNames
	 * @param conditions
	 * @param pageMap
	 * @param orderBy
	 * @return
	 * @throws Exception
	 */
	public static String postDefaultSearchInteface(String session, String host, String url, String tableName, String[] columnNames) throws Exception{
		Map<String,String> page = new HashMap<>();
		page.put("offset", "0");
		page.put("limit", "5");
		
		return postSearchInteface(session, host, url, tableName, columnNames, null, page, null);
	}

}
