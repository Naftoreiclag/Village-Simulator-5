/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import com.jme3.math.ColorRGBA;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

public class LuaColor
{
    private final boolean nil;
    
    public double r = 0d;
    public double g = 0d;
    public double b = 0d;

    public LuaColor(LuaValue data) {
        if(data.isnil())
        {
            this.nil = true;
        }
        else
        {
            try {
                r = data.get(1).checkdouble();
            } catch (LuaError error) {}
            try {
                g = data.get(2).checkdouble();
            } catch (LuaError error) {}
            try {
                b = data.get(3).checkdouble();
            } catch (LuaError error) {}
            
            this.nil = false;
        }
    }

    public boolean isNil()
    {
        return nil;
    }
    
    public ColorRGBA toColor()
    {
        return new ColorRGBA((float) r, (float) g, (float) b, 1.0f);
    }
}
