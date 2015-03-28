/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import java.util.ArrayList;
import java.util.List;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class LuaUtils
{
    public static List<String> getStrings(LuaTable table)
    {
        List<String> strings = new ArrayList<String>();
        for(Varargs pair = table.next(LuaValue.NIL); !pair.isnil(2); pair = table.next(pair.arg(1)))
        {
            LuaValue value = pair.arg(2);
            strings.add(value.checkjstring());
        }
        return strings;
    }
    public static List<LuaValue> getValues(LuaTable table)
    {
        List<LuaValue> values = new ArrayList<LuaValue>();
        for(Varargs pair = table.next(LuaValue.NIL); !pair.isnil(2); pair = table.next(pair.arg(1)))
        {
            LuaValue value = pair.arg(2);
            values.add(value);
        }
        return values;
    }
}
