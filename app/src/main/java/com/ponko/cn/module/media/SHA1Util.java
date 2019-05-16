package com.ponko.cn.module.media;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Title: SHA1算法</p>
 *
 * @author levi
 */
 public class SHA1Util {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //byte字节转换成16进制的字符串MD5Utils.hexString
    public static byte[] eccrypt(String info, String shaType) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(shaType);
        byte[] srcBytes = info.getBytes();
        // 使用srcBytes更新摘要
        sha.update(srcBytes);
        // 完成哈希计算，得到result
        byte[] resultBytes = sha.digest();
        return resultBytes;
    }

    public static byte[] eccryptSHA1(String info) throws NoSuchAlgorithmException {
        return eccrypt(info, "SHA1");
    }

    public byte[] eccryptSHA256(String info) throws NoSuchAlgorithmException {
        return eccrypt(info, "SHA-256");
    }

    public byte[] eccryptSHA384(String info) throws NoSuchAlgorithmException {
        return eccrypt(info, "SHA-384");
    }

    public byte[] eccryptSHA512(String info) throws NoSuchAlgorithmException {
        return eccrypt(info, "SHA-512");
    }

    public static String hexString(byte[] bytes){
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            int val = ((int) bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}

