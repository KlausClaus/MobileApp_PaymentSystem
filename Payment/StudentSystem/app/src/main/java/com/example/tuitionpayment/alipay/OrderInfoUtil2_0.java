package com.example.tuitionpayment.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Utility class for generating and signing order parameters for Alipay payments.
 * Note: For security reasons, signing and sensitive key handling should always occur on the server side in a production environment.
 */
public class OrderInfoUtil2_0 {

	/**
	 * Builds a map of parameters for authorization requests.
	 *
	 * @param pid       The merchant's partner ID.
	 * @param app_id    The app ID obtained during merchant registration.
	 * @param target_id A unique identifier for the merchant.
	 * @param rsa2      Whether to use RSA2 for signing.
	 * @return A map containing authorization parameters.
	 */
	public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id, boolean rsa2) {
		Map<String, String> keyValues = new HashMap<String, String>();

		keyValues.put("app_id", app_id);
		keyValues.put("pid", pid);
		keyValues.put("apiname", "com.alipay.account.auth");
		keyValues.put("methodname", "alipay.open.auth.sdk.code.get");
		keyValues.put("app_name", "mc");
		keyValues.put("biz_type", "openservice");
		keyValues.put("product_id", "APP_FAST_LOGIN");
		keyValues.put("scope", "kuaijie");
		keyValues.put("target_id", target_id);
		keyValues.put("auth_type", "AUTHACCOUNT");
		keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

		return keyValues;
	}

	/**
	 * Builds a map of parameters for payment orders.
	 *
	 * @param app_id The app ID obtained during merchant registration.
	 * @param rsa2   Whether to use RSA2 for signing.
	 * @param price  The price of the payment.
	 * @return A map containing payment order parameters.
	 */
	public static Map<String, String> buildOrderParamMap(String app_id, boolean rsa2, String price) {
		Map<String, String> keyValues = new HashMap<String, String>();

		keyValues.put("app_id", app_id);

		keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":"+"\""+price+"\",\"subject\":\"Tuition fee\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + getOutTradeNo() +  "\"}");

		keyValues.put("charset", "utf-8");

		keyValues.put("method", "alipay.trade.app.pay");

		keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

		keyValues.put("timestamp", "2016-07-29 16:55:53");

		keyValues.put("version", "1.0");

		return keyValues;
	}

	/**
	 * Builds a string of order parameters from the provided map.
	 *
	 * @param map The map of order parameters.
	 * @return A string representation of the order parameters.
	 */
	public static String buildOrderParam(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			sb.append(buildKeyValue(key, value, true));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		sb.append(buildKeyValue(tailKey, tailValue, true));

		return sb.toString();
	}

	/**
	 * Constructs a key-value pair as a string.
	 *
	 * @param key      The key.
	 * @param value    The value.
	 * @param isEncode Whether to URL-encode the value.
	 * @return A string representing the key-value pair.
	 */
	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		if (isEncode) {
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				sb.append(value);
			}
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * Signs the provided order parameters.
	 *
	 * @param map    The map of parameters to sign.
	 * @param rsaKey The RSA key for signing.
	 * @param rsa2   Whether to use RSA2 for signing.
	 * @return The signed parameters as a string.
	 */
	public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
		List<String> keys = new ArrayList<String>(map.keySet());
		// key排序
		Collections.sort(keys);

		StringBuilder authInfo = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			authInfo.append(buildKeyValue(key, value, false));
			authInfo.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		authInfo.append(buildKeyValue(tailKey, tailValue, false));

		String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
		String encodedSign = "";

		try {
			encodedSign = URLEncoder.encode(oriSign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "sign=" + encodedSign;
	}

	/**
	 * Generates a unique order number.
	 *
	 * @return A unique order number.
	 */
	private static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

}
