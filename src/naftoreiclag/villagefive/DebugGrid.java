/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import naftoreiclag.villagefive.util.ModelBuilder;

public class DebugGrid
{
    public static final int width = 64;
    public static final int height = 64;
    
    public Mesh evenCells;
    public Mesh oddCells;
    
    public void buildGeometry()
    {
        ModelBuilder mb = new ModelBuilder();
        ModelBuilder mb2 = new ModelBuilder();
        
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                ModelBuilder aa = mb;
                
                if(((x + z) & 1) == 1)
                {
                    aa = mb2;
                }
                
                aa.setAppendOrigin(x, 0.0f, z);
                
                aa.addQuad(0, 0, 0, Vector3f.UNIT_Y, x, x, 
                           1, 0, 0, Vector3f.UNIT_Y, x, x, 
                           1, 0, 1, Vector3f.UNIT_Y, x, x, 
                           0, 0, 1, Vector3f.UNIT_Y, x, x);
            }
        }
        
        evenCells = mb.bake();
        oddCells = mb2.bake();
    }
}
