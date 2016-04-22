/**
 * @author neckau
 */
package com.zero.misignature;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 小米用的签名工具
 * 
 * @author neckau
 */
public class SignatureUtils
{
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	/**
	 * 小米的签名
	 * @param params
	 * @param appSecret
	 * @return
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String sign(Map<String, String> params, String appSecret)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException
	{
		// 首先针对参数进行排序，按照字母顺序从小到大排序
		Vector<String> vec = sort(params);
		// 把参数通过&连接起来
		String result = append(vec, "&");
		// URLDecode
		result = decode(result);
		return HmacSHA1Encrypt(result, appSecret);
	}

	/**
	 * 把所有的key按照字母顺序从小到大排序
	 * @param params key-value对
	 * @return
	 */
	public static Vector<String> sort(final Map<String, String> params)
	{
		Vector<String> vec = new Vector<String>();
		if (null == params) {
			return vec;
		}
		for (Entry<String, String> entry : params.entrySet()) {
			// 必须要有value才能添加进去
			if (entry.getValue() != null && !entry.getValue().isEmpty()) {
				vec.add(entry.getKey() + "=" + entry.getValue());
			}
		}
		vec.sort(new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				int index = 0;
				int minLen = Math.min(o1.length(), o2.length());
				while (index < minLen) {
					int char1 = o1.charAt(index);
					int char2 = o2.charAt(index);
					if (char1 < char2) {
						return -1;
					} else if (char2 < char1) {
						return 1;
					}
					++index;
				}
				return 0;
			}
		});
		return vec;
	}

	/**
	 * 把字符串使用connector连接起来
	 * @param vec
	 * @param connector
	 * @return
	 */
	public static String append(Vector<String> vec, String connector)
	{
		if (null == vec || vec.isEmpty()) {
			return "";
		}
		if (null == connector) {
			connector = "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < vec.size() - 1; ++i) {
			sb.append(vec.get(i)).append(connector);
		}
		sb.append(vec.get(vec.size() - 1));
		return sb.toString();
	}

	public static String decode(String content)
	{
		if (null == content || content.isEmpty()) {
			return "";
		}
		try {
			return URLDecoder.decode(content, ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * 
	 * @param encryptText
	 *            被签名的字符串
	 * @param encryptKey
	 *            密钥
	 * @return 返回被加密后的字符串
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static String HmacSHA1Encrypt(String encryptText, String encryptKey)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException
	{
		byte[] data = encryptKey.getBytes(ENCODING);
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);
		byte[] text = encryptText.getBytes(ENCODING);
		// 完成 Mac 操作
		byte[] digest = mac.doFinal(text);
		StringBuilder sBuilder = bytesToHexString(digest);
		return sBuilder.toString();
	}

	/**
	 * 转换成Hex
	 * 
	 * @param bytesArray
	 */
	public static StringBuilder bytesToHexString(byte[] bytesArray)
	{
		if (bytesArray == null) {
			return null;
		}
		StringBuilder sBuilder = new StringBuilder();
		for (byte b : bytesArray) {
			String hv = String.format("%02x", b);
			sBuilder.append(hv);
		}
		return sBuilder;
	}
}
