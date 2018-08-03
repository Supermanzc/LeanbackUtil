package hk.reco.baselib.util;

import java.security.MessageDigest;
import java.util.Random;

public class MD5Util {
	/**
	 * 
	 * @param platNo 解析密码
	 * @param start 约定加密起始
	 * @param end 约定加密结束
	 * @param len 随机码长度
	 * @param extP 扩展码
	 * @return
	 */
	public static boolean rePassWNo(String platNo, int start, int end, int len,
			String extP) {
		boolean flag = false;
		if (platNo != null && !platNo.equals("") && platNo.length() >= end) {
			String pa1 = platNo.substring(start, end);
			String pa1Md5 = MD5Encode(pa1 + extP);
			String pa = platNo.substring(len);
			if (pa != null && pa1Md5.equals(pa)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取加密码
	 * 
	 * @param start 加密随机码开始
	 * @param end 加密随机码结束
	 * @param len 随机码长度
	 * @param extP 扩展码
	 * @return
	 */
	public static String getPassWNo(int start, int end, int len, String extP) {
		String platNo = "";
		String ram = getRandomString(len);
		// System.out.println(ram);
		if (ram != null && !ram.equals("") && ram.length() >= end) {
			String pa1 = ram.substring(start, end);
			// System.out.println(pa1);
			String pa = MD5Encode(pa1 + extP);
			// System.out.println(pa);
			platNo = ram + pa;

		}
		return platNo;
	}

	/**
	 * 生成随机码
	 * 
	 * @param length
	 * @return
	 */
	private static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String MD5Encode(String origin) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(origin.getBytes("utf8"));
			byte[] result = md.digest();
            for (byte aResult : result) {
                // int val = result[i] & 0xff;
                // sb.append(Integer.toHexString(val));
                int val = (aResult & 0x000000ff) | 0xffffff00;
                sb.append(Integer.toHexString(val).substring(6));
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * md5加密
	 * 
	 * @param pwd
	 * @return
	 */
	public static String MD5(String pwd) {
		// 用于加密的字符
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			// 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
			byte[] btInput = pwd.getBytes();
			// 获得指定摘要算法的 MessageDigest对象，此处为MD5
			// MessageDigest类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
			// 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// System.out.println(mdInst);
			// MD5 Message Digest from SUN, <initialized>
			// MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
			mdInst.update(btInput);
			// System.out.println(mdInst);
			// MD5 Message Digest from SUN, <in progress>
			// 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
			byte[] md = mdInst.digest();
			// System.out.println(md);
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			// System.out.println(j);
			char str[] = new char[j * 2];
			int k = 0;
            for (byte byte0 : md) { // i = 0
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }
			// 返回经过加密后的字符串
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}
}
