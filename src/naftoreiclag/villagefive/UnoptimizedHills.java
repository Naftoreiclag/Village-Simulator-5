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
    
    float vertu = 1.0f;
    float horzu = 0.25f;
    public void updateVertex(int x, int z)
    {
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
        for(int x = 0; x < width - 1; ++ x)
        {
            for(int z = 0; z < height - 1; ++ z)
            {
                // 1   2
                //
                // 4   3
                
                float h1 = access(x    , z    );
                float h2 = access(x + 1, z    );
                float h3 = access(x + 1, z + 1);
                float h4 = access(x    , z + 1);
                
                float smallest = smallest(h1, h2, h3, h4);
                
                mb.setAppendOrigin(x, smallest, z);
                
                h1 -= smallest;
                h2 -= smallest;
                h3 -= smallest;
                h4 -= smallest;
                
                if(h1 > 1)
                {
                    h1 = 1;
                }
                if(h2 > 1)
                {
                    h2 = 1;
                }
                if(h3 > 1)
                {
                    h3 = 1;
                }
                if(h4 > 1)
                {
                    h4 = 1;
                }
                
                mb.addQuad(
                        0, h1, 0, Vector3f.ZERO, vertu, vertu, 
                        1, h2, 0, Vector3f.ZERO, vertu, vertu, 
                        1, h3, 1, Vector3f.ZERO, vertu, vertu, 
                        0, h4, 1, Vector3f.ZERO, vertu, vertu);
            }
        }
        
        mesh = mb.bake(vertu, horzu, vertu);
    }
    
    private float smallest(float a, float b, float c, float d)
    {
        return a<b?(a<c?(a<d?a:d):(c<d?c:d)):(b<c?(b<d?b:d):(c<d?c:d));
    }
}
