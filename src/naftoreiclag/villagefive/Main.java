/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Main extends SimpleApplication
{
    // This is where the magic begins
    public static void main(String[] args)
    {
        Main main = new Main();
        main.start();
    }
    
    Hills hills;

    @Override
    public void simpleInitApp()
    {
        hills = new Hills();
        
        Geometry geom = new Geometry("Box", hills.mesh);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setColor("Color", ColorRGBA.Blue);
        //mat.getAdditionalRenderState().setWireframe(true);
        
        Texture texture = assetManager.loadTexture("Textures/debug.png");
        mat.setTexture("ColorMap", texture);
        geom.setMaterial(mat);
        //geom.setLocalTranslation(5, 5, 5);

        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf)
    {
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }
}
