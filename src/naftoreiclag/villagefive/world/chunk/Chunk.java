/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.chunk;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;
import naftoreiclag.villagefive.world.Mundane;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.World;
import org.dyn4j.dynamics.Body;

public class Chunk extends Mundane
{
    // 
    public static final int width = 25;
    public static final int height = 25;
    
    // Completely rendered grass
    public static final int HIGH_LOD = 3;
    
    // Only highest layer of grass is rendered
    public static final int MID_LOD = 2;
    
    // No grass
    public static final int LOW_LOD = 1;
    
    protected Node node;
    public int x;
    public int z;
    
    public int currentLOD = LOW_LOD;
    
    public static final int numLayers = 8;
    public static final double layerSpacing = 0.03;
    Geometry[] grassLayers = new Geometry[numLayers];
    
    public static Material[] textures = new Material[numLayers];
    public static boolean texturesGenerated = false;
    

    public void createNode()
    {
        ModelBuilder mb = new ModelBuilder();
        
        mb.addQuad(0, 0, 0, Vector3f.UNIT_Y, 0, 0, 
                   width, 0, 0, Vector3f.UNIT_Y, 1, 0, 
                   width, 0, height, Vector3f.UNIT_Y, 1, 1, 
                   0, 0, height, Vector3f.UNIT_Y, 0, 1);

        node = new Node();
        
        Mesh evenCells = mb.bake();
        Geometry geo = new Geometry("", evenCells);
        geo.setMaterial(Main.mat_grass);
        node.attachChild(geo);
        
        if(!texturesGenerated)
        {
            genTextures();
        }
        
        for(int i = 0; i < numLayers; ++ i)
        {
            grassLayers[i] = new Geometry("", evenCells);
            grassLayers[i].setMaterial(textures[i]);
            grassLayers[i].setShadowMode(RenderQueue.ShadowMode.Receive);
            grassLayers[i].move(0, (float) (layerSpacing * (i + 1)), 0);
        }
        updateLOD();
        
        node.move(x * width, 0, z * height);
    }
    
    public void genTextures()
    {
        for(int i = 0; i < numLayers; ++ i)
        {
            // TODO: make textures static
            Texture tex = makeLevel(i * (1d / numLayers), 4, new Random(1337));
            Material mat = SAM.ASSETS.loadMaterial("Materials/SpringGrassTufts.j3m");
            mat.setTexture("DiffuseMap", tex);
            
            textures[i] = mat;
        }
        
        texturesGenerated = true;
    }
    
    public void setLOD(int lod)
    {
        if(currentLOD != lod)
        {
            currentLOD = lod;
            this.updateLOD();
        }
    }
    
    private void updateLOD()
    {
        switch(currentLOD)
        {
            case Chunk.HIGH_LOD:
            {
                for(int i = 0; i < numLayers; ++ i)
                {
                    node.attachChild(grassLayers[i]);
                }
                break;
            }
            case Chunk.MID_LOD:
            {
                for(int i = 0; i < numLayers; ++ i)
                {
                    if((i & 1) == 1)
                    {
                        grassLayers[i].removeFromParent();
                    }
                    else
                    {
                        node.attachChild(grassLayers[i]);
                    }
                }
                break;
            }
            case Chunk.LOW_LOD:
            {
                for(int i = 0; i < numLayers; ++ i)
                {
                    grassLayers[i].removeFromParent();
                }
                break;
            }
        }
    }
    
    public Texture2D makeLevel(double amountToRemove, int size, Random random)
    {
        try
        {
            BufferedImage bi = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
            BufferedImage bi2 = ImageIO.read(new File("assets/Textures/SpringGrass.jpeg"));
        
            for(int x = 0; x < 512 / size; ++ x)
            {
                for(int y = 0; y < 512 / size; ++ y)
                {
                    boolean yesno = random.nextDouble() < amountToRemove;
                    
                    for(int h = 0; h < size; ++ h)
                    {
                        for(int k = 0; k < size; ++ k)
                        {
                            if(yesno)
                            {
                                bi.setRGB(x * size + h, y * size + k, bi2.getRGB(x * size + h, y * size + k) & 0x00FFFFFF + 0x01000000);
                            }
                            else
                            {
                                bi.setRGB(x * size + h, y * size + k, bi2.getRGB(x * size + h, y * size + k));
                            }
                        }
                    }
                }
            }
            
            Image image = new AWTLoader().load(bi, false);
            Texture2D tex = new Texture2D(image);
        
            return tex;
        }
        catch(IOException ex)
        {
            return null;
        }
    }

    public Node getNode()
    {
        return node;
    }

    @Override
    public void createBody(PhysWorld world) {}

    @Override
    protected Body getBody()
    {
        return null;
    }

}
