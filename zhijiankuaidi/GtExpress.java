package zhijiankuaidi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import express.Express;

public class GtExpress implements Express {

	private final String gtex_login = "yabolove"; // �û���¼�˺�
	private final String gtex_key = "gtexpress"; // ���ͨ������Ȩkey
	private final String gtex_method = "getOrderByCarrier"; // �ӿڷ�������������дΪgetOrderByCarrier
	private final String timestamp = "2016-8-15 14:00:00"; // ��ǰʱ�䣬��ʽΪ��yyyy-MM-dd
															// HH:mm:ss

	/**
	 * 
	 * @param gtExpress
	 * @return
	 * @throws Exception
	 * 
	 *             ���ܣ����ݴ����GtExpressʵ����ʹ��Json��ʽ���������ݻ�ȡ��ݹ켣
	 */
	public String query(String billCode, String carrierCode) throws Exception {
		String gtex_params = "{'gtex_bill_code':'" + billCode + "','gtex_carrier_code':'" + carrierCode + "'}";

		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("gtex_type", "json");
		requestParams.put("gtex_user_login", gtex_login);
		requestParams.put("gtex_key", gtex_key);
		requestParams.put("gtex_method", gtex_method);
		requestParams.put("timestamp", timestamp);
		requestParams.put("gtex_params", gtex_params);
		requestParams.put("gtex_sign", MD5(gtex_key + gtex_login + timestamp));

		String url = "http://api.gtexpress.cn/gt_open/query/order/query.action";
		return sendPost(url, requestParams);
	}

	/**
	 * 
	 * ʵ�ּ���һ���ַ�����MD5ֵ
	 *
	 */
	private static String MD5(String str) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes("utf-8"));
		byte[] result = md.digest();// ���MD5���ܺ������

		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < result.length; i++) {

			int val = result[i] & 0xff;
			if (val <= 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val));

		}
		return sb.toString().toUpperCase();
	}

	/**
	 * @param url
	 *            ��������ReqURL
	 * @param params
	 *            ��ָ���ݽӿڵ�7��������װ��Map��params��
	 * 
	 *            ���ܣ���ָ���ݽӿڻ�ȡ��ݵĹ켣��Ϣ����������ַ���result��
	 */
	private String sendPost(String url, Map<String, String> params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = null;

		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

			// ����ΪPOST
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");

			// ����Http����ͷ������ȡ������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();

			// ��������5��ϵͳ�����ݴ�������������
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

			// �ӿ�����������ȡ���ݣ��������result��
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
