package com.test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Copyright 2017 杭州天阙. All rights reserved.
 * 
 * @company：杭州天阙
 * @Prject: sichuan_grid
 * @path：com.tianque.webService.util.HttpUtil.java
 * @author: zhangxiong
 * @createDate: 2017年11月6日 下午3:37:08
 * @department:四川分中心(研发)
 * @Modifier:（修改者姓名）
 * @updateDate：2017年11月6日下午3:37:08
 * @version:V1.0
 */
public class HttpUtil {

	/**
	 * 从网络Url中下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @throws IOException
	 */
	public static String downloadFile(String urlStr, String fileName) throws IOException {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			StringBuffer strBuf = new StringBuffer();
			String inputName = "downFilePath";// (String) entry.getKey();
			String inputValue = fileName;// (String) entry.getValue();
			strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
			strBuf.append(inputValue);
			out.write(strBuf.toString().getBytes());
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			// 文件大小
			int fileLength = conn.getContentLength();
			System.out.println("file length---->" + fileLength);
			BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
			String path = "uploadFile" + File.separatorChar + fileName;
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			int size = 0;
			byte[] buf = new byte[1024];
			while ((size = bin.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.close();
			bin.close();
			System.out.println("----------->恭喜你,文件下载成功<--------------------------");
			System.out.println("----------->文件路径为:" + path + "<-------------");
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * 发送HTTP GET请求
	 * @param urlStr
	 * @return
	 */
	public static String doGet(String urlStr) {
		try {
			URL url = new URL(urlStr.trim());
			// 打开连接
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			if (200 == urlConnection.getResponseCode()) {
				// 得到输入流
				InputStream is = urlConnection.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = is.read(buffer))) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				return baos.toString("utf-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 发送HTTP POST请求
	 * @param urlStr
	 * @return
	 */
	public static String doPost(String urlStr, String param) {
		URL url = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			// conn.setConnectTimeout(10000);//连接超时 单位毫秒
			// conn.setReadTimeout(2000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(param);// post的参数 xx=xx&yy=yy
			// flush输出流的缓冲
			printWriter.flush();
			// 开始获取数据
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return bos.toString("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送HTTP POST请求
	 * @param urlStr
	 * @return
	 */
	public static String doPostContentType(String urlStr, String param) {
		URL url = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			// conn.setConnectTimeout(10000);//连接超时 单位毫秒
			// conn.setReadTimeout(2000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(param);// post的参数 xx=xx&yy=yy
			// flush输出流的缓冲
			printWriter.flush();
			// 开始获取数据
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return bos.toString("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String result = doPost("http://localhost/user/sayHellow",
				"param=%E5%93%88%E5%93%88&a=aaaaaa&&b=bbbbb&c=cccccccccccccc");
		System.out.println(result);
	}
}
