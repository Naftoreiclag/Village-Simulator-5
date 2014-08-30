/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import naftoreiclag.villagefive.util.ModelBuilder;


public class Hills
{
    public static final int width = 25;
    public static final int height = 25;
    
    public static final int numLevels = 5;
    
    public int[][] heights;
    
    public Mesh mesh;
    public Mesh mess;
    
    public Hills()
    {
        loadDataFromFile();
        buildGeometry();
    }
    public void loadDataFromFile()
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File("donotinclude/heightmap.png"));
        } catch (IOException e)
        {
        }

        heights = new int[width][height];
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                heights[x][z] = (img.getRGB(x, z) & 0x000000F0) >> 1;
            }
        }
    }
    
    public void buildGeometry()
    {
        
        ModelBuilder mb = new ModelBuilder();
        ModelBuilder mb2 = new ModelBuilder();
        
        mb2.combineNormals = false;
        
        //int target = 1;
        for(int target = 0; target < numLevels; ++ target)
        {
            for(int x = 0; x < width - 1; ++ x)
            {
                for(int y = 0; y < height - 1; ++ y)
                {
                    addGeo(mb, mb2, x, target, y, heights[x][y] >= target, heights[x + 1][y] >= target, heights[x + 1][y + 1] >= target, heights[x][y + 1] >= target);
                }
            }
        }
        
        mesh = mb.bake();
        mess = mb2.bake();
    }
    
    private void addGeo(ModelBuilder mb, ModelBuilder mb2, float offX, float offY, float offZ, boolean nw, boolean ne, boolean se, boolean sw)
    {
        //int foo = 1;
        
        float bar = 0.25f;
        float foo = 1 - bar;
        
        float thickness = -0.5f;
        
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
        
        Vector3f U = Vector3f.UNIT_Y;
        
        mb.setAppendOrigin(offX, offY * -thickness, offZ);
        mb2.setAppendOrigin(offX, offY * -thickness, offZ);
        if(type == 1)
        {
            // x .
            // . .

            // NW corner

            mb.addQuad(
                    0, 0, bar, NW, 0, 0, 
                    bar, 0, 0, NW, bar, 0, 
                    bar, thickness, 0, NW, bar, 1,
                    0, thickness, bar, NW, 0, 1);

            mb2.addTriangle(
                    0, 0, 0, U, 0, 0, 
                    bar, 0, 0, U, bar, 0, 
                    0, 0, bar, U, 0, bar);
        }
        else if(type == 2)
        {
            // . x
            // . .

            // NE corner

            mb.addQuad(
                    foo, 0, 0, NE, foo, 0,
                    1, 0, bar, NE, 1, 0,
                    1, thickness, bar, NE, 1, 1,
                    foo, thickness, 0, NE, foo, 1);

            mb2.addTriangle(
                    foo, 0, 0, U, 0, 0, 
                    1, 0, 0, U, bar, 0, 
                    1, 0, bar, U, 0, bar);


        }
        else if(type == 4)
        {
            // . .
            // . x

            // SE corner

            mb.addQuad(
                    1, 0, foo, SE, foo, 0, 
                    foo, 0, 1, SE, 1, 0, 
                    foo, thickness, 1, SE, 1, 1, 
                    1, thickness, foo, SE, foo, 1);

            mb2.addTriangle(
                    1, 0, 1, U, bar, 0, 
                    foo, 0, 1, U, 0, 0, 
                    1, 0, foo, U, 0, bar);


        }
        else if(type == 8)
        {
            // . .
            // x .

            // SW corner

            mb.addQuad(
                    bar, 0, 1, SW, 0, 0, 
                    0, 0, foo, SW, bar, 0, 
                    0, thickness, foo, SW, bar, 1,
                    bar, thickness, 1, SW, 0, 1);

            mb2.addTriangle(
                    bar, 0, 1, U, 0, 0, 
                    0, 0, 1, U, bar, 0, 
                    0, 0, foo, U, 0, bar);


        }
        else if(type == 3)
        {
            // x x
            // . .

            // N slab

            mb.addQuad(
                    0, 0, bar, N, 0, 0, 
                    1, 0, bar, N, 1, 0, 
                    1, thickness, bar, N, 1, 1, 
                    0, thickness, bar, N, 0, 1);

            mb2.addQuad(
                    0, 0, 0, U, 0, 0, 
                    1, 0, 0, U, bar, 0, 
                    1, 0, bar, U, 0, bar, 
                    0, 0, bar, U, 0, bar);


        }
        else if(type == 6)
        {
            // . x
            // . x

            // E slab

            mb.addQuad(
                    foo, 0, 0, E, 0, 0, 
                    foo, 0, 1, E, 1, 0, 
                    foo, thickness, 1, E, 1, 1, 
                    foo, thickness, 0, E, 0, 1);

            mb2.addQuad(
                    foo, 0, 0, U, 0, 0, 
                    1, 0, 0, U, bar, 0, 
                    1, 0, 1, U, 0, bar, 
                    foo, 0, 1, U, 0, bar);


        }
        else if(type == 12)
        {
            // . .
            // x x

            // S slab

            mb.addQuad(
                    1, 0, foo, S, 0, 0, 
                    0, 0, foo, S, 1, 0, 
                    0, thickness, foo, S, 1, 1, 
                    1, thickness, foo, S, 0, 1);

            mb2.addQuad(
                    1, 0, 1, U, bar, 0, 
                    0, 0, 1, U, 0, 0, 
                    0, 0, foo, U, 0, bar,
                    1, 0, foo, U, 0, bar);


        }
        else if(type == 9)
        {
            // x .
            // x .


            // W slab
            mb.addQuad(
                    bar, 0, 1, W, 0, 0, 
                    bar, 0, 0, W, 1, 0, 
                    bar, thickness, 0, W, 1, 1, 
                    bar, thickness, 1, W, 0, 1);

            mb2.addQuad(
                    0, 0, 0, U, bar, 0, 
                    bar, 0, 0, U, 0, 0, 
                    bar, 0, 1, U, 0, bar,
                    0, 0, 1, U, 0, bar);


        }
        else if(type == 11)
        {
            // x x
            // x .
            
            mb2.addPentagon(
                    0, 0, 0, U, bar, 0, 
                    1, 0, 0, U, 0, 0, 
                    1, 0, bar, U, 0, bar,
                    bar, 0, 1, U, 0, bar,
                    0, 0, 1, U, 0, bar);
        }
        else if(type == 7)
        {
            // x x
            // . x
            
            mb2.addPentagon(
                    1, 0, 1, U, 0, bar,
                    foo, 0, 1, U, 0, bar,
                    0, 0, bar, U, 0, bar, 
                    0, 0, 0, U, 0, 0,
                    1, 0, 0, U, bar, 0);
        }
        else if(type == 14)
        {
            // . x
            // x x
            
            mb2.addPentagon(
                    1, 0, 1, U, bar, 0, 
                    0, 0, 1, U, 0, 0, 
                    0, 0, foo, U, 0, bar,
                    foo, 0, 0, U, 0, bar,
                    1, 0, 0, U, 0, bar);
        }
        else if(type == 13)
        {
            // x .
            // x x
            
            mb2.addPentagon(
                    0, 0, 0, U, 0, bar,
                    bar, 0, 0, U, 0, bar,
                    1, 0, foo, U, 0, bar, 
                    1, 0, 1, U, 0, 0,
                    0, 0, 1, U, bar, 0);
        }
        else if(type == 10)
        {
            // . x
            // x .
            
            mb2.addHexagon(
                    foo, 0, 0, U, 0, bar,
                    1, 0, 0, U, 0, bar,
                    1, 0, bar, U, 0, bar, 
                    bar, 0, 1, U, 0, 0,
                    0, 0, 1, U, bar, 0,
                    0, 0, foo, U, 0, 0);
        }
        else if(type == 5)
        {
            // x .
            // . x
            
            mb2.addHexagon(
                    1, 0, foo, U, 0, 0,
                    1, 0, 1, U, bar, 0, 
                    foo, 0, 1, U, 0, 0,
                    0, 0, bar, U, 0, bar,
                    0, 0, 0, U, 0, bar,
                    bar, 0, 0, U, 0, bar);
        }
        
        if(type == 11 || type == 10)
        {
            // x x
            // x .

            // NW wedge

            mb.addQuad(
                    bar, 0, 1, NW, bar, 0, 
                    1, 0, bar, NW, 1, 0, 
                    1, thickness, bar, NW, 1, 1,
                    bar, thickness, 1, NW, bar, 1);
        }
        if(type == 7 || type == 5)
        {
            // x x
            // . x

            // NE slab

            mb.addQuad(
                    0, 0, bar, NE, 0, 0, 
                    foo, 0, 1, NE, foo, 0, 
                    foo, thickness, 1, NE, foo, 1,
                    0, thickness, bar, NE, 0, 1);


        }
        if(type == 14 || type == 10)
        {
            // . x
            // x x

            // SE wedge

            mb.addQuad(
                    foo, 0, 0, SE, 0, 0, 
                    0, 0, foo, SE, foo, 0, 
                    0, thickness, foo, SE, foo, 1,
                    foo, thickness, 0, SE, 0, 1);


        }
        if(type == 13 || type == 5)
        {
            // x .
            // x x

            mb.addQuad(
                    1, 0, foo, SW, bar, 0, 
                    bar, 0, 0, SW, 1, 0, 
                    bar, thickness, 0, SW, 1, 1,
                    1, thickness, foo, SW, bar, 1);

            // SW wedge


        }
        if(type == 15)
        {
            // x x
            // x x

            // nothing?

            mb2.addQuad(
                    0, 0, 0, U, bar, 0, 
                    1, 0, 0, U, 1, 0, 
                    1, 0, 1, U, 1, 1,
                    0, 0, 1, U, bar, 1);


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
