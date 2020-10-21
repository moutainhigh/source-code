package com.yd.api.pay.util;


public class JjbPayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 商户号
	public static String merchant_id = "jujibao001";
	// 商户的MD5密钥
	public static String key = "UuTk4RGMTTyWlSFyt1UG4A4jPEIWkvhE";
	// 商户的RSA私钥
	public static String private_key = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAKULHAnUqE8pm6IENyBrwv+0YTZzGYDBDhonEOaZJ58HU1GFuu8vnWAp/lDVLQn2IPM8Bjsoo7Q+R6WBczoR8dV6aAJlaPzG3gVQtASfc3H2euxwVOzvgMZ5clmzlZd5mO3GFcTMiZOSHrRcsd3jSeM7378oo/63IfkCx/SnhputAgMBAAECgYEAmjEi5NxU0oCg8mDVz+hdqd+v4trtvKigsDZHR+lWZvDFGqjB2Ky3bxxMiOBgz6mvZw9xEtwI9WAqN6oVKO+S6sr5WrrL20yGNFOfdHQcqZKvgA2XXug//HrEzkLd2I0s3lLKK0tP+5+DPr9N22lJe8+TyayYY3AooEk9pEvCn4ECQQDNz5/5l89l9vqxrPqBU8nN5eXq3VSrACioQhR2kVKVmAg2kTd+oead/doO5md1L2w0M9ahVMR+UHwj1HEADGApAkEAzUpxFnqAtujgqzhxaMKHTEAwIIVwxCW/+TipazE1vhESP+QW8JG7xgYibOqTSIDUjFkAF3K/9uhDt0XgGca/5QJBALEPlLOoNVelmZvOE1279fvbc2gRrkvHQTMwVmaDM6dbOoWnfTTGmJ8Hy9fRxOP+PnrSti3pvXpmR8aTd/vwqbECQQC61Z44hAMy9JzMl5D3c9to9R4X6vonOegOrJfb+6xar/S4+yA5j5iib6DTGWYz3TQUC0f/DytQTjCa0thS/URVAkEApCAfyCDbIhBTzdx63dBdfL9Wr7aeBRiY0eySEcvDPn7zTxueAk4VPwK7VPVpvLGPyadFsx13i1c1h7+prAGflg==";
	// 聚积宝的公钥，无需修改该值
	public static String jjb_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtE+0BMwJtS08e68f4ABQ4Wi9zvtsYsOSXzx6AoNlVCdw4nbE254mOWzzbKEREYWLt7KgNKaQvdOHVzFDdqpMf95T/x8WoaGpRFmUFuMFOVLpHgN/ZIiSBd2uFU0XMjX5+i+mhEKRahv9jPrzrdcxPPVjXNA0xBHqnB2E58Z3oOQIDAQAB";
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	// 签名方式 不需修改,这里可以采用MD5或者RSA方式
	public static String sign_type = "MD5";
}
