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

public class Addon
{
    public String id;
    public String version;
    
    public String name;
    public String desc;
    public String author;
    
    public List<String> downgrades = new ArrayList<String>();

    public Addon(LuaTable table)
    {
        id = table.get("id").checkjstring();
        version = table.get("version").checkjstring();
        
        name = table.get("id").optjstring(id);
        desc = table.get("description").optjstring("No description available.");
        author = table.get("author").optjstring("Anonymous");
        
        try {
            LuaTable downgradeTable = table.get("downgrades").checktable();
            
            LuaValue value;
            for(int i = 1; !downgradeTable.get(i).isnil(); ++ i)
            {
                value = downgradeTable.get(i);
                
                downgrades.add(value.checkjstring());
            }
        }
        catch(LuaError error) {}
    }

}
