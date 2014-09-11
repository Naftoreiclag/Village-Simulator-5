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
import naftoreiclag.villagefive.util.ModelBuilder.Vertex;

public class TraditionalHills
{
    public static final int width = 25;
    public static final int height = 25;
    
    public static final int numLevels = 16;
    
    public float[][] map = new float[width][height];
    public Vector3f[][] mapNormals = new Vector3f[width][height];
    
    public Mesh mesh;
    
    public TraditionalHills()
    {
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                mapNormals[x][z] = new Vector3f();
            }
        }
        
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
        /*
         *     A                X ->
         *                    
         * C   M   D          Z
         *       P            |
         *     B   N          V
         *
         * M is the point defined by x, z
         */
        
        // Calculate height of these points ===
        
        float c = access(x - 1, z);
        float d = access(x + 1, z);
        float a = access(x, z - 1);
        float b = access(x, z + 1);
        
        // Calculate normals for M ===
        
        float cd_d = c - d;
        float ab_d = a - b;

        mapNormals[x][z].set(cd_d * vertu, 2 * horzu, ab_d * vertu);
        mapNormals[x][z].normalizeLocal();
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
                
                mb.addQuad(
                        x    , map[x    ][z    ], z    , mapNormals[x    ][z    ], 0, 0, 
                        x + 1, map[x + 1][z    ], z    , mapNormals[x + 1][z    ], 1, 0, 
                        x + 1, map[x + 1][z + 1], z + 1, mapNormals[x + 1][z + 1], 1, 1, 
                        x    , map[x    ][z + 1], z + 1, mapNormals[x    ][z + 1], 0, 1);
            }
        }
        
        mesh = mb.bake(vertu, horzu, vertu);
    }
}
