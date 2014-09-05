/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.scene.Mesh;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static naftoreiclag.villagefive.Hills.height;
import static naftoreiclag.villagefive.Hills.width;
import naftoreiclag.villagefive.util.Layer;
import naftoreiclag.villagefive.util.ModelBuilder;

public class NewHills
{
    public static final int width = 25;
    public static final int height = 25;
    
    public static final float thickness = 0.25f;
    
    public static final int numLevels = 16;
    
    // Some kind of array to store floor decals
    
    public int[][] heights;
    
    public Mesh[] mesh = new Mesh[numLevels];
    
    public NewHills()
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
                heights[x][z] = (img.getRGB(x, z) & 0x000000F0) / 16;
            }
        }
    }

    private void buildGeometry()
    {
        
        for(int y = 0; y < numLevels; ++ y)
        {
            Layer mb = new Layer();
            for(int x = 0; x < width - 1; ++ x)
            {
                for(int z = 0; z < height - 1; ++ z)
                {
                    addGeo(mb, x, y, z);
                }
            }
            mesh[y] = mb.bake(thickness);
        }
        
    }
    
    public int access(int x, int z)
    {
        if(x < 0 || z < 0 || x >= width || z >= width)
        {
            return 0;
        }
        
        return heights[x][z];
    }

    private void addGeo(Layer mb, int x, int y, int z)
    {
        if(access(x, z) >= y)
        {
            return;
        }
        
        int type = 0;
        if(access(x - 1, z) >= y)
        {
            type += 1;
        }
        if(access(x, z - 1) >= y)
        {
            type += 2;
        }
        if(access(x + 1, z) >= y)
        {
            type += 4;
        }
        if(access(x, z + 1) >= y)
        {
            type += 8;
        }
        
        mb.offX = x;
        mb.offZ = z;
        
        if(type == 12)
        {
            mb.addSeg(0.0f, 1.0f, 1.0f, 0.0f);
        }
        else if(type == 9)
        {
            mb.addSeg(0.0f, 0.0f, 1.0f, 1.0f);
        }
        else if(type == 3)
        {
            mb.addSeg(1.0f, 0.0f, 0.0f, 1.0f);
        }
        else if(type == 6)
        {
            mb.addSeg(1.0f, 1.0f, 0.0f, 0.0f);
        }
        else if(type == 14)
        {
            mb.addSeg(0.0f, 1.0f, 0.25f, 0.75f);
            mb.addSeg(0.25f, 0.75f, 0.25f, 0.25f);
            mb.addSeg(0.25f, 0.25f, 0.0f, 0.0f);
        }
        else if(type == 13)
        {
            mb.addSeg(1.0f, 0.0f, 0.75f, 0.25f);
            mb.addSeg(0.75f, 0.25f, 0.25f, 0.25f);
            mb.addSeg(0.25f, 0.25f, 0.0f, 0.0f);
        }
        else if(type == 11)
        {
            mb.addSeg(1.0f, 0.0f, 0.75f, 0.25f);
            mb.addSeg(0.75f, 0.25f, 0.75f, 0.75f);
            mb.addSeg(0.75f, 0.75f, 1.0f, 1.0f);
        }
        else if(type == 7)
        {
            mb.addSeg(1.0f, 1.0f, 0.75f, 0.75f);
            mb.addSeg(0.75f, 0.75f, 0.25f, 0.75f);
            mb.addSeg(0.25f, 0.75f, 0.0f, 1.0f);
        }
        if(type == 8 || type == 10)
        {
            mb.addSeg(0.0f, 1.0f, 1.0f, 1.0f);
        }
        if(type == 2 || type == 10)
        {
            mb.addSeg(1.0f, 0.0f, 0.0f, 0.0f);
        }
        if(type == 1 || type == 5)
        {
            mb.addSeg(0.0f, 0.0f, 0.0f, 1.0f);
        }
        if(type == 4 || type == 5)
        {
            mb.addSeg(1.0f, 1.0f, 1.0f, 0.0f);
        }
    }
}
