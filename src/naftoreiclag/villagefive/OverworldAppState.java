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
import com.jme3.audio.AudioRenderer;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import naftoreiclag.villagefive.util.KeyKeys;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.HorizQuad;
import naftoreiclag.villagefive.world.entity.StoolEntity;
import naftoreiclag.villagefive.world.plot.Plot;
import org.json.simple.parser.ParseException;

public class OverworldAppState extends AbstractAppState
{
    private Main app;
    private Node trueRootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private ViewPort magicViewPort;
    private InputManager inputManager;
    private Camera cam;
	private RenderManager renderManager;
    private ViewPort viewPort;
    private Nifty nifty;
Camera magicCamera;
    
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
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getGuiViewPort();
        this.renderManager = this.app.getRenderManager();
        this.cam = this.app.getCamera();
        this.viewPort = this.app.getViewPort();
        
        
        this.stateRootNode = new Node();
        trueRootNode.attachChild(stateRootNode);
        
        setupUselessAestetics();
        inputManager.setCursorVisible(true);
        
        rcam = new ReiCamera(cam);
        rcam.mode = ReiCamera.SmoothMode.cubic;
        
        
        
        setupInvScreen();

        loadworld();

    }
    
    Sprite sweetmelon;
    
    private void setupInvScreen()
    {
        magicCamera = new Camera(cam.getWidth(), cam.getHeight());
        magicCamera.setParallelProjection(true);
        magicCamera.setLocation(Vector3f.UNIT_Z);
        magicCamera.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        magicCamera.setFrustum(-1000, 1000, (float) -cam.getWidth(), (float) 0, (float) cam.getHeight(), (float) 0);
        magicViewPort = renderManager.createPostView("baaackground", magicCamera);
        magicViewPort.setClearFlags(false, true, true);
        
        sweetmelon = new Sprite("Interface/melon.png");
        SpritePlane plane = new SpritePlane(magicViewPort);
        
        plane.add(sweetmelon);
    }
    
    
    @Override
    public void cleanup()
    {
        stateRootNode.removeFromParent();
        viewPort.removeProcessor(dlsr);
        
    }
    
    boolean keyRel = true;

    @Override
    public void update(float tpf)
    {
        if(KeyKeys.p_fastforward)
        {
            tpf *= 5;
        }
        
        if(KeyKeys.p_save && keyRel)
        {
            try
            {
                SaveLoad.save(world);
            }
            catch(IOException ex)
            {
                Logger.getLogger(OverworldAppState.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            keyRel = false;
        }
        if(!KeyKeys.p_save)
        {
            keyRel = true;
        }
        
        super.update(tpf);
        
        playCont.tick(tpf);
        world.tick(tpf);
        
        rcam.tick(tpf);
        
        Vec2 deleteme = new Vec2(inputManager.getCursorPosition());
        deleteme.debug();
        sweetmelon.setPos(deleteme);
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
        
        HorizQuad quad = new HorizQuad(-300, -300, 300, 300);
        ground = new Geometry("", quad);
        
    }

    private void testworld()
    {
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
    }

    private void loadworld()
    {
        
        try
        {
            world = SaveLoad.load(stateRootNode, assetManager);
        }
        catch(IOException ex)
        {
            Logger.getLogger(OverworldAppState.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(ParseException ex)
        {
            Logger.getLogger(OverworldAppState.class.getName()).log(Level.SEVERE, null, ex);
        }
        //world.rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        player = world.spawnEntity(PlayerEntity.class, new Vec2(256f, 256f));
        player.attachSpatial(chasePnt);
        player.attachGround(ground);
        playCont = new PlayerController();
        playCont.setEntity(player);
        playCont.setCamera(rcam);
        playCont.setGround(ground);
        playCont.setManager(inputManager);
    }
}
