/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import org.luaj.vm2.LuaValue;

public class LuaModel {

    public String meshFile;
    public LuaMaterial materialOverride;
    
    public final String dir;
    
    public LuaModel(String dir, LuaValue data)
    {
        this.dir = dir;
        meshFile = data.get("meshFile").checkjstring();
        materialOverride = new LuaMaterial(dir, data.get("material"));
    }

}
