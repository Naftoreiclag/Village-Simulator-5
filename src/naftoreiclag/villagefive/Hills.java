/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.Random;
import naftoreiclag.villagefive.util.ModelBuilder;


public class Hills
{
    public static final int width = 25;
    public static final int height = 25;
    
    public static final int numLevels = 5;
    
    public int[][] heights;
    
    public Mesh mesh;
    
    public Hills()
    {
        buildGeometry();
    }
    
    public void buildGeometry()
    {
        Random r = new Random(1337);
        
        heights = new int[width][height];
        for(int x = 0; x < width; ++ x)
        {
            for(int y = 0; y < height; ++ y)
            {
                heights[x][y] = r.nextFloat() > 0.8f ? 1 : 0;
            }
        }
        
        ModelBuilder mb = new ModelBuilder();
        
        int target = 1;
        //for(int i = 0; i < numLevels; ++ i)
        {
            for(int x = 0; x < width - 1; ++ x)
            {
                for(int y = 0; y < height - 1; ++ y)
                {
                    addGeo(mb, x, target, y, heights[x][y] >= target, heights[x + 1][y] >= target, heights[x + 1][y + 1] >= target, heights[x][y + 1] >= target);
                }
            }
        }
        
        mesh = mb.bake();
    }
    
    private void addGeo(ModelBuilder mb, int offX, int offY, int offZ, boolean nw, boolean ne, boolean se, boolean sw)
    {
        //int foo = 1;
        
        float bar = 0.25f;
        float foo = 1 - bar;
        
        int type = 0;
        
        if(nw)
        {
            type += 1;
        }
        if(ne)
        {
            type += 2;
        }
        if(se)
        {
            type += 4;
        }
        if(sw)
        {
            type += 8;
        }
        
        mb.setAppendOrigin(offX, offY, offZ);
        if(type == 1)
        {
            // x .
            // . .

            // NW corner

            mb.addQuad(
                    0, 0, bar, Vector3f.ZERO, type, type, 
                    bar, 0, 0, Vector3f.ZERO, type, type, 
                    bar, 1, 0, Vector3f.ZERO, type, type, 
                    0, 1, bar, Vector3f.ZERO, type, type);


        }
        else if(type == 2)
        {
            // . x
            // . .

            // NE corner

            mb.addQuad(
                    foo, 0, 0, Vector3f.ZERO, type, type, 
                    1, 0, bar, Vector3f.ZERO, type, type, 
                    1, 1, bar, Vector3f.ZERO, type, type, 
                    foo, 1, 0, Vector3f.ZERO, type, type);


        }
        else if(type == 4)
        {
            // . .
            // . x

            mb.addQuad(
                    1, 0, foo, Vector3f.ZERO, type, type, 
                    foo, 0, 1, Vector3f.ZERO, type, type, 
                    foo, 1, 1, Vector3f.ZERO, type, type, 
                    1, 1, foo, Vector3f.ZERO, type, type);

            // SE corner


        }
        else if(type == 8)
        {
            // . .
            // x .

            mb.addQuad(
                    bar, 0, 1, Vector3f.ZERO, type, type, 
                    0, 0, foo, Vector3f.ZERO, type, type, 
                    0, 1, foo, Vector3f.ZERO, type, type,
                    bar, 1, 1, Vector3f.ZERO, type, type);

            // SW corner


        }
        else if(type == 3)
        {
            // x x
            // . .

            // N slab

            mb.addQuad(
                    0, 0, bar, Vector3f.ZERO, type, type, 
                    1, 0, bar, Vector3f.ZERO, type, type, 
                    1, 1, bar, Vector3f.ZERO, type, type, 
                    0, 1, bar, Vector3f.ZERO, type, type);


        }
        else if(type == 6)
        {
            // . x
            // . x

            mb.addQuad(
                    foo, 0, 0, Vector3f.ZERO, type, type, 
                    foo, 0, 1, Vector3f.ZERO, type, type, 
                    foo, 1, 1, Vector3f.ZERO, type, type, 
                    foo, 1, 0, Vector3f.ZERO, type, type);

            // E slab


        }
        else if(type == 12)
        {
            // . .
            // x x

            mb.addQuad(
                    1, 0, foo, Vector3f.ZERO, type, type, 
                    0, 0, foo, Vector3f.ZERO, type, type, 
                    0, 1, foo, Vector3f.ZERO, type, type, 
                    1, 1, foo, Vector3f.ZERO, type, type);

            // S slab


        }
        else if(type == 9)
        {
            // x .
            // x .

            mb.addQuad(
                    bar, 0, 1, Vector3f.ZERO, type, type, 
                    bar, 0, 0, Vector3f.ZERO, type, type, 
                    bar, 1, 0, Vector3f.ZERO, type, type, 
                    bar, 1, 1, Vector3f.ZERO, type, type);

            // W slab


        }
        if(type == 11 || type == 10)
        {
            // x x
            // x .

            // NW wedge

            mb.addQuad(
                    bar, 0, 1, Vector3f.ZERO, type, type, 
                    1, 0, bar, Vector3f.ZERO, type, type, 
                    1, 1, bar, Vector3f.ZERO, type, type,
                    bar, 1, 1, Vector3f.ZERO, type, type);


        }
        if(type == 7 || type == 5)
        {
            // x x
            // . x

            mb.addQuad(
                    0, 0, bar, Vector3f.ZERO, type, type, 
                    foo, 0, 1, Vector3f.ZERO, type, type, 
                    foo, 1, 1, Vector3f.ZERO, type, type,
                    0, 1, bar, Vector3f.ZERO, type, type);

            // NE slab


        }
        if(type == 14 || type == 10)
        {
            // . x
            // x x

            mb.addQuad(
                    foo, 0, 0, Vector3f.ZERO, type, type, 
                    0, 0, foo, Vector3f.ZERO, type, type, 
                    0, 1, foo, Vector3f.ZERO, type, type,
                    foo, 1, 0, Vector3f.ZERO, type, type);

            // SE wedge


        }
        if(type == 13 || type == 5)
        {
            // x .
            // x x

            mb.addQuad(
                    1, 0, foo, Vector3f.ZERO, type, type, 
                    bar, 0, 0, Vector3f.ZERO, type, type, 
                    bar, 1, 0, Vector3f.ZERO, type, type,
                    1, 1, foo, Vector3f.ZERO, type, type);

            // SW wedge


        }
        if(type == 15)
        {
            // x x
            // x x

            // nothing?


        }
        
        /*
        mb.addQuad(
            offX, offY, offZ, Vector3f.UNIT_Y, 0, 0, 
            offX + 1, offY, offZ, Vector3f.UNIT_Y, 0, 0, 
            offX + 1, offY, offZ + 1, Vector3f.UNIT_Y, 0, 0, 
            offX, offY, offZ + 1, Vector3f.UNIT_Y, 0, 0);
        */
    }
}
