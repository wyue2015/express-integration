package kuaidiapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import express.Express;

public class Kuaidiapi implements Express {

	private final String USER_ID = "75110";
	private final String USER_KEY = "f68846f72ee7493bb49502164b16ddf0";
	private final String SHOW_TEXT_FORMAT = "json";
	private final String TIME_SORT = "desc";

	public String query(String express_order_number, String company_id) {
		String express_url = "http://www.kuaidiapi.cn/rest/?uid=" + USER_ID + "&key=" + USER_KEY + "&order="
				+ express_order_number + "&id=" + company_id + "&show=" + SHOW_TEXT_FORMAT + "&ord=" + TIME_SORT;
		String order_info = sendGet(express_url);
		return order_info;
	}

	private static String sendGet(String url) {
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
