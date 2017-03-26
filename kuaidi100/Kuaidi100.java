package kuaidi100;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import express.Express;

public class Kuaidi100 implements Express {

	private final String ID = "xxxxx"; // 身份授权的key，可申请获得
	private final String SHOW = "0"; // 返回类型：0：返回json字符串，1：返回xml对象，2：返回html对象，3：返回text文本。如果不填，默认返回json字符串。
	private final String MUTI = "1"; // 返回信息数量：1:返回多行完整的信息，0:只返回一行信息。不填默认返回多行。
	private final String ORDER = "desc"; // 排序：desc：按时间由新到旧排列，asc：按时间由旧到新排列。不填默认返回倒序（大小写不敏感）

	@Override
	public String query(String billCode, String carrierCode) throws Exception {
		String express_url = "http://api.kuaidi100.com/api?id=" + ID + "&com=" + carrierCode + "&nu=" + billCode
				+ "&show=" + SHOW + "&muti=" + MUTI + "&order=" + ORDER;
		return sendGet(express_url);
	}

	private String sendGet(String url) {
		String order_info = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");

			connection.connect();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				order_info += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常" + e);
			e.printStackTrace();
		}

		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return order_info;
	}

}
