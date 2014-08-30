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
        
        Vector3f NW = new Vector3f( 1, 0,  1).normalizeLocal();
        Vector3f NE = new Vector3f(-1, 0,  1).normalizeLocal();
        Vector3f SE = new Vector3f(-1, 0, -1).normalizeLocal();
        Vector3f SW = new Vector3f( 1, 0, -1).normalizeLocal();
        
        Vector3f N = new Vector3f( 0, 0,  1);
        Vector3f E = new Vector3f(-1, 0,  0);
        Vector3f S = new Vector3f( 0, 0, -1);
        Vector3f W = new Vector3f( 1, 0,  0);
        
        mb.setAppendOrigin(offX, offY, offZ);
        if(type == 1)
        {
            // x .
            // . .

            // NW corner

            mb.addQuad(
                    0, 0, bar, NW, 0, 0, 
                    bar, 0, 0, NW, bar, 0, 
                    bar, 1, 0, NW, bar, 1, 
                    0, 1, bar, NW, 0, 1);


        }
        else if(type == 2)
        {
            // . x
            // . .

            // NE corner

            mb.addQuad(
                    foo, 0, 0, NE, foo, 0,
                    1, 0, bar, NE, 1, 0,
                    1, 1, bar, NE, 1, 1,
                    foo, 1, 0, NE, foo, 1);


        }
        else if(type == 4)
        {
            // . .
            // . x

            // SE corner

            mb.addQuad(
                    1, 0, foo, SE, 1, 0, 
                    foo, 0, 1, SE, foo, 0, 
                    foo, 1, 1, SE, foo, 1, 
                    1, 1, foo, SE, 1, 1);


        }
        else if(type == 8)
        {
            // . .
            // x .

            // SW corner

            mb.addQuad(
                    bar, 0, 1, SW, bar, 0, 
                    0, 0, foo, SW, 0, 0, 
                    0, 1, foo, SW, 0, 1,
                    bar, 1, 1, SW, bar, 1);


        }
        else if(type == 3)
        {
            // x x
            // . .

            // N slab

            mb.addQuad(
                    0, 0, bar, N, 0, 0, 
                    1, 0, bar, N, 1, 0, 
                    1, 1, bar, N, 1, 1, 
                    0, 1, bar, N, 0, 1);


        }
        else if(type == 6)
        {
            // . x
            // . x

            // E slab

            mb.addQuad(
                    foo, 0, 0, E, 0, 0, 
                    foo, 0, 1, E, 0, 0, 
                    foo, 1, 1, E, 0, 1, 
                    foo, 1, 0, E, 0, 1);


        }
        else if(type == 12)
        {
            // . .
            // x x

            // S slab

            mb.addQuad(
                    1, 0, foo, S, 1, 0, 
                    0, 0, foo, S, 0, 0, 
                    0, 1, foo, S, 0, 1, 
                    1, 1, foo, S, 1, 1);


        }
        else if(type == 9)
        {
            // x .
            // x .


            // W slab
            mb.addQuad(
                    bar, 0, 1, W, 0, 0, 
                    bar, 0, 0, W, 0, 0, 
                    bar, 1, 0, W, 0, 1, 
                    bar, 1, 1, W, 0, 1);


        }
        if(type == 11 || type == 10)
        {
            // x x
            // x .

            // NW wedge

            mb.addQuad(
                    bar, 0, 1, NW, bar, 0, 
                    1, 0, bar, NW, 1, 0, 
                    1, 1, bar, NW, 1, 1,
                    bar, 1, 1, NW, bar, 1);


        }
        if(type == 7 || type == 5)
        {
            // x x
            // . x

            // NE slab

            mb.addQuad(
                    0, 0, bar, NE, 0, 0, 
                    foo, 0, 1, NE, foo, 0, 
                    foo, 1, 1, NE, foo, 1,
                    0, 1, bar, NE, 0, 1);


        }
        if(type == 14 || type == 10)
        {
            // . x
            // x x

            // SE wedge

            mb.addQuad(
                    foo, 0, 0, SE, foo, 0, 
                    0, 0, foo, SE, 0, 0, 
                    0, 1, foo, SE, 0, 1,
                    foo, 1, 0, SE, foo, 1);


        }
        if(type == 13 || type == 5)
        {
            // x .
            // x x

            mb.addQuad(
                    1, 0, foo, SW, 1, 0, 
                    bar, 0, 0, SW, bar, 0, 
                    bar, 1, 0, SW, bar, 1,
                    1, 1, foo, SW, 1, 1);

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
