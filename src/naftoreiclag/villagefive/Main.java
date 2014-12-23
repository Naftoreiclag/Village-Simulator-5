/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.Nifty;
import naftoreiclag.villagefive.ReiCamera.SmoothMode;
import naftoreiclag.villagefive.util.ModelBuilder;

public class Main extends SimpleApplication implements ActionListener
{
    public static void main(String[] args)
    {
        AppSettings displ = new AppSettings(true);
        displ.setSettingsDialogImage("interface/splash.png");
        displ.setResolution(1280, 720);
        displ.setSamples(4);
        
        Main main = new Main();
        main.setSettings(displ);
        main.showSettings = false;
        main.start();
    }
    
    World world;
    Node chasePnt;
    
    KatCompleteEntity morgan;
    
    PlayerController playCont;
    ChaseCamera chaseCam;
    
    ReiCamera rcam;
    
    Plot testp;
    
    @Override
    public void simpleInitApp()
    {
        setupUselessAestetics();
        
        rcam = new ReiCamera(cam);
        rcam.mode = SmoothMode.cubic;
        
        world = new World(rootNode, assetManager);
        world.enableRender();
        
        playCont = new PlayerController();
        
        inputManager.addMapping("Rotate Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rotate Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Walk Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Walk Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Rotate Cam Left", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Rotate Cam Right", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Debugkey", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("Left Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(playCont, "Rotate Left", "Rotate Right", "Walk Forward", "Walk Backward", "Rotate Cam Left", "Rotate Cam Right", "Left Click");
        inputManager.addListener(this, "Debugkey");
        
        morgan = world.spawnEntity(KatCompleteEntity.class, new Vector2f(0f, 0f));
        morgan.attachSpatial(chasePnt);
        
        playCont.setEntity(morgan);
        playCont.setCamera(rcam);
        playCont.setGround(ground);
        playCont.setManager(inputManager);
        
        
        MailboxEntity ent = world.spawnEntity(MailboxEntity.class, new Vector2f(5f, 5f));
        ent.meow();
        
        for(int i = 0; i < 200; ++ i)
        {
            world.spawnEntity(FlowerEntity.class, new Vector2f(FastMath.rand.nextFloat() * 64f - 32f, FastMath.rand.nextFloat() * 64f - 32f));
        }
        
        
        testp = new Plot();
        testp.setHeight(10);
        testp.setWidth(10);
        testp.setX(10);
        testp.setZ(10);
        
        world.addPlot(testp);
        
        
        world.spawnEntity(StoolEntity.class, new Vector2f(0f, 0f));
        world.spawnEntity(DoorEntity.class, new Vector2f(-2f, 2f));
        world.spawnEntity(PinguinEntity.class, new Vector2f(-4f, 4f));
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.registerScreenController(new TestController());
        //nifty.fromXml("Interface/test.xml", "start");
        guiViewPort.addProcessor(niftyDisplay);
        inputManager.setCursorVisible(true);

    }

    @Override
    public void simpleUpdate(float tpf)
    {
        playCont.tick(tpf);
        world.tick(tpf);
        
        if(mhe != null)
        {
            mhe.tick(tpf);
        }
        
        rcam.tick(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }
    Spatial ground;
    
    private void setupUselessAestetics()
    {
        
        
        flyCam.setEnabled(false);
        chasePnt = new Node();
        chasePnt.setLocalTranslation(0, 3.5f, 0);
        rootNode.attachChild(chasePnt);
        
        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.4f));
        rootNode.addLight(al);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.6f));
        sun.setDirection(new Vector3f(0.96f, -2.69f, -0.69f).normalizeLocal());
        rootNode.addLight(sun);
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.5f);
        dlsr.setLambda(0.55f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        viewPort.addProcessor(dlsr);
        
        DebugGrid gridMeshThing = new DebugGrid();
        gridMeshThing.buildGeometry();
        ground = new Geometry("84903401239015290", gridMeshThing.evenCells);
        ground.move(-DebugGrid.width / 2, 0, -DebugGrid.height / 2);
        ground.setMaterial((Material) assetManager.loadMaterial("Materials/camograss.j3m"));
        ground.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(ground);
        
        Texture west = assetManager.loadTexture("Textures/clouds/clouds1_west.bmp");
        Texture east = assetManager.loadTexture("Textures/clouds/clouds1_east.bmp");
        Texture north = assetManager.loadTexture("Textures/clouds/clouds1_north.bmp");
        Texture south = assetManager.loadTexture("Textures/clouds/clouds1_south.bmp");
        Texture up = assetManager.loadTexture("Textures/clouds/clouds1_up.bmp");
        Texture down = assetManager.loadTexture("Textures/clouds/clouds1_down.bmp");
        //rootNode.attachChild(SkyFactory.createSky(assetManager, west, east, north, south, up, down));
    }

    boolean debugKey = false;
    
    public void onAction(String key, boolean isPressed, float tpf)
    {
        if(key.equals("Debugkey"))
        {
            debugKey = isPressed;
            
            if(debugKey)
            {
            onDebugKeypress();
                
            }
            else
            {
                onDebugKeyrelease();
            }
        }
    }
    
    HouseEditor mhe;

    private void onDebugKeypress()
    {
        mhe = new HouseEditor(rootNode, assetManager, testp, rcam);
        playCont.disableInput();
        world.disableRender();
        mhe.enableRender();
        mhe.enableInput();
    }

    private void onDebugKeyrelease()
    {
        playCont.enable();
        mhe.disableRender();
        mhe.disableInput();
        world.enableRender();
    }

    class DebugGrid
    {
        public static final int width = 64;
        public static final int height = 64;
        public Mesh evenCells;

        public void buildGeometry()
        {
            ModelBuilder mb = new ModelBuilder();
            
            float tw = 1f / 32f;
            float th = 1f / 32f;

            for(int x = 0; x < width; ++x)
            {
                for(int z = 0; z < height; ++z)
                {
                    mb.setAppendOrigin(x, 0.0f, z);
                    
                    float tx = ((float ) x) * tw;
                    float ty = ((float ) z) * th;

                    mb.addQuad(0, 0, 0, new Vector3f(0, 2, 0), tx, ty,
                               1, 0, 0, new Vector3f(0, 2, 0), tx + tw, ty,
                               1, 0, 1, new Vector3f(0, 2, 0), tx + tw, ty + th,
                               0, 0, 1, new Vector3f(0, 2, 0), tx, ty + th);
                }
            }

            evenCells = mb.bake();
        }
    }
}
