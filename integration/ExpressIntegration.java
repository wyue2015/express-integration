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
		// 前三个快递接口的授权key仍可用，可正常使用
		map.put("zhijiankuaidi", new GtExpress());
		map.put("kuaidiniao", new Kuaidiniao());
		map.put("kuaidiapi", new Kuaidiapi());

		// 后面的两个快递接口的授权key已到期，测试结果不会通过
		map.put("kuaidi100", new Kuaidi100());
		map.put("kuaidiwang", new Kuaidiwang());
	}

	public static Express getExpress(String expressType) {
		return map.get(expressType);
	}

}
