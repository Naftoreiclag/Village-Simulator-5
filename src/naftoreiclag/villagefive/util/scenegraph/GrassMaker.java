/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;

public class GrassMaker
{
    public static final float[] HEX_X = new float[6];
    public static final float[] HEX_Z = new float[6];
    public static final double THIRD_TAU = Math.PI / 3d;
    
    public static float[] grassnormals = new float[]
    {
        0f, 1f, 0f,
        0f, 1f, 0f,
        0f, 1f, 0f,
        0f, 1f, 0f
    };
    
    static
    {
        for(int i = 0; i < 6; ++ i)
        {
            HEX_X[i] = (float) (Math.cos(THIRD_TAU * i));
            HEX_Z[i] = (float) (Math.sin(THIRD_TAU * i));
        }
    }
    
    public static Mesh makeGrass(double radius, int amount, double height, double grassRadius, boolean cullHack)
    {
        return makeGrass(radius, amount, height, grassRadius, cullHack, new Random());
    }
    
    public static Mesh makeGrass2(double radius, int amount, double height, double grassRadius, boolean cullHack, Random seed)
    {
        
        // 3 vertexes, 3 floats each
        FloatBuffer v = BufferUtils.createFloatBuffer(amount * 3 * 3);
        FloatBuffer n = BufferUtils.createFloatBuffer(amount * 3 * 3);
        FloatBuffer t = BufferUtils.createFloatBuffer(amount * 3 * 2);
        IntBuffer indicies;
        
        if(cullHack)
        {
            // 2 triangles, 3 floats each
            indicies = BufferUtils.createIntBuffer(amount * 2 * 3);
        }
        else
        {
            indicies = BufferUtils.createIntBuffer(amount * 1 * 3);
        }
        
        /*
         *  really bad hexagon ascii art
         * 
         *   5    6
         * 
         * 4         1
         *       
         *   3    2
         */
        
        int currentIndex = 0;
        
        for(int i = 0; i < amount; ++ i)
        {
            //http://stackoverflow.com/questions/5837572/generate-a-random-point-within-a-circle-uniformly
            double angle = seed.nextDouble() * Math.PI * 2d;
            double distance = seed.nextDouble() + seed.nextDouble();
            if(distance > 1)
            {
                distance = 2 - distance;
            }
            distance *= radius;
            
            float x = (float) (Math.cos(angle) * distance);
            float z = (float) (Math.sin(angle) * distance);
            
            double direction = seed.nextDouble() * Math.PI * 2d;
            
            int b = seed.nextInt(3);
            float cos = (float) (HEX_X[b] * grassRadius);
            float sin = (float) (HEX_Z[b] * grassRadius);
            
            float moss = -sin;
            float min = cos;
            
            /*
             *  1-----2
             *   \   /
             *    \ /
             *     0
             */
            
            float heightf = (float) height;
            
            v.put(x).put(-heightf).put(z);
            v.put(x - cos + moss).put(heightf).put(z - sin + min);
            v.put(x + cos + moss).put(heightf).put(z + sin + min);

            n.put(0).put(1).put(0);
            n.put(0).put(1).put(0);
            n.put(0).put(1).put(0);

            t.put(0.5f).put(1);
            t.put(0).put(0);
            t.put(1).put(0);

            indicies.put(currentIndex    ).put(currentIndex + 1).put(currentIndex + 2);

            currentIndex += 3;

            if(cullHack)
            {
                indicies.put(currentIndex + 1).put(currentIndex    ).put(currentIndex + 2);
            }
            
        }
                
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, n);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, indicies);

        mesh.updateBound();
                
        return mesh;
    }

    public static Mesh makeGrass(double radius, int amount, double height, double grassRadius, boolean cullHack, Random seed)
    {
        FloatBuffer v = BufferUtils.createFloatBuffer(amount * 36);
        FloatBuffer n = BufferUtils.createFloatBuffer(amount * 36);
        FloatBuffer t = BufferUtils.createFloatBuffer(amount * 24);
        IntBuffer indicies;
        
        if(cullHack)
        {
            indicies = BufferUtils.createIntBuffer(amount * 36);
        }
        else
        {
            indicies = BufferUtils.createIntBuffer(amount * 18);
        }
        
        /*
         *  really bad hexagon ascii art
         * 
         *   5    6
         * 
         * 4         1
         *       
         *   3    2
         */
        
        float[] hexX = new float[6];
        float[] hexZ = new float[6];
        
        for(int i = 0; i < 6; ++ i)
        {
            hexX[i] = (float) (HEX_X[i] * grassRadius);
            hexZ[i] = (float) (HEX_Z[i] * grassRadius);
        }
        
        int currentIndex = 0;
        
        for(int i = 0; i < amount; ++ i)
        {
            //http://stackoverflow.com/questions/5837572/generate-a-random-point-within-a-circle-uniformly
            double angle = seed.nextDouble() * Math.PI * 2d;
            double distance = seed.nextDouble() + seed.nextDouble();
            if(distance > 1)
            {
                distance = 2 - distance;
            }
            distance *= radius;
            
            float x = (float) (Math.cos(angle) * distance);
            float z = (float) (Math.sin(angle) * distance);
            
            
            /*
             *  1-----2
             *  |     |
             *  |     |
             *  0-----3
             */
            
            float heightf = (float) height;
            
            for(int j = 0; j < 3; ++ j)
            {
                v.put(x - hexX[j]).put(0f).put(z - hexZ[j]);
                v.put(x - hexX[j]).put(heightf).put(z - hexZ[j]);
                v.put(x + hexX[j]).put(heightf).put(z + hexZ[j]);
                v.put(x + hexX[j]).put(0f).put(z + hexZ[j]);
                
                n.put(grassnormals);
                
                t.put(0).put(1);
                t.put(0).put(0);
                t.put(1).put(0);
                t.put(1).put(1);
                
                indicies.put(currentIndex    ).put(currentIndex + 1).put(currentIndex + 3);
                indicies.put(currentIndex + 1).put(currentIndex + 2).put(currentIndex + 3);
                
                currentIndex += 4;
                
                if(cullHack)
                {
                    indicies.put(currentIndex + 1).put(currentIndex    ).put(currentIndex + 3);
                    indicies.put(currentIndex + 2).put(currentIndex + 1).put(currentIndex + 3);
                }
                
            }
            
        }
                
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, n);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, indicies);

        mesh.updateBound();
                
        return mesh;
    }
}
