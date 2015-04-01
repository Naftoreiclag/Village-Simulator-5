/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {
    public static byte[] encrypt(String key, String message) {
        return process(HashUtil.hash24(key), message.getBytes(), Cipher.ENCRYPT_MODE);
    }
    public static byte[] decrypt(String key, byte[] message) {
        return process(HashUtil.hash24(key), message, Cipher.DECRYPT_MODE);
    }
    
    public static byte[] encrypt(byte[] key, byte[] message) {
        return process(key, message, Cipher.ENCRYPT_MODE);
    }
    public static byte[] decrypt(byte[] key, byte[] message) {
        return process(key, message, Cipher.DECRYPT_MODE);
    }
    
    public static byte[] process(byte[] key, byte[] message, int mode) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "DESede");
            
            Cipher cipher = Cipher.getInstance("DESede");
            
            cipher.init(mode, keySpec);
            
            byte[] processedBytes = new byte[cipher.getOutputSize(message.length)];
            int processedBytesLength = cipher.update(message, 0, message.length, processedBytes, 0);
            processedBytesLength += cipher.doFinal(processedBytes, processedBytesLength);
            
            return processedBytes;
            
        }
        catch(Exception e) {
            return new byte[]{};
        }

    }
    
    public static void generate(String key, String message) {
        HashUtil.getJavaCodeLol(encrypt(key, message));
        
    }
    
    public static byte[] congrats = new byte[] {-103,-22,59,-46,38,-52,-22,-95,25,-59,-65,-101,22,-70,-114,74};
    public static byte[] jalol = new byte[] {62,-10,47,53,-14,47,21,27,23,90,-64,30,103,-103,-19,60,-83,-5,-81,96,4,-101,0,-32,122,120,-3,-32,105,123,6,34,-106,38,39,5,-108,-98,76,-6};
}
