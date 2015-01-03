/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.ReiCamera;
import naftoreiclag.villagefive.util.serializable.PlotSerial;
import naftoreiclag.villagefive.world.entity.PinguinEntity;
import naftoreiclag.villagefive.world.entity.PlayerEntity;
import naftoreiclag.villagefive.world.entity.DoorEntity;
import naftoreiclag.villagefive.world.World;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.HorizQuad;
import naftoreiclag.villagefive.world.entity.StoolEntity;

public class OverworldAppState extends AbstractAppState
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
    
    PlayerEntity player;
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
        world.rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        
        
        player = world.spawnEntity(PlayerEntity.class, new Vec2(256f, 256f));
        player.attachSpatial(chasePnt);
        player.attachGround(ground);
        
        
        playCont = new PlayerController();
        playCont.setEntity(player);
        playCont.setCamera(rcam);
        playCont.setGround(ground);
        playCont.setManager(inputManager);
        
        house.setX(266);
        house.setZ(266);
        
        world.spawnPlot(house);
        
        world.spawnEntity(DoorEntity.class, new Vec2(256f, 256f));
        world.spawnEntity(PinguinEntity.class, new Vec2(266f, 266f));
        world.spawnEntity(StoolEntity.class, new Vec2(246f, 256f));
        
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
        //viewPort.addProcessor(dlsr);
        
        HorizQuad quad = new HorizQuad(-300, -300, 300, 300);
        ground = new Geometry("", quad);
        
    }
}
