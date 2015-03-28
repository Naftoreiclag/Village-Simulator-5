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
        
        name = table.get("name").optjstring(id);
        desc = table.get("description").optjstring("No description available.");
        author = table.get("author").optjstring("Anonymous");
        
        try {
            LuaTable downgradeTable = table.get("downgrades").checktable();
            
            for(Varargs pair = downgradeTable.next(LuaValue.NIL); !pair.isnil(2); pair = downgradeTable.next(pair.arg(1)))
            {
                LuaValue value = pair.arg(2);
                
                downgrades.add(value.checkjstring());
            }
        }
        catch(LuaError error) {}
        
        System.out.println(this);
    }
    
    @Override
    public String toString()
    {
        return "Addon: [" + id + "|" + version + "]" + ",name=" + name + ",author=" + author;
    }

}
