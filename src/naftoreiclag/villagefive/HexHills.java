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
import javax.imageio.ImageIO;
import naftoreiclag.villagefive.util.Layer;
import naftoreiclag.villagefive.util.ModelBuilder;

public class HexHills
{
    public static final int width = 25;
    public static final int height = 25;
    
    public static final float thickness = 0.25f;
    
    public static final float woodif = 10.0f;
    public static final float heetah = 17.5f;
    
    public static final float[] xoffs = {0.0f, woodif, woodif * 3.0f, woodif * 4.0f, woodif * 3.0f, woodif, 0.0f};
    public static final float[] zoffs = {heetah, 0.0f, 0.0f, heetah, heetah * 2.0f, heetah * 2.0f};
    
    public static final int numLevels = 16;
    
    // Some kind of array to store floor decals
    
    public int[][] heights;
    
    public Mesh[] mesh = new Mesh[numLevels];
    
    public Mesh mesh2;
    
    public HexHills()
    {
        loadDataFromFile();
        buildGeometry2();
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
    
    private void buildGeometry2()
    {
        for(int y = 0; y < numLevels; ++ y)
        {
            ModelBuilder mb = new ModelBuilder();
            for(int x = 0; x < width; ++ x)
            {
                for(int z = 0; z < height; ++ z)
                {
                    if(access(x, z) > y)
                    {
                        continue;
                    }
                    
                    mb.setAppendOrigin(x * woodif * 3, 0, (((x & 1) == 0) ? 0 : heetah) + (z * heetah * 2));
                    
                    if(access(x, z) == y)
                    {
                        mb.addHexagon(
                                xoffs[0], 0.0f, zoffs[0], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                xoffs[1], 0.0f, zoffs[1], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                xoffs[2], 0.0f, zoffs[2], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                xoffs[3], 0.0f, zoffs[3], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                xoffs[4], 0.0f, zoffs[4], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                xoffs[5], 0.0f, zoffs[5], Vector3f.UNIT_Y, 0.0f, 0.0f
                                
                                );
                    }
                    
                    boolean[] neighbor;

                    // Even
                    if((x & 1) == 0)
                    {
                        neighbor = new boolean[]{
                            access(x - 1, z - 1) > y,
                            access(x    , z - 1) > y,
                            access(x + 1, z - 1) > y,
                            access(x + 1, z    ) > y,
                            access(x    , z + 1) > y,
                            access(x - 1, z    ) > y};
                    }

                    // Odd
                    else
                    {
                        neighbor = new boolean[]{
                            access(x - 1, z    ) > y,
                            access(x    , z - 1) > y,
                            access(x + 1, z    ) > y,
                            access(x + 1, z + 1) > y,
                            access(x    , z + 1) > y,
                            access(x - 1, z + 1) > y};
                    }
                    
                    
                    for(int i = 0; i < 6; ++ i)
                    {
                        int ip = (i + 1) % 6;
                        int ipp = (i + 2) % 6;
                        int im = i - 1;
                        
                        if(im == -1)
                        {
                            im = 5;
                        }
                        
                        if(neighbor[i])
                        {
                            if(neighbor[ip])
                            {
                                mb.addQuad(
                                        xoffs[i], thickness, zoffs[i], Vector3f.ZERO, 0.0f, 0.0f, 
                                        xoffs[ipp], thickness, zoffs[ipp], Vector3f.ZERO, 1.0f, 0.0f, 
                                        xoffs[ipp], 0.0f, zoffs[ipp], Vector3f.ZERO, 1.0f, 1.0f, 
                                        xoffs[i], 0.0f, zoffs[i], Vector3f.ZERO, 0.0f, 1.0f);
                                
                                
                                mb.addTriangle(
                                        xoffs[i], thickness, zoffs[i], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                        xoffs[ip], thickness, zoffs[ip], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                        xoffs[ipp], thickness, zoffs[ipp], Vector3f.UNIT_Y, 0.0f, 0.0f

                                        );
                            }
                            else
                            {
                                if(!neighbor[im])
                                {
                                    mb.addQuad(
                                            xoffs[i], thickness, zoffs[i], Vector3f.ZERO, 0.0f, 0.0f, 
                                            xoffs[ip], thickness, zoffs[ip], Vector3f.ZERO, 1.0f, 0.0f, 
                                            xoffs[ip], 0.0f, zoffs[ip], Vector3f.ZERO, 1.0f, 1.0f, 
                                            xoffs[i], 0.0f, zoffs[i], Vector3f.ZERO, 0.0f, 1.0f);
                                }
                            }
                        }
                    }
                }
            }
            
            mesh[y] = mb.bake(0.02f, 1.0f, 0.02f);
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
