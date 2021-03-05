package com.chenm.user_manager.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * DES加密解密工具类
 * @author admin
 *
 */
public class DesUtil {
	
	private static final String DES="DES";

    /**
     * 公钥  8位以上
     */
    private static final String SECRET_KEY="12345678";
	
    
    /**
     * 获取秘钥对象
     * @return
     * @throws Exception
     */
    private static final SecretKey getSecretKeyFactory() throws Exception {
        SecretKeyFactory des = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = des.generateSecret(new DESKeySpec(SECRET_KEY.getBytes()));
        return secretKey;
    }

    /**
     * 加密
     * @param param
     * @return
     * @throws Exception
     */
    public static final String encryption(String param) throws Exception {
        Cipher cipher = Cipher.getInstance(DES);
        SecretKey secretKey = getSecretKeyFactory();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return  new String(Base64.encodeBase64(cipher.doFinal(param.toString().getBytes())));
    }

    /**
     * 解密
     * @param value
     * @return
     * @throws Exception
     */
    public static final String decrypt(String value) throws Exception {
        Cipher cipher = Cipher.getInstance(DES);
        SecretKey secretKey = getSecretKeyFactory();
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.decodeBase64(value.getBytes())));
    }
    
    public static void main(String[] args) throws Exception {
		System.out.println(DesUtil.encryption("123456789VCc"));
	}
    
}
