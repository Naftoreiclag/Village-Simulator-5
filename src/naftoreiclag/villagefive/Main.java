/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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

    @Override
    public void simpleInitApp()
    {
        
        
        Hills hills = new Hills();
        Geometry geom = new Geometry("Rock", hills.mesh);
        geom.setMaterial((Material) assetManager.loadMaterial("Materials/bumpDebug.j3m"));
        Geometry geom2 = new Geometry("Grass", hills.mess);
        geom2.setMaterial((Material) assetManager.loadMaterial("Materials/camograss.j3m"));
        rootNode.attachChild(geom);
        rootNode.attachChild(geom2);
        

        /*
        NewHills hills = new NewHills();
        Geometry geom = new Geometry("Rock", hills.mesh);
        geom.setMaterial((Material) assetManager.loadMaterial("Materials/bumpDebug.j3m"));
        rootNode.attachChild(geom);
        */
        
        /*
        Box box = new Box(Vector3f.ZERO, 1.0f, 1.0f, 1.0f);
        
        Geometry foo = new Geometry("TestBox", box);
        foo.setMaterial((Material) assetManager.loadMaterial("Materials/testBump.j3m"));
        rootNode.attachChild(foo);
        */
        
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.5f));
        sun.setDirection(new Vector3f(1, -2, -3).normalizeLocal());
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(3.0f));
        
        
        
        rootNode.addLight(al);
        
        rootNode.addLight(sun);
        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
        
        
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
