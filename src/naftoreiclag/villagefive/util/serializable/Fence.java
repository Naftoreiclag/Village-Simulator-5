/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.math.Polygon;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;

public class Fence extends WallType
{
    double postSpacing = 5.0f;

    @Override
    public Spatial makeInside(Polygon poly)
    {
        ModelBuilder mb = new ModelBuilder();
        
        for(int i = 0; i < poly.size(); ++ i)
        {
            /*
             * 
             * 
             *  A----------B
             * 
             */
            
            Vec2 A = poly.get(i);
            Vec2 B = poly.get(i + 1);
            
            Vec2 AB = B.subtract(A);
            Vec2 ABn = AB.normalize();
            
            double len = AB.len();
            
            int numPosts = (new Long(Math.round(len / postSpacing))).intValue();
            
            double space = len / ((double) numPosts);
            
            // less than or equals
            for(int j = 0; j <= numPosts; ++ j)
            {
                Vec2 sp = A.add(ABn.mult(j * space));
                
                mb.addDebugCube(sp.toJmeVec3());
            }
        }
        
        return new Geometry("Outside", mb.bake());
    }

    @Override
    public Spatial makeOutside(Polygon polygon)
    {
        return null;
    }
}
