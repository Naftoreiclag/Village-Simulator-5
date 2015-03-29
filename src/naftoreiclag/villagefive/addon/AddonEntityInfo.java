/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import org.luaj.vm2.LuaTable;

public class AddonEntityInfo {

    public AddonInfo parent;
    public final String dir;
    
    public String id;
    
    public String name;
    public String desc;
    
    public LuaModel model;
    
    public double radius;
    
    AddonEntityInfo(AddonInfo parent, LuaTable data)
    {
        this.parent = parent;
        this.dir = parent.dir;
        
        id = data.get("id").checkjstring();
        
        name = data.get("name").optjstring(id);
        desc = data.get("description").optjstring(null);
        
        model = new LuaModel(dir, data.get("model"));
        radius = data.get("radius").checkdouble();
    }

}
