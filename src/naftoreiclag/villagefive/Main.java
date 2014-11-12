/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
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
        AppSettings displ = new AppSettings(true);
        displ.setSettingsDialogImage("interface/splash.png");
        displ.setResolution(1280, 720);
        displ.setSamples(4);
        
        Main main = new Main();
        main.setSettings(displ);
        main.start();
    }
    
    Player player = new Player();

    @Override
    public void simpleInitApp()
    {
        keySutff();
        
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
        
        Material mat = (Material) assetManager.loadMaterial("Materials/camograss.j3m");
        Material mat2 = (Material) assetManager.loadMaterial("Materials/rock.j3m");
        
        TraditionalHills hills = new TraditionalHills();
        Geometry geom = new Geometry("Grass ", hills.mesh);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        Geometry geom2 = new Geometry("Rock ", hills.mesh2);
        geom2.setMaterial(mat2);
        rootNode.attachChild(geom2);
        
        addGrid();
        
        Box player = new Box(2f / 2f, 3.75f / 2f, 2f / 2f);
        Geometry bounds = new Geometry("box", player);
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

    private void addGrid()
    {
        Material lightColor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lightColor.setColor("Color", new ColorRGBA(0.87f, 0.87f, 0.87f, 1.0f));
        Material darkColor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        darkColor.setColor("Color", new ColorRGBA(0.69f, 0.69f, 0.69f, 1.0f));
        
        DebugGrid gridMeshThing = new DebugGrid();
        gridMeshThing.buildGeometry();
        Geometry even = new Geometry("Grass ", gridMeshThing.evenCells);
        even.setMaterial(lightColor);
        rootNode.attachChild(even);
        Geometry odd = new Geometry("Rock ", gridMeshThing.oddCells);
        odd.setMaterial(darkColor);
        rootNode.attachChild(odd);
    }

    private void keySutff()
    {
        inputManager.addMapping("Rotate Left", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rotate Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Walk Forward", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("Walk Backward", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addListener(player, "Rotate Left", "Rotate Right");
        inputManager.addListener(player, "Walk Forward", "Walk Backward");
    }
}
