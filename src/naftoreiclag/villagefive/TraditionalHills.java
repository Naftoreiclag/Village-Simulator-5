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
    public static final int width = 64;
    public static final int height = 64;
    
    public static final int numLevels = 16;
    
    public float[][] map = new float[width][height];
    public Vector3f[][] mapNormals = new Vector3f[width][height];
    
    // Vertical distance between the farthest two vertexes of a trinagle
    public static final double maxSteep = 5;
    
    public Mesh mesh;
    public Mesh mesh2;
    
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
            img = ImageIO.read(new File("donotinclude/heightmap2.png"));
        } catch (IOException e)
        {
        }

        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                map[x][z] = (img.getRGB(x, z) & 0x000000FF) / 4f;
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
    
    float vertu = 0.50f; // 20 cm
    float horzu = 0.25f; // 10 cm
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
        ModelBuilder mb2 = new ModelBuilder();
        for(int x = 0; x < width - 1; ++ x)
        {
            for(int z = 0; z < height - 1; ++ z)
            {
                
                // M  0  D
                //
                // 2  P  3
                //
                // B  1  N
                
                int mxi = x;
                int mzi = z;
                int bxi = x;
                int bzi = z + 1;
                int nxi = x + 1;
                int nzi = z + 1;
                int dxi = x + 1;
                int dzi = z;
                
                float m = map[mxi][mzi];
                float b = map[bxi][bzi];
                float n = map[nxi][nzi];
                float d = map[dxi][dzi];
                
                double mnb = steepness(m, n, b);
                double bdn = steepness(b, d, n);
                double nmd = steepness(n, m, d);
                double dbm = steepness(d, b, m);
                
                double flat = smallest(mnb, bdn, nmd, dbm);
                
                Vertex vm = new Vertex(mxi, m, mzi, mapNormals[mxi][mzi], 0, 0);
                Vertex vd = new Vertex(dxi, d, dzi, mapNormals[dxi][dzi], 1, 0);
                Vertex vn = new Vertex(nxi, n, nzi, mapNormals[nxi][nzi], 1, 1);
                Vertex vb = new Vertex(bxi, b, bzi, mapNormals[bxi][bzi], 0, 1);
                
                if(mnb == flat || nmd == flat)
                {
                    if(mnb > maxSteep)
                    {
                        mb2.addTriangle(vm, vn, vb);
                    }
                    else
                    {
                        mb.addTriangle(vm, vn, vb);
                    }
                    if(nmd > maxSteep)
                    {
                        mb2.addTriangle(vn, vm, vd);
                    }
                    else
                    {
                        mb.addTriangle(vn, vm, vd);
                    }
                }
                else
                {
                    if(bdn > maxSteep)
                    {
                        mb2.addTriangle(vb, vd, vn);
                    }
                    else
                    {
                        mb.addTriangle(vb, vd, vn);
                    }
                    if(dbm > maxSteep)
                    {
                        mb2.addTriangle(vd, vb, vm);
                    }
                    else
                    {
                        mb.addTriangle(vd, vb, vm);
                    }
                }
            }
        }
        
        mesh = mb.bake(vertu, horzu, vertu);
        mesh2 = mb2.bake(vertu, horzu, vertu);
    }
    
    private double smallest(double a, double b, double c, double d)
    {
        return a<b?(a<c?(a<d?a:d):(c<d?c:d)):(b<c?(b<d?b:d):(c<d?c:d));
    }
    
    private float smallest(float a, float b, float c)
    {
        return a<b?(a<c?a:c):(b<c?b:c);
    }
    private float largest(float a, float b, float c)
    {
        return a>b?(a>c?a:c):(b>c?b:c);
    }
    
    private double steepness(float a, float b, float c)
    {
        return largest(a, b, c) - smallest(a, b, c);
    }
}
