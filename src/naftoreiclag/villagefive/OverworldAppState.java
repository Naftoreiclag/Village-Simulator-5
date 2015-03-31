/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import naftoreiclag.villagefive.console.DevConsoleAppState;
import naftoreiclag.villagefive.gui.SpritePlane;
import naftoreiclag.villagefive.util.ReiCamera;
import naftoreiclag.villagefive.world.entity.PlayerEntity;
import naftoreiclag.villagefive.world.World;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import de.lessvoid.nifty.Nifty;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import naftoreiclag.villagefive.util.KeyKeys;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.AxesMaker;
import naftoreiclag.villagefive.util.scenegraph.HorizQuad;
import naftoreiclag.villagefive.util.serializable.Blueprint;
import naftoreiclag.villagefive.util.serializable.BlueprintUtil;
import naftoreiclag.villagefive.world.Resident;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.entity.ForSaleEntity;
import naftoreiclag.villagefive.world.entity.StoolEntity;
import naftoreiclag.villagefive.world.plot.Plot;
import org.json.simple.parser.ParseException;

public class OverworldAppState extends AbstractAppState implements ActionListener
{
    private Main app;
    private Node trueRootNode;
    private AppStateManager stateManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private ViewPort invPort;
    private Camera cam;
	private RenderManager renderManager;
    private ViewPort viewPort;
    private Nifty nifty;
    Camera invCam;
    
    DevConsoleAppState devConsole;
    
    World world;
    Node chasePnt;
    
    PlayerEntity player;
    private Node stateRootNode;
    
    PlayerController playCont;
    ChaseCamera chaseCam;
    
    ReiCamera rcam;
    
    Blueprint house;
    Inventory inv;
    
    public void gimmiePlot(Blueprint house)
    {
        this.house = house;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        this.app = (Main) app;
        this.trueRootNode = this.app.getRootNode();
        this.stateManager = this.app.getStateManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getGuiViewPort();
        this.renderManager = this.app.getRenderManager();
        this.cam = this.app.getCamera();
        this.viewPort = this.app.getViewPort();
        
        this.stateRootNode = new Node();
        trueRootNode.attachChild(stateRootNode);
        
        setupUselessAestetics();
        SAM.INPUT.setCursorVisible(true);
        
        rcam = new ReiCamera(cam);
        rcam.mode = ReiCamera.SmoothMode.cubic;
        
        Node debugArrows = AxesMaker.make();
        debugArrows.move(256f, 0.1f, 256f);
        this.stateRootNode.attachChild(debugArrows);
        
        devConsole = new DevConsoleAppState();
        devConsole.setEnabled(false);
        stateManager.attach(devConsole);
        SAM.INPUT.addListener(this, KeyKeys.console);
        
        genworld();
        devConsole.setWorld(world);
        // loadworld();
        
        setupInvScreen();

    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf)
    {
    }
    
    
    private void setupInvScreen()
    {
        invCam = new Camera(cam.getWidth(), cam.getHeight());
        invCam.setParallelProjection(true);
        invCam.setLocation(Vector3f.UNIT_Z);
        invCam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        invCam.setFrustum(-1000, 1000, (float) -cam.getWidth(), (float) 0, (float) cam.getHeight(), (float) 0);
        invPort = renderManager.createPostView("baaackground", invCam);
        invPort.setClearFlags(false, true, true);
        
        inv = new Inventory(new SpritePlane(invPort));
        playCont.inv = this.inv;
    }
    
    
    @Override
    public void cleanup()
    {
        stateRootNode.removeFromParent();
        viewPort.removeProcessor(dlsr);
        
    }
    
    boolean keyRel = true;
    
    boolean consoleRelease = true;
    
    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        
        
        if(KeyKeys.p_console && consoleRelease)
        {
            System.out.println("press console");

            this.devConsole.setEnabled(!this.devConsole.isEnabled());
            
            consoleRelease = false;
        }
        if(!KeyKeys.p_console)
        {
            consoleRelease = true;
        }
        
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
        
        
        playCont.tick(tpf);
        world.tick(tpf);
        
        rcam.tick(tpf);
        
        inv.tick(tpf);
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
        
        dlsr = new DirectionalLightShadowRenderer(SAM.ASSETS, 2048, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.5f);
        dlsr.setLambda(0.55f);
        dlsr.setEdgesThickness(10);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        viewPort.addProcessor(dlsr);
        
        
        
        FilterPostProcessor fpp = new FilterPostProcessor(SAM.ASSETS);
        BloomFilter bf = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bf);
        viewPort.addProcessor(fpp);

        
        HorizQuad quad = new HorizQuad(-300, -300, 300, 300);
        ground = new Geometry("", quad);
        
    }

    // its as dirty as possible
    private void genworld()
    {
        world = new World(stateRootNode, SAM.ASSETS);
        
        // By default, nothing has influence on shadow rendering.
        world.rootNode.setShadowMode(RenderQueue.ShadowMode.Off);
        
        Resident resid = new Resident();
        world.residents.add(resid);
        
        player = new PlayerEntity();
        world.materializeEntity(player);
        player.setLocation(new Vec2(256, 256));
        player.attachSpatial(chasePnt);
        player.attachGround(ground);
        
        resid.SID = player.SID;
        
        playCont = new PlayerController();
        playCont.setResidence(resid);
        playCont.setEntity(player);
        playCont.setCamera(rcam);
        playCont.setGround(ground);
        playCont.setManager(SAM.INPUT);
        
        /*
        Plot plot = new Plot();
        plot.setBlueprint(house);
        plot.setLocation(new Vec2(260, 260));
        world.materializePlot(plot);
        */
        
        world.materializeEntityByName("naftogeometry:cone").setLocation(new Vec2(280, 280));
        world.materializeEntityByName("naftogeometry:torus").setLocation(new Vec2(275, 280));
        world.materializeEntityByName("naftogeometry:shiny").setLocation(new Vec2(270, 280));
        world.materializeEntityByName("naftogeometry:futbol").setLocation(new Vec2(265, 280));
        world.materializeEntityByName("naftogeometry:basketball").setLocation(new Vec2(260, 280));

        Random rand = new Random(1337);
        
        for(int i = 0; i < 10; ++ i)
        {
            Plot newPlot = new Plot();
            newPlot.setBlueprint(BlueprintUtil.makeSimple(30, 30));
            newPlot.setLocation(new Vec2(rand.nextDouble() * 512, rand.nextDouble() * 512));
            
            world.materializePlot(newPlot);
            
            
            ForSaleEntity sale = new ForSaleEntity();
            sale.thingy = newPlot;
            world.materializeEntity(sale);
            sale.setLocation(newPlot.getLocation());
        }
    }

    private void loadworld()
    {
        try
        {
            world = SaveLoad.load(stateRootNode, SAM.ASSETS);
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
        
        player = new PlayerEntity();
        world.materializeEntity(player);
        player.setLocation(new Vec2(256, 256));
        player.attachSpatial(chasePnt);
        player.attachGround(ground);
        playCont = new PlayerController();
        playCont.setEntity(player);
        playCont.setCamera(rcam);
        playCont.setGround(ground);
        playCont.setManager(SAM.INPUT);
    }
}
