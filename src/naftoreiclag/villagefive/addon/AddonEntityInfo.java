/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import org.luaj.vm2.LuaTable;

public class AddonEntityInfo {

    public AddonInfo parent;
    
    public String id;
    
    public String name;
    public String desc;
    
    public String model;
    public String material;
    
    public double radius;
    
    AddonEntityInfo(AddonInfo parent, LuaTable data)
    {
        this.parent = parent;
        
        id = data.get("id").checkjstring();
        
        name = data.get("name").optjstring(id);
        desc = data.get("description").optjstring(null);
        
        model = data.get("model").optjstring(null);
        radius = data.get("radius").checkdouble();
    }

}
