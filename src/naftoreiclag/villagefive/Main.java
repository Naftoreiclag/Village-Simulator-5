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
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication
{
    // This is where the magic begins
    public static void main(String[] args)
    {
        AppSettings settings = new AppSettings(true);
        settings.setSettingsDialogImage("interface/splash.png");
        
        Main main = new Main();
        main.setSettings(settings);
        main.start();
    }

    @Override
    public void simpleInitApp()
    {
        Material mat = (Material) assetManager.loadMaterial("Materials/camograss.j3m");
        Material mat2 = (Material) assetManager.loadMaterial("Materials/rock.j3m");
        
        TraditionalHills hills = new TraditionalHills();
        Geometry geom = new Geometry("Grass ", hills.mesh);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        Geometry geom2 = new Geometry("Rock ", hills.mesh2);
        geom2.setMaterial(mat2);
        rootNode.attachChild(geom2);
        
        Box box = new Box(2f / 2f, 3.75f / 2f, 2f / 2f);
        Geometry bounds = new Geometry("box", box);
        bounds.setMaterial((Material) assetManager.loadMaterial("Materials/testBump.j3m"));
        bounds.move(16f, 3.75f / 2f, 16f);
        rootNode.attachChild(bounds);
        
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.5f));
        sun.setDirection(new Vector3f(1, -2, -3).normalizeLocal());
        rootNode.addLight(sun);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(3.0f));
        rootNode.addLight(al);
        
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
