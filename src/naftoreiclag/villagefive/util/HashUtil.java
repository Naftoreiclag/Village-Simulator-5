/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {
        
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
    
    public static byte[] empty(int length) {
        byte[] ret = new byte[length];
        for(int i = 0; i < length; ++ i) {
            ret[i] = 0;
        }
        
        return ret;
    }
    
    public static byte[] hash32(String input) {
        
        byte[] ret;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(input.getBytes("UTF-8"));
            ret = md.digest();
        } catch(Exception e) { ret = empty(32); };
        
        return ret;
    }
    
    public static byte[] hash64(String input) {
        
        byte[] ret;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            md.update(input.getBytes("UTF-8"));
            ret = md.digest();
        } catch(Exception e) { ret = empty(64); };
        
        return ret;
    }
    public static byte[] hash48(String input) {
        byte[] sixtyFour = hash64(input);
        byte[] ret = new byte[48];
        
        for(int i = 0; i < 48; ++ i) {
            ret[i] = sixtyFour[i];
        }
        
        return ret;
    }
    public static byte[] hash24(String input) {
        byte[] thirtyTwo = hash32(input);
        byte[] ret = new byte[24];
        
        for(int i = 0; i < 24; ++ i) {
            ret[i] = thirtyTwo[i];
        }
        
        return ret;
    }
    
    public static void generate(String secret) {
        getJavaCodeLol(hash32(secret));
    }
    
    public static void getJavaCodeLol(byte[] code) {
        
        StringBuilder output = new StringBuilder();
        
        output.append("public static byte[] variable = new byte[] {");
        for(int i = 0; i < code.length; ++ i) {

            output.append(code[i]);
            if(i != code.length - 1) {
                output.append(",");
            }
        }
        output.append("};");
        
        System.out.println(output.toString());
        
    }
    
    public static byte[] pin_number = new byte[] {-20,126,60,-117,-122,-24,11,40,-28,-20,86,-47,-24,-27,-65,-13,-51,-110,78,-46,-25,24,6,-123,-31,-128,46,1,-124,-86,45,46};

}
