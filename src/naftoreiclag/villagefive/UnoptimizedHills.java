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
import naftoreiclag.villagefive.util.ModelBuilder;

public class UnoptimizedHills
{
    public static final int width = 25;
    public static final int height = 25;
    
    public static final int numLevels = 16;
    
    public float[][] map = new float[width][height];
    boolean[][][] sides = new boolean[width][height][2];
    
    public Mesh mesh;
    
    public UnoptimizedHills()
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

        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                map[x][z] = (img.getRGB(x, z) & 0x000000F0) / 16;
                sides[x][z][0] = false;
                sides[x][z][1] = false;
            }
        }
    }
    
    public float access(int x, int z)
    {
        /*
        if(x < 0) { x = 0; }
        if(x > width) { x = width; }
        if(z < 0) { z = 0; }
        if(z > height) { z = height; }
        */
        
        if(x < 0 || x >= width || z < 0 || z >= height)
        {
            return 0.0f;
        }
        
        return map[x][z];
    }
    
    public void foobar(int x, int z, int side)
    {
        if(side == 2)
        {
            sides[x + 1][z][0] = !sides[x + 1][z][0];
        }
        else if(side == 3)
        {
            sides[x][z + 1][1] = !sides[x][z + 1][1];
        }
        else
        {
            sides[x][z][side] = !sides[x][z][side];
        }
    }
    
    public boolean quux(int x, int z, int side)
    {
        if(side == 2)
        {
            return sides[x + 1][z][0];
        }
        else if(side == 3)
        {
            return sides[x][z + 1][1];
        }
        else
        {
            return sides[x][z][side];
        }
    }
        
    
    float vertu = 1.0f;
    float horzu = 0.125f;
    public void updateVertex(int x, int z)
    {
    }
    
    private float smallest(float a, float b, float c, float d)
    {
        return a<b?(a<c?(a<d?a:d):(c<d?c:d)):(b<c?(b<d?b:d):(c<d?c:d));
    }
    
    public void buildGeometry()
    {
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                updateVertex(x, z);
            }
        }
        ModelBuilder mb = new ModelBuilder();
        
        float limit = 2;
        
        
        for(int x = 0; x < width - 1; ++ x)
        {
            for(int z = 0; z < height - 1; ++ z)
            {
                // 
                //       1
                //     0   1
                //   0   p   2
                //     3   2
                //       3
                //       
                
                //   B
                // A p C
                //   D
                
                float h0 = access(x    , z    );
                float h1 = access(x + 1, z    );
                float h2 = access(x + 1, z + 1);
                float h3 = access(x    , z + 1);
                
                float sp = smallest(h0, h1, h2, h3);
                
                h0 -= sp;
                h1 -= sp;
                h2 -= sp;
                h3 -= sp;
                
                if(h3 > limit || h0 > limit)
                {
                    foobar(x, z, 0);
                }
                if(h0 > limit || h1 > limit)
                {
                    foobar(x, z, 1);
                }
                if(h1 > limit || h2 > limit)
                {
                    foobar(x, z, 2);
                }
                if(h2 > limit || h3 > limit)
                {
                    foobar(x, z, 3);
                }
            }
        }
        
        /*
        for(int x = 0; x < width - 1; ++ x)
        {
            for(int z = 0; z < height - 1; ++ z)
            {
                // 
                //       b
                //     0   1
                //   a   p   c
                //     3   2
                //       d
                //       
                
                //   B
                // A p C
                //   D
                
                float h0 = access(x    , z    );
                float h1 = access(x + 1, z    );
                float h2 = access(x + 1, z + 1);
                float h3 = access(x    , z + 1);
                
                float sp = smallest(h0, h1, h2, h3);
                
                mb.setAppendOrigin(x, sp, z);
                
                h0 -= sp;
                h1 -= sp;
                h2 -= sp;
                h3 -= sp;
                
                float mh0 = h0 > limit ? limit : h0;
                float mh1 = h1 > limit ? limit : h1;
                float mh2 = h2 > limit ? limit : h2;
                float mh3 = h3 > limit ? limit : h3;
                
                mb.addQuad(
                        0, mh0, 0, Vector3f.ZERO, 0, 0, 
                        1, mh1, 0, Vector3f.ZERO, 1, 0, 
                        1, mh2, 1, Vector3f.ZERO, 1, 1, 
                        0, mh3, 1, Vector3f.ZERO, 0, 1);
                
                if(quux(x, z, 0))
                {
                    mb.addQuad(
                            0, h0, 0, Vector3f.ZERO, 0, 0, 
                            1, h1, 0, Vector3f.ZERO, 1, 0, 
                            1, mh1, 0, Vector3f.ZERO, 1, 1, 
                            0, mh0, 0, Vector3f.ZERO, 0, 1);
                }
                if(quux(x, z, 1))
                {
                    mb.addQuad(
                            1, h1, 0, Vector3f.ZERO, 0, 0, 
                            1, h2, 1, Vector3f.ZERO, 1, 0, 
                            1, mh2, 1, Vector3f.ZERO, 1, 1, 
                            1, mh1, 0, Vector3f.ZERO, 0, 1);
                }
                if(quux(x, z, 2))
                {
                    mb.addQuad(
                            1, h2, 1, Vector3f.ZERO, 0, 0, 
                            0, h3, 1, Vector3f.ZERO, 1, 0, 
                            0, mh3, 1, Vector3f.ZERO, 1, 1, 
                            1, mh2, 1, Vector3f.ZERO, 0, 1);
                }
                if(quux(x, z, 3))
                {
                    mb.addQuad(
                            0, h3, 1, Vector3f.ZERO, 0, 0, 
                            0, h0, 0, Vector3f.ZERO, 1, 0, 
                            0, mh0, 0, Vector3f.ZERO, 1, 1, 
                            0, mh3, 1, Vector3f.ZERO, 0, 1);
                }
            }
        }
        */
                
        for(int x = 0; x < width - 1; ++ x)
        {
            for(int z = 0; z < height - 1; ++ z)
            {
                //     6   7
                //       b
                // 5   0   1   8
                //   a   p   c
                // 4   3   2   9
                //       d
                //     B   A
                
                //   B
                // A p C
                //   D
                
                float h0 = access(x    , z    );
                float h1 = access(x + 1, z    );
                float h2 = access(x + 1, z + 1);
                float h3 = access(x    , z + 1);
                
                float sp = smallest(h0, h1, h2, h3);
                
                mb.setAppendOrigin(x, sp, z);
                
                h0 -= sp;
                h1 -= sp;
                h2 -= sp;
                h3 -= sp;
                
                float mh0 = h0 > limit ? limit : h0;
                float mh1 = h1 > limit ? limit : h1;
                float mh2 = h2 > limit ? limit : h2;
                float mh3 = h3 > limit ? limit : h3;
                
                mb.addQuad(
                        0, mh0, 0, Vector3f.ZERO, 0, 0, 
                        1, mh1, 0, Vector3f.ZERO, 1, 0, 
                        1, mh2, 1, Vector3f.ZERO, 1, 1, 
                        0, mh3, 1, Vector3f.ZERO, 0, 1);
                
                if(quux(x, z, 1))
                {
                    mb.addQuad(
                            0, h0, 0, Vector3f.ZERO, 0, 0, 
                            1, h1, 0, Vector3f.ZERO, 1, 0, 
                            1, mh1, 0, Vector3f.ZERO, 1, 1, 
                            0, mh0, 0, Vector3f.ZERO, 0, 1);
                }
                if(quux(x, z, 2))
                {
                    mb.addQuad(
                            1, h1, 0, Vector3f.ZERO, 0, 0, 
                            1, h2, 1, Vector3f.ZERO, 1, 0, 
                            1, mh2, 1, Vector3f.ZERO, 1, 1, 
                            1, mh1, 0, Vector3f.ZERO, 0, 1);
                }
                if(quux(x, z, 3))
                {
                    mb.addQuad(
                            1, h2, 1, Vector3f.ZERO, 0, 0, 
                            0, h3, 1, Vector3f.ZERO, 1, 0, 
                            0, mh3, 1, Vector3f.ZERO, 1, 1, 
                            1, mh2, 1, Vector3f.ZERO, 0, 1);
                }
                if(quux(x, z, 0))
                {
                    mb.addQuad(
                            0, h3, 1, Vector3f.ZERO, 0, 0, 
                            0, h0, 0, Vector3f.ZERO, 1, 0, 
                            0, mh0, 0, Vector3f.ZERO, 1, 1, 
                            0, mh3, 1, Vector3f.ZERO, 0, 1);
                }
            }
        }
        
        mesh = mb.bake(vertu, horzu, vertu);
    }
}
