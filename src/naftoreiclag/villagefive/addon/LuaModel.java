/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import org.luaj.vm2.LuaValue;

public final class LuaModel {

    public String meshFile;
    public LuaMaterial materialOverride;
    
    public final String dir;
    
    private LuaModel(String dir, LuaValue data)
    {
        this.dir = dir;
        meshFile = data.get("meshFile").checkjstring();
        materialOverride = LuaMaterial.create(dir, data.get("material"));
    }
    
    public static LuaModel create(String dir, LuaValue data) {
        LuaModel ret;
        
        try {
            ret = new LuaModel(dir, data);
        } catch(Exception e) {
            ret = null;
        }
        
        return ret;
    }
}
