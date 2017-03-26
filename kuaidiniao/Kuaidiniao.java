package kuaidiniao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import express.Express;

public class Kuaidiniao implements Express {

	private final String EBusinessID = "1262757";
	private final String AppKey = "65de43dc-5abc-4b6c-8e3b-d81d72de2756";
	private final String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

	/**
	 * 
	 * @param kd
	 * @return
	 * @throws Exception
	 * 
	 *             功能：根据传入的kuaidiniao实例，使用Json格式的请求数据获取快递轨迹
	 */
	public String query(String express_order_number, String company_id) throws Exception {

		String requestData = "{'OrderCode':'','ShipperCode':'" + company_id + "','LogisticCode':'"
				+ express_order_number + "'}";

		// 将快递鸟的5个系统级数据封装到Map中存放
		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", urlEncoder(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		params.put("RequestType", "1002");
		params.put("DataSign", encrypt(requestData, AppKey, "UTF-8"));
		params.put("DataType", "2");

		String result = sendPost(ReqURL, params);

		return result;
	}

	/**
	 * @param str
	 *            需要MD5加密处理的字符串
	 * @param charset
	 *            指定编码格式
	 * 
	 *            功能：使用指定的编码格式charset对字符串str进行MD5编码处理
	 * 
	 */
	private String MD5(String str, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(charset));
		byte[] result = md.digest();
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < result.length; i++) {
			int val = result[i] & 0xff;
			if (val <= 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val));
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * @param str
	 *            需要url编码处理的字符串
	 * @param charset
	 *            指定编码格式
	 * 
	 *            功能：使用指定的编码方式charset对字符串str进行url编码处理
	 */
	private String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
		String result = URLEncoder.encode(str, charset);
		return result;
	}

	/**
	 * @param content
	 *            带加密为数字签名的请求数据，实际上就是包含快递公司代码和快递单号的请求数据requestData（未进行url编码处理）
	 * @param keyValue
	 *            就是申请得到的AppKey
	 * @param charset
	 *            指定编码格式
	 * 
	 *            功能：请求数据content+密钥AppKey，依次对其进行如下顺序进行加密或编码处理：MD5加密、Base64编码、URL(UTF-8)编码，得到数据签名DataSign
	 */
	private String encrypt(String content, String keyValue, String charset)
			throws UnsupportedEncodingException, Exception {
		if (keyValue != null) {
			// 返回快递鸟系统级参数中的数据签名DataSign,具体可参考文档《快递鸟使用说明》
			return urlEncoder(Base64.getEncoder().encodeToString(MD5(content + keyValue, charset).getBytes(charset)),
					charset);
		}
		// 若执行该语句，则快递鸟接口返回验证数据签名失败，因为密钥为空
		return urlEncoder(Base64.getEncoder().encodeToString(MD5(content, charset).getBytes(charset)), charset);
	}

	/**
	 * @param url
	 *            请求链接ReqURL
	 * @param params
	 *            封装快递鸟的5个系统级数据
	 * 
	 *            功能：从快递鸟接口获取快递的轨迹信息，并存放在字符串result中
	 */
	private String sendPost(String url, Map<String, String> params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = null;

		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

			// 设置为POST
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");

			// 设置Http请求头，并获取链连接
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();

			// 将快递鸟的5个系统级数据传给快递鸟服务器
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			if (params != null) {
				StringBuilder param = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (param.length() > 0) {
						param.append("&");
					}
					param.append(entry.getKey());
					param.append("=");
					param.append(entry.getValue());
				}
				out.write(param.toString());
			}
			out.flush();

			// 从快递鸟服务器读取数据，并存放在result中
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			result = new StringBuilder();
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

}
