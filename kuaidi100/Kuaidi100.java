package kuaidi100;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import express.Express;

public class Kuaidi100 implements Express {

	private final String ID = "xxxxx"; // �����Ȩ��key����������
	private final String SHOW = "0"; // �������ͣ�0������json�ַ�����1������xml����2������html����3������text�ı���������Ĭ�Ϸ���json�ַ�����
	private final String MUTI = "1"; // ������Ϣ������1:���ض�����������Ϣ��0:ֻ����һ����Ϣ������Ĭ�Ϸ��ض��С�
	private final String ORDER = "desc"; // ����desc����ʱ�����µ������У�asc����ʱ���ɾɵ������С�����Ĭ�Ϸ��ص��򣨴�Сд�����У�

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
			System.out.println("����GET��������쳣" + e);
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
