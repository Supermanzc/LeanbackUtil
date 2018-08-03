package hk.reco.baselib.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Encrypt {

    private static final String CRYPT_KEY = "C1934hengKLdu";
    private final static String DES = "DES";

    public static final String PARTNER_KEY = "5428EAD4EE4022C44BF0610B8BEAE782";
    public static final String PARTNER_PRIVATE_KEY = "4D7DB1B1ADBBDC5562473DBB1F4B515191B5F096820D846E";


    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        return cipher.doFinal(src);
    }

    public final static String decrypt(String data) {
        try {
            return new String(decrypt(hex2byte(data.getBytes()), CRYPT_KEY.getBytes()));
        } catch (Exception e) {
        }
        return null;
    }

    private static byte[] hex2byte(byte[] b) {

        if ((b.length % 2) != 0)

            throw new IllegalArgumentException("长度不是偶数");

        byte[] b2 = new byte[b.length / 2];

        for (int n = 0; n < b.length; n += 2) {

            String item = new String(b, n, 2);

            b2[n / 2] = (byte) Integer.parseInt(item, 16);

        }

        return b2;
    }
}
