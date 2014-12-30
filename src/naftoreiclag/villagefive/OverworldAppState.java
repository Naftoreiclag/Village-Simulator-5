/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.ReiCamera;
import naftoreiclag.villagefive.util.serializable.PlotSerial;
import naftoreiclag.villagefive.world.entity.PinguinEntity;
import naftoreiclag.villagefive.world.entity.KatCompleteEntity;
import naftoreiclag.villagefive.world.entity.DoorEntity;
import naftoreiclag.villagefive.world.entity.FlowerEntity;
import naftoreiclag.villagefive.world.World;
import naftoreiclag.villagefive.util.KeyKeys;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Texture;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;

public class OverworldAppState extends AbstractAppState implements ActionListener
{
    private Main app;
    private Node trueRootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private Camera cam;
    private ViewPort viewPort;
    
    World world;
    Node chasePnt;
    
    KatCompleteEntity morgan;
    private Node stateRootNode;
    
    PlayerController playCont;
    ChaseCamera chaseCam;
    
    ReiCamera rcam;
    
    PlotSerial house;
    
    public void gimmiePlot(PlotSerial house)
    {
        this.house = house;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        this.app = (Main) app;
        this.trueRootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();
        this.viewPort = this.app.getViewPort();
        
        this.stateRootNode = new Node();
        trueRootNode.attachChild(stateRootNode);
        
        setupUselessAestetics();
        
        rcam = new ReiCamera(cam);
        rcam.mode = ReiCamera.SmoothMode.cubic;
        
        world = new World(stateRootNode, assetManager);
        
        playCont = new PlayerController();
        
        inputManager.addListener(playCont, KeyKeys.move_backward, KeyKeys.move_forward, KeyKeys.move_left, KeyKeys.move_right, KeyKeys.rotate_camera_left, KeyKeys.rotate_camera_right, KeyKeys.mouse_left);
        inputManager.addListener(this, KeyKeys.debug);
        
        morgan = world.spawnEntity(KatCompleteEntity.class, new Vector2f(0f, 0f));
        morgan.attachSpatial(chasePnt);
        
        playCont.setEntity(morgan);
        playCont.setCamera(rcam);
        playCont.setGround(ground);
        playCont.setManager(inputManager);
        
        
        
        for(int i = 0; i < 200; ++ i)
        {
            world.spawnEntity(FlowerEntity.class, new Vector2f(FastMath.rand.nextFloat() * 64f - 32f, FastMath.rand.nextFloat() * 64f - 32f));
        }
        
        
        house.setX(10);
        house.setZ(10);
        
        world.rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        world.spawnPlot(house);
        
        world.spawnEntity(DoorEntity.class, new Vector2f(0f, 0f));
        world.spawnEntity(PinguinEntity.class, new Vector2f(-4f, 4f));
        
        inputManager.setCursorVisible(true);

    }
    
    @Override
    public void cleanup()
    {
        stateRootNode.removeFromParent();
        viewPort.removeProcessor(dlsr);
        
    }

    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        
        playCont.tick(tpf);
        world.tick(tpf);
        
        rcam.tick(tpf);
    }

    @Override
    public void render(RenderManager rm)
    {
    }
    Spatial ground;
    
    DirectionalLightShadowRenderer dlsr;
    private void setupUselessAestetics()
    {
        
        
        chasePnt = new Node();
        chasePnt.setLocalTranslation(0, 3.5f, 0);
        stateRootNode.attachChild(chasePnt);
        
        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
	    viewPort.setClearFlags(true, true, true);
        
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.4f));
        stateRootNode.addLight(al);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.6f));
        sun.setDirection(new Vector3f(0.96f, -2.69f, -0.69f).normalizeLocal());
        stateRootNode.addLight(sun);
        
        dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 3);
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
        stateRootNode.attachChild(ground);
        
        Texture west = assetManager.loadTexture("Textures/clouds/clouds1_west.bmp");
        Texture east = assetManager.loadTexture("Textures/clouds/clouds1_east.bmp");
        Texture north = assetManager.loadTexture("Textures/clouds/clouds1_north.bmp");
        Texture south = assetManager.loadTexture("Textures/clouds/clouds1_south.bmp");
        Texture up = assetManager.loadTexture("Textures/clouds/clouds1_up.bmp");
        Texture down = assetManager.loadTexture("Textures/clouds/clouds1_down.bmp");
    }

    boolean debugKey = false;
    
    public void onAction(String key, boolean isPressed, float tpf)
    {
        if(key.equals(KeyKeys.debug))
        {
            debugKey = isPressed;
            
        }
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
