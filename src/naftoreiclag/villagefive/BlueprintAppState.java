/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.BlueprintGeoGen;

public class BlueprintAppState extends AbstractAppState implements ActionListener
{
    private Main app;
    private Node rootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private Camera cam;
    private ViewPort viewPort;
    
    Geometry grid;
    
    List<Flag> flags = new ArrayList<Flag>();
    
    boolean leftClick;

    public void onAction(String key, boolean isPressed, float tpf)
    {
    }
    
    public static class Flag
    {
        public final Vector2f loc;
        
        public Flag()
        {
            loc = new Vector2f();
        }
        
        public Flag(float x, float y)
        {
            loc = new Vector2f(x, y);
        }
    }
    
    Plot plot = new Plot();
    
    public BlueprintAppState()
    {
        plot.setWidth(7);
        plot.setHeight(5);
    }
    
    float frustumSize = 10.0f;

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();
        this.viewPort = this.app.getViewPort();
        
        inputManager.addListener(this, "Debugkey");
        
        Node knight = (Node) assetManager.loadModel("Models/Knight.mesh.j3o");
        //rootNode.attachChild(knight);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.0f));
        rootNode.addLight(al);

        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
        
        cam.setParallelProjection(true);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
    
        
        cam.setLocation(Vector3f.UNIT_Y);
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z.mult(-1.0f));
    
        Geometry geo = gimmie();
        Material mat = assetManager.loadMaterial("Materials/Stroke.j3m");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        geo.setMaterial(mat);
        geo.setQueueBucket(RenderQueue.Bucket.Transparent);
        rootNode.attachChild(geo);
        
    }
    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        
        
    }
    
    public Geometry gimmie()
    {
        BlueprintGeoGen bgg = new BlueprintGeoGen();
        
        for(int x = 0; x <= plot.getWidth(); ++ x)
        {
            bgg.addLine(x, 0, x, plot.getHeight());
        }
        for(int y = 0; y <= plot.getHeight(); ++ y)
        {
            bgg.addLine(0, y, plot.getWidth(), y);
        }
        
        
        Mesh m = bgg.bake(0.1f, 5.0f, 1.0f, 1.0f);
        
        Geometry geo = new Geometry("", m);
        
        return geo;
    }
}
