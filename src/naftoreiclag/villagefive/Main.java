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
import naftoreiclag.villagefive.util.Vector2f;

public class Main extends SimpleApplication implements ActionListener
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
    
    Space space;
    
    Player player;
    
    ChaseCamera chaseCam;
    
    @Override
    public void simpleInitApp()
    {
        setupInput();
        attachDebugFloor();
        
        Node body = (Node) assetManager.loadModel("Models/perry/Cube.mesh.xml");
        body.setMaterial((Material) assetManager.loadMaterial("Materials/perry.j3m"));
        body.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        player = new Player(body);
        
        rootNode.attachChild(player.node);
        
        //setupAndAttachPlayer();
        setupCamera();
        setupLight();
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        updateCharacterPhysics(tpf);
        //space.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }

    private void attachDebugFloor()
    {
        DebugGrid gridMeshThing = new DebugGrid();
        gridMeshThing.buildGeometry();
        Geometry even = new Geometry("84903401239015290", gridMeshThing.evenCells);
        even.setMaterial((Material) assetManager.loadMaterial("Materials/40cm.j3m"));
        even.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(even);
    }

    public static final String key_fwd = "lolfwd";
    public static final String key_bwd = "lolbwd";
    public static final String key_left = "lolleft";
    public static final String key_right = "lolright";
    
    private void setupInput()
    {
        inputManager.addMapping(key_left, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(key_right, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(key_fwd, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(key_bwd, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, key_left, key_right, key_fwd, key_bwd);
    }

    private void setupLight()
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

    boolean turningLeft = false;
    boolean turningRight = false;
    boolean movingFwd = false;
    boolean movingBwd = false;
    @Override
    public void onAction(String key, boolean isPressed, float tpf)
    {
        System.out.println("key " + key + " = " + isPressed + ";");
        
        if(key.equals(key_fwd))
        {
            movingFwd = isPressed;
        }
        if(key.equals(key_bwd))
        {
            movingBwd = isPressed;
        }
        if(key.equals(key_left))
        {
            turningLeft = isPressed;
        }
        if(key.equals(key_right))
        {
            turningRight = isPressed;
        }
        
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

    private void setupCamera()
    {
        flyCam.setEnabled(false);
        Node chasePnt = new Node();
        chasePnt.setLocalTranslation(0, 2.0f, 0);
        player.node.attachChild(chasePnt);
        chaseCam = new ChaseCamera(cam, chasePnt, inputManager);
        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
    }

    private void updateCharacterPhysics(float tpf)
    {
        
        boolean mv = false;
        
        if(movingFwd)
        {
            mv = true;
        }
        if(movingBwd)
        {
            mv = true;
        }
        if(turningLeft)
        {
            mv = true;
        }
        if(turningRight)
        {
            mv = true;
        }
        
        if(!mv)
        {
            player.stopMoving();
            return;
        }
        
        Vector3f g = cam.getDirection();
        Vector2f vec = new Vector2f(g.x, g.z);
        
        if(movingBwd)
        {
            vec.inverseLocal();
        }
        if(turningLeft)
        {
            vec.perpendicularLocal();
        }
        if(turningRight)
        {
            vec.perpendicularLocal().inverseLocal();
        }
        
        System.out.println(vec.a + ", " + vec.b);
        player.move(vec, tpf);
    }
}
