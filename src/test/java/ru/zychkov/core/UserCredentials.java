package ru.zychkov.core;

import ru.zychkov.core.custom.Config;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserCredentials {

    private static final String ALGORITHM = "AES";
    private static final SecretKey SECRET_KEY =
            new SecretKeySpec(System.getenv("AUTOTEST_CREDENTIAL_TOKEN").getBytes(), ALGORITHM);

    public static String getPortalUser() {
        return decrypt(Config.getInstance().getPortalUsername());
    }

    public static String getPortalPassword() {
        return decrypt(Config.getInstance().getPortalPassword());
    }

    private static String decrypt(String encryptedText) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        byte[] decryptedByte;

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            decryptedByte = cipher.doFinal(encryptedTextByte);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return new String(decryptedByte);
    }
}
