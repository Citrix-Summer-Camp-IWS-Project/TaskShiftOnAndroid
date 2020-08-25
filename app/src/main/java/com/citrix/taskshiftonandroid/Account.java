package com.citrix.taskshiftonandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Account implements Serializable {
    private String username;
    private String token;
    private String accountID;
    private String url;
    private String clientID;
    private String clientSecret;

    Account(String username, String token, String accountID, String url, String clientID, String clientSecret) {
        this.username = username;
        this.token = token;
        this.accountID = accountID;
        this.url = url;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getUrl() { return url; }

    public String getClientID() { return clientID; }

    public String getClientSecret() { return clientSecret; }


    public static byte[] RSAEncrypt(String text, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text.getBytes("UTF-8"));
    }

    public static byte[] RSADecrypt(byte[] text, PublicKey key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(text);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static PrivateKey getPriKey(Account mAccount, MainActivity activity) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        int usernamePos = mAccount.getUsername().indexOf("@");
        String identifier = mAccount.getUsername().substring(0, usernamePos) + "private";
        InputStream privateKey = activity.getResources().openRawResource(activity.getResources().getIdentifier(identifier,
                "raw", activity.getPackageName()));
        byte[] bytes = activity.toByteArray(privateKey);
        PrivateKey key =
                KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
        return key;
    }
    public static PublicKey getPubKey(String helMsg, MainActivity activity) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        int usernamePos = helMsg.indexOf("@");
        String identifier = helMsg.substring(0, usernamePos) + "public";
        InputStream publicKey = activity.getResources().openRawResource(activity.getResources().getIdentifier(identifier,
                "raw", activity.getPackageName()));
        byte[] bytes1;
        bytes1 = activity.toByteArray(publicKey);
        PublicKey pubKey;
        pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes1));
        return pubKey;
    }
}
