/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import com.jme3.math.ColorRGBA;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

public final class LuaColor {
    private LuaColor(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public final double r;
    public final double g;
    public final double b;

    static LuaColor create(LuaValue data) {
        if(data.isnil()) {
            return null;
        }

        double r = 0;
        double g = 0;
        double b = 0;

        try {
            r = data.get(1).checkdouble();
        } catch(LuaError _) {
        }
        try {
            g = data.get(2).checkdouble();
        } catch(LuaError _) {
        }
        try {
            b = data.get(3).checkdouble();
        } catch(LuaError _) {
        }

        return new LuaColor(r, g, b);
    }

    public ColorRGBA toColor() {
        return new ColorRGBA((float) r, (float) g, (float) b, 1.0f);
    }
}
