package kuaidiwang;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import express.Express;

public class Kuaidiwang implements Express {

	private final String ID = "xxxxx"; // 身份授权key
	private final String SHOW = "0"; // 返回类型：0：返回json字符串，1：返回xml对象，如果不填，默认返回json字符串。
	private final String MUTI = "0"; // 返回信息数量：0:返回多行完整的信息,1:只返回一行信息。不填默认返回多行。
	private final String ORDER = "desc"; // 排序：desc：按时间由新到旧排列，asc：按时间由旧到新排列。不填默认由新到旧排列（大小写不敏感）

	public String query(String billCode, String companyNum) {
		String express_url = "http://api.kuaidi.com/openapi.html?id=" + ID + "&com=" + companyNum + "&nu=" + billCode
				+ "&show=" + SHOW + "&muti=" + MUTI + "&order=" + ORDER;
		String order_info = sendGet(express_url);
		return order_info;
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
