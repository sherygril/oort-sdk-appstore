
package com.oortcloud.basemodule.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.oortcloud.basemodule.constant.Constant;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @filename: RSAUtils.java
 * @function：对用户登录信息加密
 * @version：
 * @author: zhangzhijun
 * @date: 2019/11/4 10:18
 */
public class RSAUtils {
    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 512;

    private static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeBase64(Constant.PUBLIC_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static String encryptRSA(String jsonStr) throws Exception{
        PublicKey publicKey = getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        cipher.update(jsonStr.getBytes("UTF-8"));
        return encodeBase64(cipher.doFinal());
    }


    public static String decryptByPublicKey(byte[] encryptedData)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(Constant.PUBLIC_KEY);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }


    /**
     * 图片解密  密文长度为512
     * 1 解密之后将 byte 转 bitmap
     * 2 将bitmap转 base64码 以String 形式存储
     *
     *  示例:
     *  image.setBitmap(base64ToBitmap(person.photo))
     */
    private static final int MAX_DECRYPT_BLOCK_IMG = 512;
    public static String decryptByPublicKeyImg(byte[] encryptedData)
            throws Exception {
        try{
            byte[] keyBytes = Base64Utils.decode(Constant.PUBLIC_KEY);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK_IMG) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK_IMG);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK_IMG;
            }
            byte[] decryptedData = out.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(decryptedData, 0, decryptedData.length);
            return BitmapUtils.bitmapToBase64(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
      return "";
    }


    public static byte[] decodeBase64(String target) throws Exception {
        return Base64.decode(target.getBytes("UTF-8"), Base64.DEFAULT);
    }

    private static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encode(source, Base64.DEFAULT));
    }
}



