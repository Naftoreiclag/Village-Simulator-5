/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

public class CommandyString {
    
    private final String[] aa;
    
    public CommandyString(String[] args) {
        this.aa = args;
    }
    
    public String get(int index) {
        if(index >= 0 && index < aa.length) {
            return aa[index];
        } else {
            return "";
        }
    }
}
