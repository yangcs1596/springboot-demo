package com.example.demo.util.Decryption;

/**
 * @Author: 杨春生
 * @Date: 2019/12/5 11:39
 * @Description:
 */

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static final String ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "PUBLIC_KEY";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";
    /**
     * 加密算法
     */
    private static final String CIPHER_DE = "RSA";
    /**
     * 解密算法
     */
    private static final String CIPHER_EN = "RSA";
    /**
     * 密钥长度
     */
    private static final Integer KEY_LENGTH = 2048;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成秘钥对，公钥和私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, Object> genKeyPair() throws NoSuchAlgorithmException {
        Map<String, Object> keyMap = new HashMap<String, Object>();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        // 秘钥字节数
        keyPairGenerator.initialize(KEY_LENGTH);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws InvalidKeySpecException
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        // 得到公钥
        byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key key = keyFactory.generatePublic(x509EncodedKeySpec);
        // 加密数据，分段加密
        Cipher cipher = Cipher.getInstance(CIPHER_EN);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //cipher.doFinal(data);
        //分段加密？
//        int inputLength = data.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offset = 0;
//        byte[] cache;
//        int i = 0;
//        while (inputLength - offset > 0) {
//            if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offset, inputLength - offset);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offset = i * MAX_ENCRYPT_BLOCK;
//        }
//        byte[] encryptedData = out.toByteArray();
//        out.close();
//        return encryptedData;
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        // 得到私钥
        byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key key = keyFactory.generatePrivate(pKCS8EncodedKeySpec);
        // 解密数据，分段解密
        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.DECRYPT_MODE, key);
//        int inputLength = data.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offset = 0;
//        byte[] cache;
//        int i = 0;
//        byte[] tmp;
//        while (inputLength - offset > 0) {
//            if (inputLength - offset > MAX_DECRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offset, inputLength - offset);
//            }
////            out.write(cache, 0, cache.length);
//            out.write(cache);
//            i++;
//            offset = i * MAX_DECRYPT_BLOCK;
//        }
//        byte[] decryptedData = out.toByteArray();
//        out.close();
//        return decryptedData;
        return cipher.doFinal(data);
    }

    /**
     * 获取公钥
     *
     * @param keyMap
     * @return
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        String str = new String(Base64.encodeBase64(key.getEncoded()));
        return str;
    }

    /**
     * 获取私钥
     *
     * @param keyMap
     * @return
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        String str = new String(Base64.encodeBase64(key.getEncoded()));
        return str;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> keyMap = RSAUtils.genKeyPair();
        String publicKey = RSAUtils.getPublicKey(keyMap);
        String privateKey = RSAUtils.getPrivateKey(keyMap);
        System.out.println("公钥：" + publicKey);
        System.out.println("私钥：" + privateKey);
        // 公钥加密
        String sourceStr = "GZAQo23456";
        System.out.println("加密前：" + sourceStr);
        byte[] encryptStrByte = RSAUtils.encryptByPublicKey(sourceStr.getBytes(), publicKey);
        byte[] btt = Base64.encodeBase64(encryptStrByte);
        String encryptStr = new String(btt);
        System.out.println("加密后：" + encryptStr);
        System.out.println("长度：" + encryptStr.length());
        // 私钥解密
        byte[] decryptStrByte = RSAUtils.decryptByPrivateKey(Base64.decodeBase64(Base64.encodeBase64(encryptStrByte)), privateKey);
        String sourceStr_1 = new String(decryptStrByte);
        System.out.println("解密后：" + sourceStr_1);
    }
}
