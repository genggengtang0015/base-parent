package org.zxs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;

public final class UploadQiniu {
	private final static Log log = LogFactory.getLog(UploadQiniu.class);

	// 设置好账号的ACCESS_KEY和SECRET_KEY
//	private static final String ACCESS_KEY = "UXFS46gwTF1uoFLZ6H-4Mbh1e3Z126OWiddZQ-Dl";
//	private static final String SECRET_KEY = "M-dAUv5MzBURRSTeAsx1zEg_xeEROSvcOH8R2qWp";
	
	private static final String ACCESS_KEY = "E_wRniP2hBlyA4PmFVUA1JT0Oi2p6w7cSLbo1Pci";
	private static final String SECRET_KEY = "BNLEByt8HEBxZT54hPx7iFaPPcm88AoeKEb1kyCu";
	
	// 要上传的空间
//	private static final String BUCKET_NAME = "origin";
	
	private static final String BUCKET_NAME = "home";
	// private static final String QINIU_HOME_URL =
	// "//ogbcvxavq.bkt.clouddn.com/";
	private static final String QINIU_HOME_URL = "//qiniu.uulead.com/";

	private static final String DEFAULT_CALLBACK = "http://139.224.228.65:8506/pb-web/video-notify";
	
	//http域名
//	private static final String HTTP_QINIU_HOME_URL = "http://cdn.jyblue.com/";
	private static final String HTTP_QINIU_HOME_URL = "http://p53wzuaxq.bkt.clouddn.com/";
	
	//上传http域名空间
//	private static final String HTTP_BUCKET_NAME = "jiuyu";
	
	private static final String HTTP_BUCKET_NAME = "home";

	// 上传对象
	// private static UploadManager uploadManager = null;

	// 策略
	// private static String policy = null;

	// static{
	// if(policy == null){
	// Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// policy = auth.uploadToken(BUCKET_NAME); //单上传，使用默认策略，只要设置上传的空间名就可以
	// }
	//
	// if(uploadManager == null){
	// Zone z = Zone.autoZone();
	// Configuration c = new Configuration(z);
	// uploadManager = new UploadManager(c);
	// }
	//
	// }

	/**
	 * 获取token
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getUpToken() {
//		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//		String upToken = auth.uploadToken(BUCKET_NAME);
//		return upToken;
		return getUpToken(false);
	}
	
	/**
	 * 获取token
	 * @param falg: true时，获取http域名空间的token， false时，获取https域名空间的token
	 * @return
	 */
	public static String getUpToken(boolean flag) {
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		String upToken = "";
		if(flag){
			upToken = auth.uploadToken(HTTP_BUCKET_NAME);  // 单上传，使用默认策略，只要设置上传的空间名就可以
		}else{
			upToken = auth.uploadToken(BUCKET_NAME);
		}
		return upToken;
	}
	

	/**
	 * 上传本地文件至七牛
	 * 
	 * @return
	 * @throws IOException
	 */
	private static String upload(String filePath, String fileName, String bucket, String baseUrl) throws QiniuException {
		log.info("开始上传文件[" + fileName + "]到七牛！");
		Zone z = Zone.autoZone();
		Configuration c = new Configuration(z);
		UploadManager uploadManager = new UploadManager(c);
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		String bucketName = auth.uploadToken(bucket);
		// 调用put方法上传
		Response res = uploadManager.put(filePath, fileName, bucketName);
		log.info("上传七牛返回的信息：" + res.bodyString());

		JSONObject resBodyJson = JSON.parseObject(res.bodyString());
		String url = baseUrl + resBodyJson.getString("key");
		log.info("成功上传文件[" + fileName + "]到七牛,文件地址为" + url + "！");
		return url;
	}

