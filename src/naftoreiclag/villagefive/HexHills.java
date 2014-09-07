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
    
    public static final float fwoodif = woodif * 4;
    public static final float fheetah = heetah * 2;
    
    public static final float[] xoffs2 = {xoffs[0] / fwoodif, xoffs[1] / fwoodif, xoffs[2] / fwoodif, xoffs[3] / fwoodif, xoffs[4] / fwoodif, xoffs[5] / fwoodif};
    public static final float[] zoffs2 = {zoffs[0] / fheetah, zoffs[1] / fheetah, zoffs[2] / fheetah, zoffs[3] / fheetah, zoffs[4] / fheetah, zoffs[5] / fheetah};
    
    public static final int numLevels = 16;
    
    // Some kind of array to store floor decals
    
    public int[][] heights;
    
    public Mesh[] mesh = new Mesh[numLevels];
    public Mesh[] mesh2 = new Mesh[numLevels];
    
    
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
            Layer layer = new Layer();
            ModelBuilder mb2 = new ModelBuilder();
            for(int x = 0; x < width; ++ x)
            {
                for(int z = 0; z < height; ++ z)
                {
                    if(access(x, z) > y)
                    {
                        continue;
                    }
                    
                    layer.offX = x * woodif * 3;
                    layer.offZ = (((x & 1) == 0) ? 0 : heetah) + (z * heetah * 2);
                    mb2.setAppendOrigin(x * woodif * 3, 0, (((x & 1) == 0) ? 0 : heetah) + (z * heetah * 2));
                    
                    if(access(x, z) == y)
                    {
                        mb2.addHexagon(
                                xoffs[0], 0.0f, zoffs[0], Vector3f.UNIT_Y, xoffs2[0], zoffs2[0],
                                xoffs[1], 0.0f, zoffs[1], Vector3f.UNIT_Y, xoffs2[1], zoffs2[1],
                                xoffs[2], 0.0f, zoffs[2], Vector3f.UNIT_Y, xoffs2[2], zoffs2[2],
                                xoffs[3], 0.0f, zoffs[3], Vector3f.UNIT_Y, xoffs2[3], zoffs2[3],
                                xoffs[4], 0.0f, zoffs[4], Vector3f.UNIT_Y, xoffs2[4], zoffs2[4],
                                xoffs[5], 0.0f, zoffs[5], Vector3f.UNIT_Y, xoffs2[5], zoffs2[5]
                                
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
                                layer.addSeg(xoffs[i], zoffs[i], xoffs[ipp], zoffs[ipp]);
                                
                                mb2.addTriangle(
                                        xoffs[i], thickness, zoffs[i], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                        xoffs[ip], thickness, zoffs[ip], Vector3f.UNIT_Y, 0.0f, 0.0f,
                                        xoffs[ipp], thickness, zoffs[ipp], Vector3f.UNIT_Y, 0.0f, 0.0f
                                        );
                            }
                            else
                            {
                                if(!neighbor[im])
                                {
                                    layer.addSeg(xoffs[i], zoffs[i], xoffs[ip], zoffs[ip]);
                                }
                            }
                        }
                    }
                }
            }
            
            mesh[y] = layer.bake(0.02f, thickness, 0.02f);
            mesh2[y] = mb2.bake(0.02f, 1.0f, 0.02f);
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
}
