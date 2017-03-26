package integration;

import java.util.HashMap;

import express.Express;
import kuaidi100.Kuaidi100;
import kuaidiapi.Kuaidiapi;
import kuaidiniao.Kuaidiniao;
import kuaidiwang.Kuaidiwang;
import zhijiankuaidi.GtExpress;

public class ExpressIntegration {

	private static HashMap<String, Express> map = new HashMap<>();

	static {
		// ǰ������ݽӿڵ���Ȩkey�Կ��ã�������ʹ��
		map.put("zhijiankuaidi", new GtExpress());
		map.put("kuaidiniao", new Kuaidiniao());
		map.put("kuaidiapi", new Kuaidiapi());

		// �����������ݽӿڵ���Ȩkey�ѵ��ڣ����Խ������ͨ��
		map.put("kuaidi100", new Kuaidi100());
		map.put("kuaidiwang", new Kuaidiwang());
	}

	public static Express getExpress(String expressType) {
		return map.get(expressType);
	}

}
