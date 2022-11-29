package com.mikuac.shiro.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class RSAUtils {
    public static String encrypt(String modulus, String exponent, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //modulus和exponent都是base64编码，获取公匙
        //RSAPublicKey publicKey = RSAUtil.getPublicKey(modulus, exponent);
        // use String to hold cipher binary data
        Base64 base64 = new Base64();
        //String cipherTextBase64 = base64.encodeToString(cipherText);
        byte[] modulusByte = base64.decode(modulus);
        byte[] exponentByte = base64.decode(exponent);
        Cipher cipher = Cipher.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(modulusByte), new BigInteger(exponentByte));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(password.getBytes());
        return base64.encodeToString(cipherText);
    }

    public static String encryptOne(String key, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        Base64 base64 = new Base64();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(base64.decode(key)));
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(password.getBytes());
        return base64.encodeToString(cipherText);
    }
}
