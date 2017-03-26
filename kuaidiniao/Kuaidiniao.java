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
	 *             ���ܣ����ݴ����kuaidiniaoʵ����ʹ��Json��ʽ���������ݻ�ȡ��ݹ켣
	 */
	public String query(String express_order_number, String company_id) throws Exception {

		String requestData = "{'OrderCode':'','ShipperCode':'" + company_id + "','LogisticCode':'"
				+ express_order_number + "'}";

		// ��������5��ϵͳ�����ݷ�װ��Map�д��
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
	 *            ��ҪMD5���ܴ�����ַ���
	 * @param charset
	 *            ָ�������ʽ
	 * 
	 *            ���ܣ�ʹ��ָ���ı����ʽcharset���ַ���str����MD5���봦��
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
	 *            ��Ҫurl���봦����ַ���
	 * @param charset
	 *            ָ�������ʽ
	 * 
	 *            ���ܣ�ʹ��ָ���ı��뷽ʽcharset���ַ���str����url���봦��
	 */
	private String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
		String result = URLEncoder.encode(str, charset);
		return result;
	}

	/**
	 * @param content
	 *            ������Ϊ����ǩ�����������ݣ�ʵ���Ͼ��ǰ�����ݹ�˾����Ϳ�ݵ��ŵ���������requestData��δ����url���봦��
	 * @param keyValue
	 *            ��������õ���AppKey
	 * @param charset
	 *            ָ�������ʽ
	 * 
	 *            ���ܣ���������content+��ԿAppKey�����ζ����������˳����м��ܻ���봦��MD5���ܡ�Base64���롢URL(UTF-8)���룬�õ�����ǩ��DataSign
	 */
	private String encrypt(String content, String keyValue, String charset)
			throws UnsupportedEncodingException, Exception {
		if (keyValue != null) {
			// ���ؿ����ϵͳ�������е�����ǩ��DataSign,����ɲο��ĵ��������ʹ��˵����
			return urlEncoder(Base64.getEncoder().encodeToString(MD5(content + keyValue, charset).getBytes(charset)),
					charset);
		}
		// ��ִ�и���䣬������ӿڷ�����֤����ǩ��ʧ�ܣ���Ϊ��ԿΪ��
		return urlEncoder(Base64.getEncoder().encodeToString(MD5(content, charset).getBytes(charset)), charset);
	}

	/**
	 * @param url
	 *            ��������ReqURL
	 * @param params
	 *            ��װ������5��ϵͳ������
	 * 
	 *            ���ܣ��ӿ����ӿڻ�ȡ��ݵĹ켣��Ϣ����������ַ���result��
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
