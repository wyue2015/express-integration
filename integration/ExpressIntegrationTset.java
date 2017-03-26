package integration;

import org.junit.Test;

public class ExpressIntegrationTset {

	@Test
	public void testGetExpress() throws Exception {
		String result1 = ExpressIntegration.getExpress("zhijiankuaidi").query("3325051911124", "shentong");
		System.out.println(result1);
		System.out.println("------------------------------------------");
		String result2 = ExpressIntegration.getExpress("kuaidiniao").query("3325051911124", "STO");
		System.out.println(result2);
		System.out.println("------------------------------------------");
		String result3 = ExpressIntegration.getExpress("kuaidiapi").query("3325051911124", "shentong");
		System.out.println(result3);
	}

}
