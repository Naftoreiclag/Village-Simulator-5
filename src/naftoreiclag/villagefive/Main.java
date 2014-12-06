/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import naftoreiclag.villagefive.util.ModelBuilder;

public class Main extends SimpleApplication
{
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
    
    World world;
    Node chasePnt;
    
    MorganEntity morgan;
    
    PlayerController playCont;
    ChaseCamera chaseCam;
    
    @Override
    public void simpleInitApp()
    {
        setupUselessAestetics();
        
        world = new World(rootNode, assetManager);
        
        playCont = new PlayerController();
        
        inputManager.addMapping("Rotate Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rotate Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Walk Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Walk Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(playCont, "Rotate Left", "Rotate Right", "Walk Forward", "Walk Backward");
        
        morgan = world.spawnEntity(MorganEntity.class, new Vector2f(0f, 0f));
        morgan.attachSpatial(chasePnt);
        
        playCont.setEntity(morgan);
        playCont.setCamera(cam);
        
        
        
        MailboxEntity ent = world.spawnEntity(MailboxEntity.class, new Vector2f(5f, 5f));
        ent.meow();
        
        world.spawnEntity(KatHeadEntity.class, new Vector2f(3f, 3f));
        world.spawnEntity(KatHeadEntity.class, new Vector2f(-3f, 3f));
        world.spawnEntity(KatHatEntity.class, new Vector2f(-3f, 3f));
        world.spawnEntity(KatCompleteEntity.class, new Vector2f(-6f, 6f));
        
        world.spawnEntity(StoolEntity.class, new Vector2f(0f, 0f));
        world.spawnEntity(DoorEntity.class, new Vector2f(-2f, 2f));
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        playCont.tick(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }
    
    private void setupUselessAestetics()
    {
        flyCam.setEnabled(false);
        chasePnt = new Node();
        chasePnt.setLocalTranslation(0, 3.0f, 0);
        rootNode.attachChild(chasePnt);
        chaseCam = new ChaseCamera(cam, chasePnt, inputManager);
        
        
        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(al);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.5f));
        sun.setDirection(new Vector3f(1.69f, -2.69f, -3.69f).normalizeLocal());
        rootNode.addLight(sun);
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.2f);
        dlsr.setLambda(0.55f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        viewPort.addProcessor(dlsr);
        
        DebugGrid gridMeshThing = new DebugGrid();
        gridMeshThing.buildGeometry();
        Geometry even = new Geometry("84903401239015290", gridMeshThing.evenCells);
        even.move(-DebugGrid.width / 2, 0, -DebugGrid.height / 2);
        even.setMaterial((Material) assetManager.loadMaterial("Materials/40cm.j3m"));
        even.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(even);
    }

    class DebugGrid
    {
        public static final int width = 64;
        public static final int height = 64;
        public Mesh evenCells;

        public void buildGeometry()
        {
            ModelBuilder mb = new ModelBuilder();

            for(int x = 0; x < width; ++x)
            {
                for(int z = 0; z < height; ++z)
                {
                    mb.setAppendOrigin(x, 0.0f, z);

                    mb.addQuad(0, 0, 0, new Vector3f(0, 1, 0), 0, 0,
                               1, 0, 0, new Vector3f(0, 1, 0), 1, 0,
                               1, 0, 1, new Vector3f(0, 1, 0), 1, 1,
                               0, 0, 1, new Vector3f(0, 1, 0), 0, 1);
                }
            }

            evenCells = mb.bake();
        }
    }
}
