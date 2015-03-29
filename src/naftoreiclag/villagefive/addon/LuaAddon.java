/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import java.util.ArrayList;
import java.util.List;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class LuaAddon
{
    public final String dir;
    
    public String id;
    public String version;
    
    public String name;
    public String desc;
    public String author;
    
    public List<String> downgrades = new ArrayList<String>();
    public List<LuaEntity> entities = new ArrayList<LuaEntity>();

    public LuaAddon(String dir, LuaTable data)
    {
        this.dir = dir;
        
        id = data.get("id").checkjstring();
        version = data.get("version").checkjstring();
        
        name = data.get("name").optjstring(id);
        desc = data.get("description").optjstring(null);
        author = data.get("author").optjstring(null);
        
        try
        {
            downgrades.addAll(LuaUtils.getStrings(data.get("downgrades").checktable()));
        }
        catch(LuaError error) {}
        
        List<LuaValue> entityDataList = LuaUtils.getValues(data.get("entities").checktable());
        for(int i = 0; i < entityDataList.size(); ++ i)
        {
            LuaTable entityData = null;
            try
            {
                entityData = entityDataList.get(i).checktable();
                
            }
            catch(LuaError error)
            {
                error.printStackTrace();
                System.out.println("error, entity is not table");
                // Entry is not a table
            }
            entities.add(new LuaEntity(this, entityData));
        }
    }
    
    @Override
    public String toString()
    {
        return "Addon: [" + id + "|" + version + "]" + ",name=" + name + ",author=" + author;
    }

}
