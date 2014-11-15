/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class Main extends SimpleApplication implements AnimEventListener, ActionListener
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
    ChaseCamera chaseCam;
    private AnimChannel channel;
    private AnimControl control;
    Node oto;
    @Override
    public void simpleInitApp()
    {

        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
        
        //addTerrain();
        
        addGrid();
        
        player = new Player();
        rootNode.attachChild(player.node);
        
        Box playerM = new Box(2f / 2f, 3.75f / 2f, 2f / 2f);
        Geometry playerG = new Geometry("box", playerM);
        playerG.setMaterial((Material) assetManager.loadMaterial("Materials/testBump.j3m"));
        playerG.setLocalTranslation(0, 3.75f / 2f, 0);
        playerG.move(5, 0, 5);
        playerG.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(playerG);
        foobar();
        barfoo("Flex", 20);
        barfoo("Wave", 30);
        
        
        oto = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        oto.setLocalScale(0.5f);
        oto.getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
        oto.setLocalTranslation(0, 5.0f / 2f, 0);
        oto.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        control = oto.getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        channel.setAnim("stand", 0.5f);
        player.node.attachChild(oto);
        
        //player.node.attachChild(playerG);
        
        Node chasePnt = new Node();
        chasePnt.setLocalTranslation(0, 2.0f, 0);
        player.node.attachChild(chasePnt);
        
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, chasePnt, inputManager);
        
        
        addLight();
        
        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
        keySutff();
        
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        player.tick(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }

    private void addGrid()
    {
        DebugGrid gridMeshThing = new DebugGrid();
        gridMeshThing.buildGeometry();
        Geometry even = new Geometry("84903401239015290", gridMeshThing.evenCells);
        even.setMaterial((Material) assetManager.loadMaterial("Materials/40cm.j3m"));
        even.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(even);
    }

    private void keySutff()
    {
        inputManager.addMapping("Rotate Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rotate Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Walk Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Walk Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, "Walk Forward");
        inputManager.addListener(player, "Rotate Left", "Rotate Right");
        inputManager.addListener(player, "Walk Forward", "Walk Backward");
    }

    private void addTerrain()
    {
        Material mat = (Material) assetManager.loadMaterial("Materials/camograss.j3m");
        Material mat2 = (Material) assetManager.loadMaterial("Materials/rock.j3m");
        
        TraditionalHills hills = new TraditionalHills();
        Geometry geom = new Geometry("Grass ", hills.mesh);
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, 0.1f, 0);
        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(geom);
        Geometry geom2 = new Geometry("Rock ", hills.mesh2);
        geom2.setMaterial(mat2);
        geom2.setLocalTranslation(0, 0.1f, 0);
        geom2.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(geom2);
    }

    private void addLight()
    {
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(3.0f));
        rootNode.addLight(al);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(0.5f));
        sun.setDirection(new Vector3f(1.69f, -2.69f, -3.69f).normalizeLocal());
        rootNode.addLight(sun);
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 2048, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.6f);
        dlsr.setLambda(0.55f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        viewPort.addProcessor(dlsr);
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName)
    {
        if(!isForwardPressed)
        {
            if(animName.equals("Walk"))
            {
                channel.setAnim("stand", 0.50f);
                channel.setLoopMode(LoopMode.DontLoop);
                channel.setSpeed(1f);
            }
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName)
    {
    }

    boolean isForwardPressed;
    @Override
    public void onAction(String key, boolean isPressed, float tpf)
    {
        if(key.equals("Walk Forward"))
        {
            isForwardPressed = isPressed;
            if(isPressed)
            {
                if(!channel.getAnimationName().equals("Walk"))
                {
                    channel.setAnim("Walk", 0.50f);
                    channel.setLoopMode(LoopMode.Loop);
                }
            }
        }
    }

    private void foobar()
    {
        //Node wiggle = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        Node wiggle = (Node) assetManager.loadModel("Models/wig/Cube.mesh.j3o");
        wiggle.setMaterial((Material) assetManager.loadMaterial("Materials/testBump.j3m"));
        wiggle.move(10, 0, 10);
        wiggle.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        wiggleify(wiggle);
        
        rootNode.attachChild(wiggle);
    }
    
    private void barfoo(String flex, int par)
    {
        Node wiggle = (Node) assetManager.loadModel("Models/worm/Cylinder.mesh.xml");
        wiggle.setMaterial((Material) assetManager.loadMaterial("Materials/testBump.j3m"));
        wiggle.move(par, 0, 10);
        wiggle.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        AnimControl ctrl = wiggle.getControl(AnimControl.class);
        wiggle.attachChild(debugSkele(ctrl));
        AnimChannel chnl = ctrl.createChannel();
        chnl.setAnim(flex, 0.5f);
        chnl.setLoopMode(LoopMode.Loop);
        
        rootNode.attachChild(wiggle);
    }

    private Spatial debugSkele(AnimControl control)
    {
        SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton", control.getSkeleton());
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        mat.getAdditionalRenderState().setDepthTest(false);
        skeletonDebug.setMaterial(mat);
        return skeletonDebug;
    }

    private void wiggleify(Node wiggle)
    {
        AnimControl ctrl = wiggle.getControl(AnimControl.class);
        if(ctrl == null)
        {
            System.out.println("aaaa");
        }
        
        AnimChannel chnl = ctrl.createChannel();
        wiggle.attachChild(debugSkele(ctrl));
        chnl.setAnim("my_animation", 0.5f);
        chnl.setLoopMode(LoopMode.Loop);
    }
}