	/**
	 * 上传本地文件至七牛
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String upload(String filePath, String fileName) throws QiniuException {
		/*log.info("开始上传文件[" + fileName + "]到七牛！");
		Zone z = Zone.autoZone();
		Configuration c = new Configuration(z);
		UploadManager uploadManager = new UploadManager(c);
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		String policy = auth.uploadToken(BUCKET_NAME); // 单上传，使用默认策略，只要设置上传的空间名就可以

		// 调用put方法上传
		// Response res = uploadManager.put(filePath, fileName, policy);
		// log.info("上传七牛返回的信息：" + res.bodyString());
		//
		// JSONObject resBodyJson = JSON.parseObject(res.bodyString());
		// String url = QINIU_HOME_URL + resBodyJson.getString("key");
		// log.info("成功上传文件[" + fileName + "]到七牛,文件地址为" + url + "！");
		// return url;
		return upload(filePath, fileName, uploadManager, policy, QINIU_HOME_URL);*/
		return upload(filePath, fileName, BUCKET_NAME, QINIU_HOME_URL);
	}
	
	/**
	 * 上传本地文件到七牛（http域名）
	 * @param filePath
	 * @param fileName
	 * @param flag 为true时，上传到http域名；false时为https域名
	 * @return
	 * @throws QiniuException
	 */
	public static String upload(String filePath, String fileName, boolean flag) throws QiniuException {
//		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		if(flag){
			return upload(filePath, fileName, HTTP_BUCKET_NAME, HTTP_QINIU_HOME_URL);
		}
			
		return upload(filePath, fileName, BUCKET_NAME, QINIU_HOME_URL);
	}

	/**
	 * 上传线上流文件至七牛，即源文件来自其他网站
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String uploadFromWeb(String sourceUrl, String fileName) throws IOException {
		return uploadFromWeb(sourceUrl, fileName, false);
//		log.info("开始从" + sourceUrl + "上传文件[" + fileName + "]到七牛！");
//		return uploadImgQiniu(CommonUtil.getHttpStream(sourceUrl), fileName);
	}

	/**
	 * 上传流文件到七牛
	 * 
	 * @param inStream
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws QiniuException
	 */

	public static String uploadImgQiniu(InputStream inStream, String fileName) throws QiniuException, IOException {
		return uploadImgQiniu(inStream, fileName, DEFAULT_CALLBACK);
	}

	/**
	 * inStream 流文件至七牛，
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String uploadImgQiniu(InputStream inStream, String fileName, String persistentNotifyUrl)
			throws QiniuException, IOException {
		byte[] picDataArray = CommonUtil.input2byte(inStream);

		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		String policy = auth.uploadToken(BUCKET_NAME); // 单上传，使用默认策略，只要设置上传的空间名就可以
		Zone z = Zone.autoZone();
		Configuration c = new Configuration(z);
		UploadManager uploadManager = new UploadManager(c);

		// 调用put方法上传
		Response res = uploadManager.put(picDataArray, fileName, policy);
		log.info("上传七牛返回的信息：" + res.bodyString());

		JSONObject resBodyJson = JSON.parseObject(res.bodyString());
		String urlOrigin = QINIU_HOME_URL + resBodyJson.getString("key");
		log.info("成功上传文件[" + fileName + "]到七牛,文件地址为" + urlOrigin + "！");
		String lowercaseFile = fileName.toLowerCase();

		if (lowercaseFile.endsWith("mp4")) { // 对mp4文件进行转码
			String newTransName = videoTransCodec(fileName, persistentNotifyUrl);
			String urlTran = QINIU_HOME_URL + newTransName;
			log.info("文件[" + fileName + "]转码成功，新文件地址为" + urlTran + "！");
			return urlTran;
		}
		return urlOrigin;
	}
	
	/**
	 * 上传线上流文件至七牛，即源文件来自其他网站
	 * @param sourceUrl 其他网站
	 * @param fileName
	 * @param flag 为true时，上传到http域名； 为false时，上传到https域名
	 * @return
	 * @throws IOException
	 */
	public static String uploadFromWeb(String sourceUrl, String fileName, boolean flag) throws IOException {
		log.info("开始从" + sourceUrl + "上传文件[" + fileName + "]到七牛！");
		String policy = getUpToken(flag);
		if(flag){
			return uploadImgQiniux(sourceUrl,HTTP_QINIU_HOME_URL, policy,  fileName, DEFAULT_CALLBACK);
		}
		return uploadImgQiniux(sourceUrl,QINIU_HOME_URL, policy,  fileName, DEFAULT_CALLBACK);
		
	}
	
	/**
	 * 上传到七牛公共部门（网络资源）
	 * @param sourceUrl  
	 * @param homeUrl: 七牛域名
	 * @param bucketName:七牛存储空间
	 * @param fileName:保存的文件名
	 * @param persistentNotifyUrl:
	 * @return
	 * @throws QiniuException
	 * @throws IOException
	 */
	private static String uploadImgQiniux(String sourceUrl, String homeUrl, String policy, String fileName, String persistentNotifyUrl )throws QiniuException, IOException{
		InputStream inStream = CommonUtil.getHttpStream(sourceUrl);
		byte[] picDataArray = CommonUtil.input2byte(inStream);
		
		Zone z = Zone.autoZone();
		Configuration c = new Configuration(z);
		UploadManager uploadManager = new UploadManager(c);
		// 调用put方法上传
		Response res = uploadManager.put(picDataArray, fileName, policy);
		log.info("上传七牛返回的信息：" + res.bodyString());
		
		JSONObject resBodyJson = JSON.parseObject(res.bodyString());
		String urlOrigin = homeUrl + resBodyJson.getString("key");
		log.info("成功上传文件[" + fileName + "]到七牛,文件地址为" + urlOrigin + "！");
		String lowercaseFile = fileName.toLowerCase();

		if (lowercaseFile.endsWith("mp4")) { // 对mp4文件进行转码
			String newTransName = videoTransCodec(fileName, persistentNotifyUrl);
			String urlTran = homeUrl + newTransName;
			log.info("文件[" + fileName + "]转码成功，新文件地址为" + urlTran + "！");
			return urlTran;
		}
		return urlOrigin;
	}
	
	
	

	public static String getQiniuHomeUrl() {
		return QINIU_HOME_URL;
	}
	
	public static String getHttpQiniuHomeUrl() {
		return HTTP_QINIU_HOME_URL;
	}

	/**
	 * 获取七牛流文件 http 上传ftp fiel转换
	 * 
	 * @return
	 */
	public static InputStream inputstreamtofile(String hurl) {
		InputStream is = null;
		try {
			// 构造URL
			URL url = new URL(hurl);
			// 打开连接
			URLConnection con = url.openConnection();
			// 设置请求超时为5s
			con.setConnectTimeout(50 * 1000);
			// 输入流
			is = con.getInputStream();

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return is;
	}

	/**
	 * 视频转码，返回转码后文件名
	 * 
	 * @param originName
	 *            原始云空间文件名
	 * @param persistentNotifyUrl
	 *            七牛通知URL地址
	 * @throws QiniuException
	 */
	public static String videoTransCodec(String originName, String persistentNotifyUrl) throws QiniuException {
		// 数据处理指令，支持多个指令
		String saveName = "tran_" + originName;
		String saveMp4Entry = BUCKET_NAME + ":" + saveName;
		String encodeToNewName = UrlSafeBase64.encodeToString(saveMp4Entry);
		String avthumbMp4Fop = String.format(
				"avthumb/mp4/ab/160k/ar/44100/acodec/libfaac/r/30/vb/2400k/vcodec/libx264/s/1280x720/autoscale/1/strimeta/0|saveas/%s",
				encodeToNewName);
		// 将多个数据处理指令拼接起来
		String persistentOpfs = StringUtils.join(new String[] { avthumbMp4Fop }, ";");
		// 数据处理队列名称，必须
		String persistentPipeline = "avvod-pipeline";

		Zone z = Zone.zone0();
		Configuration c = new Configuration(z);
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		OperationManager operationMgr = new OperationManager(auth, c);

		// 执行转码和另存 操作
		String persistentId = operationMgr.pfop(BUCKET_NAME, originName, persistentOpfs, persistentPipeline,
				persistentNotifyUrl, true);
		log.info("转码完成，编号为" + persistentId + "，新文件名为" + saveName);
		return saveName;

	}
}
