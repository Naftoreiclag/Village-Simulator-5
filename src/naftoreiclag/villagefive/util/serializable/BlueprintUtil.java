/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import naftoreiclag.villagefive.util.serializable.Blueprint.Room;
import naftoreiclag.villagefive.util.serializable.Blueprint.Vert;

public class BlueprintUtil
{
    public static Blueprint makeSimple(double width, double height)
    {
        Blueprint nu = new Blueprint();
        
        Vert a = nu.new Vert(0, 0);
        Vert b = nu.new Vert(width, 0);
        Vert c = nu.new Vert(width, height);
        Vert d = nu.new Vert(0, height);
        
        nu.verts.add(a);
        nu.verts.add(b);
        nu.verts.add(c);
        nu.verts.add(d);
        
        Room r = nu.new Room();
        r.vertPntrs.add(a);
        r.vertPntrs.add(b);
        r.vertPntrs.add(c);
        r.vertPntrs.add(d);
        nu.rooms.add(r);
        
        return nu;
    }

}
