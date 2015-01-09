/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import naftoreiclag.villagefive.util.serializable.Blueprint;

public class OreDict
{
    public static Vec2 JmeAngleToVec2(float angle)
    {
        return new Vec2(FastMath.cos(FastMath.HALF_PI - angle), FastMath.sin(FastMath.HALF_PI - angle));
    }
    
    public static Vector3f JmeAngleToVec3(float angle)
    {
        return new Vector3f(FastMath.cos(FastMath.HALF_PI - angle), 0f, FastMath.sin(FastMath.HALF_PI - angle));
    }
    
    public static Vector3f Vec2ToVec3(Vec2 vec)
    {
        return new Vector3f(vec.getXF(), 0f, vec.getYF());
    }

    public static Vec2 vec3ToVec2(Vector3f vec)
    {
        return new Vec2(vec.x, vec.z);
    }
    
    public static Polygon roomToPoly(Blueprint data, Blueprint.Face room)
    {
        // Create a new polygon to represent it
        Polygon polygon = new Polygon();
        // Copy over the vertex data
        for(int i = 0; i < room.getVerts().length; ++ i)
        {
            // Get the vertex by its id
            Blueprint.Vert vert = data.getVerts()[room.getVerts()[i]];
            
            // Copy it over
            polygon.vecs.add(new Vec2((float) vert.getX(), (float) vert.getZ()));
            
            // Copy over decal (hole) data
            for(Blueprint.Decal decal : data.getDecals())
            {
                // If this decal does not apply
                if(decal.getVertA() != vert.getId())
                {
                    // Skip it
                    continue;
                }
                
                Polygon.Hole jam = new Polygon.Hole();
                jam.point = decal.getVertA();
                jam.x = (float) decal.getDistance();
                jam.y = 0f;
                jam.h = 5f;
                jam.w = (float) decal.width;

                ArrayList<Polygon.Hole> h = new ArrayList<Polygon.Hole>();
                h.add(jam);

                // fix dis
                polygon.holesPerEdge.put(i, h);
            }
        }
        return polygon;
    }
}
