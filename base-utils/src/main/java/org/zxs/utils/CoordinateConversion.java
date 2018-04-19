//package org.zxs.utils;
//
//import org.zxs.base.model.Point;
//
//public class CoordinateConversion {
//	private static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0;
//
//	private static final double PI = 3.14159265358979324;
//	// private static final double A = 6378245.0; // 54年北京坐标系参数
//	// private static final double A = 6378140.0; // 80年西安坐标系参数
//	private static final double A = 6378137; // WGS-84
//	// private static final double EE = 0.00669342162296594323;
//	private static final double F = 298.257223563;
//	
//	/**
//	 * 将平面坐标直接转化成百度坐标
//	 * @param x X坐标
//	 * @param y Y坐标
//	 */
//	public static Point gauss2Bd(double x, double y){
//		double[] d = gaussToBL(x, y);
//		Point p1 = wgs_gcj_encrypts(d[1], d[0]);
//		Point p2 = google_bd_encrypt(p1.getLat(), p1.getLng());
//		return p2;
//	}
//	
//
//	/**
//	 * gg_lat 纬度 gg_lon 经度 GCJ-02转换BD-09 Google地图经纬度转百度地图经纬度
//	 */
//	public static Point google_bd_encrypt(double gg_lat, double gg_lon) {
//		Point point = new Point();
//		double x = gg_lon, y = gg_lat;
//		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
//		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
//		double bd_lon = z * Math.cos(theta) + 0.0065;
//		double bd_lat = z * Math.sin(theta) + 0.006;
//		point.setLat(bd_lat);
//		point.setLng(bd_lon);
//		return point;
//	}
//
//	/**
//	 * wgLat 纬度 wgLon 经度 BD-09转换GCJ-02 百度转google
//	 */
//	public static Point bd_google_encrypt(double bd_lat, double bd_lon) {
//		Point point = new Point();
//		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
//		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
//		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
//		double gg_lon = z * Math.cos(theta);
//		double gg_lat = z * Math.sin(theta);
//		point.setLat(gg_lat);
//		point.setLng(gg_lon);
//		return point;
//	}
//
//	/**
//	 * wgLat 纬度 wgLon 经度 WGS-84 到 GCJ-02 的转换（即 GPS 加偏）
//	 */
//	public static Point wgs_gcj_encrypts(double wgLat, double wgLon) {
//		Point point = new Point();
//		if (outOfChina(wgLat, wgLon)) {
//			point.setLat(wgLat);
//			point.setLng(wgLon);
//			return point;
//		}
//		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
//		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
//		double radLat = wgLat / 180.0 * PI;
//		double magic = Math.sin(radLat);
//		double e2 = 2 / F - 1 / (F * F);
//		double ee = e2 / (1 - e2);
//
//		magic = 1 - ee * magic * magic;
//		double sqrtMagic = Math.sqrt(magic);
//		dLat = (dLat * 180.0) / ((A * (1 - ee)) / (magic * sqrtMagic) * PI);
//		dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
//		double lat = wgLat + dLat;
//		double lon = wgLon + dLon;
//		point.setLat(lat);
//		point.setLng(lon);
//		return point;
//	}
//
//	public static void transform(double wgLat, double wgLon, double[] latlng) {
//		if (outOfChina(wgLat, wgLon)) {
//			latlng[0] = wgLat;
//			latlng[1] = wgLon;
//			return;
//		}
//		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
//		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
//		double radLat = wgLat / 180.0 * PI;
//		double magic = Math.sin(radLat);
//		double e2 = 2 / F - 1 / (F * F);
//		double ee = e2 / (1 - e2);
//		magic = 1 - ee * magic * magic;
//		double sqrtMagic = Math.sqrt(magic);
//		dLat = (dLat * 180.0) / ((A * (1 - ee)) / (magic * sqrtMagic) * PI);
//		dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
//		latlng[0] = wgLat + dLat;
//		latlng[1] = wgLon + dLon;
//	}
//
//	private static boolean outOfChina(double lat, double lon) {
//		if (lon < 72.004 || lon > 137.8347)
//			return true;
//		if (lat < 0.8293 || lat > 55.8271)
//			return true;
//		return false;
//	}
//
//	private static double transformLat(double x, double y) {
//		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
//		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
//		ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
//		ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
//		return ret;
//	}
//
//	private static double transformLon(double x, double y) {
//		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
//		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
//		ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
//		ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
//		return ret;
//	}
//
//	/**
//	 * 由高斯投影坐标反算成经纬度，即由平面坐标系转化为大地坐标系
//	 * @param yAxis Y坐标
//	 * @param xAxis X坐标
//	 * @return
//	 */
//	public static double[] gaussToBL(double xAxis, double yAxis) {
//		int projNo;
//		int zoneWide; //// 带宽
//		double[] output = new double[2];
//		double longitude1, latitude1, longitude0, x0, y0, xval, yval;// latitude0,
//		double e1, e2, /* f, a, */ ee, nn, t, c, m, d, r, u, fai/* , iPI */;
//		// iPI = 0.0174532925199433; //// 3.1415926535898/180.0;
//		// a = 6378245.0; f = 1.0/298.3; //54年北京坐标系参数
//		// a = 6378140.0;f = 1 / 298.257; // 80年西安坐标系参数
//		// a = 6378137; f = 1 / 298.257223563 ;// WGS-84坐标系
//		zoneWide = 6; //// 6度带宽
//		projNo = (int) (yAxis / 1000000L); // 查找带号
//		longitude0 = (projNo - 1) * zoneWide + zoneWide / 2;
//		longitude0 = longitude0 * PI / 180.0; // 中央经线
//
//		x0 = projNo * 1000000L + 500000L;
//		y0 = 0;
//		xval = yAxis - x0;
//		yval = xAxis - y0; // 带内大地坐标，与xy系相反
//		e2 = 2 / F - 1 / (F * F);
//		e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
//		ee = e2 / (1 - e2);
//		m = yval;
//		u = m / (A * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
//		fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u)
//				+ (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * u)
//				+ (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
//		c = ee * Math.cos(fai) * Math.cos(fai);
//		t = Math.tan(fai) * Math.tan(fai);
//		nn = A / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));
//		r = A * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai))
//				* (1 - e2 * Math.sin(fai) * Math.sin(fai)));
//		d = xval / nn;
//		// 计算经度(Longitude) 纬度(Latitude)
//		longitude1 = longitude0 + (d - (1 + 2 * t + c) * d * d * d / 6
//				+ (5 - 2 * c + 28 * t - 3 * c * c + 8 * ee + 24 * t * t) * d * d * d * d * d / 120) / Math.cos(fai);
//		latitude1 = fai
//				- (nn * Math.tan(fai) / r) * (d * d / 2 - (5 + 3 * t + 10 * c - 4 * c * c - 9 * ee) * d * d * d * d / 24
//						+ (61 + 90 * t + 298 * c + 45 * t * t - 256 * ee - 3 * c * c) * d * d * d * d * d * d / 720);
//		// 转换为度 DD
//		output[0] = longitude1 / (PI / 180.0);
//		output[1] = latitude1 / (PI / 180.0);
//		return output;
//	}
//
//	// 由经纬度反算成高斯投影坐标
//	public static double[] gaussToBLToGauss(double longitude, double latitude) {
//		int projNo = 0;
//		int zoneWide; // //带宽
//		double[] output = new double[2];
//		double longitude1, latitude1, longitude0, x0, y0, xval, yval;
//		double /* a, f, */ e2, ee, nn, t, c, a, m/* , iPI */;
//		// iPI = 0.0174532925199433; // //3.1415926535898/180.0;
//		zoneWide = 6; // //6度带宽
//		// a = 6378245.0;f = 1.0 / 298.3; // 54年北京坐标系参数
//		// a=6378140.0; f=1/298.257; //80年西安坐标系参数
//		// a = 6378137; f = 1/ 298.257223563 ;// WGS-84坐标系
//		projNo = (int) (longitude / zoneWide);
//		longitude0 = projNo * zoneWide + zoneWide / 2;
//		longitude0 = longitude0 * PI / 180.0;
//		longitude1 = longitude * PI / 180.0; // 经度转换为弧度
//		latitude1 = latitude * PI / 180.0; // 纬度转换为弧度
//		e2 = 2 / F - 1 / (F * F);
//		ee = e2 / (1.0 - e2);
//		nn = A / Math.sqrt(1.0 - e2 * Math.sin(latitude1) * Math.sin(latitude1));
//		t = Math.tan(latitude1) * Math.tan(latitude1);
//		c = ee * Math.cos(latitude1) * Math.cos(latitude1);
//		a = (longitude1 - longitude0) * Math.cos(latitude1);
//		m = A * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * latitude1
//				- (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * latitude1)
//				+ (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * latitude1)
//				- (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * latitude1));
//		// 因为是以赤道为Y轴的，与我们南北为Y轴是相反的，所以xy与高斯投影的标准xy正好相反;
//		xval = nn
//				* (a + (1 - t + c) * a * a * a / 6 + (5 - 18 * t + t * t + 14 * c - 58 * ee) * a * a * a * a * a / 120);
//		yval = m + nn * Math.tan(latitude1) * (a * a / 2 + (5 - t + 9 * c + 4 * c * c) * a * a * a * a / 24
//				+ (61 - 58 * t + t * t + 270 * c - 330 * ee) * a * a * a * a * a * a / 720);
//		x0 = 1000000L * (projNo + 1) + 500000L;
//		y0 = 0;
//		xval = xval + x0;
//		yval = yval + y0;
//		output[0] = xval;
//		output[1] = yval;
//		return output;
//	}
//}
