/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {
    
    public static byte[] pin_number = new byte[] {-20,126,60,-117,-122,-24,11,40,-28,-20,86,-47,-24,-27,-65,-13,-51,-110,78,-46,-25,24,6,-123,-31,-128,46,1,-124,-86,45,46};
    
    public static boolean equals(byte[] compare, byte[] withWhat) {
        if(compare.length != withWhat.length) {
            return false;
        }
        
        for(int i = 0; i < compare.length; ++ i) {
            if(compare[i] != withWhat[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    public static byte[] empty256() {
        byte[] ret = new byte[32];
        for(int i = 0; i < 32; ++ i) {
            ret[i] = 0;
        }
        
        return ret;
    }
    
    public static byte[] sha256(String input) {
        
        byte[] ret;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(input.getBytes("UTF-8"));
            ret = md.digest();
        } catch(Exception e) { ret = empty256(); };
        
        return ret;
    }
    
    public static void generate(String secret) {
        byte[] sha = sha256(secret);
        
        StringBuilder output = new StringBuilder();
        
        output.append("public static byte[] variable = new byte[] {");
        for(int i = 0; i < 32; ++ i) {

            output.append(sha[i]);
            if(i != 31) {
                output.append(",");
            }
        }
        output.append("};");
        
        System.out.println(output.toString());
    }
}
