/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaTest {
    public static void main(String[] args) {
        Globals globals = JsePlatform.standardGlobals();
        System.out.println(globals.get("PATH"));
        LuaValue chunk = globals.loadfile("addons/geometry/addon.lua");
        chunk.call();
        
    }
}
