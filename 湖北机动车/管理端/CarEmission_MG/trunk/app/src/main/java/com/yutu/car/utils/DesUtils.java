package com.yutu.car.utils;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesUtils {
	/**
	 * 根据指定的密钥加密字符串
	 * @param encryptString 需要加密的字符串
	 * @param encryptKey 密钥
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String encryptDES(String encryptString, String encryptKey)
			throws Exception {
		// 实例化IvParameterSpec对象，使用指定的初始化向量，初始化向量根据key生成
		IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.getBytes("ASCII"));
		// 实例化SecretKeySpec类，根据字节数组来构造SecretKey
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		// 创建密码器
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 用秘钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		// 执行加密操作
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		return Base64.encodeToString(encryptedData, Base64.DEFAULT);
	}
	/**
	 * 根据指定的密钥对字符串解密
	 * @param decrypString 需要解密的字符串
	 * @param decryptKey 密钥
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decryptDES(String decrypString, String decryptKey)
			throws Exception {
		byte[] byteMi = Base64.decode(decrypString, Base64.DEFAULT);
		// 实例化IvParameterSpec对象，使用指定的初始化向量
		IvParameterSpec zeroIv = new IvParameterSpec(decryptKey.getBytes("ASCII"));
		// 实例化SecretKeySpec类，根据字节数组来构造SecretKey
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		// 创建密码器
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 用秘钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		// 执行解密操作
		byte[] decryptedData = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}
}