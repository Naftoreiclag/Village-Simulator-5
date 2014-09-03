/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.List;

public class Layer
{
    public double offX = 0;
    public double offZ = 0;
    
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private List<Line> lines = new ArrayList<Line>();
    
    public void addSeg(double x1, double z1, double x2, double z2)
    {
        /*     ^ normal
         *     |
         * 1 ----- 2
         */
        
        x1 += offX;
        z1 += offZ;
        
        x2 += offX;
        z2 += offZ;
        
        if(x1 == x2 && z1 == z2)
        {
            System.out.println("Huh?");
            return;
        }
        
        double normX = z2 - z1;
        double normZ = x1 - x2;
        
        int indice1 = -1;
        int indice2 = -1;
        
        for(int i = 0; i < vertices.size(); ++ i)
        {
            Vertex comp = vertices.get(i);
            
            if(comp.x == x1 && comp.z == z1)
            {
                comp.normX += normX;
                comp.normZ += normZ;
                
                indice1 = i;
            }
            if(comp.x == x2 && comp.z == z2)
            {
                comp.normX += normX;
                comp.normZ += normZ;
                
                indice2 = i;
            }
            
            if(indice1 != -1 && indice2 != -1)
            {
                break;
            }
        }
        
        if(indice1 == -1)
        {
            indice1 = vertices.size();
            vertices.add(new Vertex(x1, z1, normX, normZ));
        }
        if(indice2 == -1)
        {
            indice2 = vertices.size();
            vertices.add(new Vertex(x2, z2, normX, normZ));
        }
        
        lines.add(new Line(indice1, indice2));
    }

    public Mesh bake()
    {
        return null;
    }
    
    // struct
    public static class Line
    {
        public int indice1;
        public int indice2;
        
        public Line(int indice1, int indice2)
        {
            this.indice1 = indice1;
            this.indice2 = indice2;
        }
    }
    
    // struct
    public static class Vertex
    {
        public double x;
        public double z;
        
        // Unnormalized vector perpendicular to its direction
        public double normX;
        public double normZ;
        
        public Vertex(double x, double z, double normX, double normZ)
        {
            this.x = x;
            this.z = z;
            this.normX = x;
            this.normZ = z;
        }
    }
}
