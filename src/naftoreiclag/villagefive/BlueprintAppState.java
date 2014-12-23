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
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.BlueprintGeoGen;

public class BlueprintAppState extends AbstractAppState
{
    private Main app;
    private Node rootNode;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private Camera cam;
    private ViewPort viewPort;
    
    Geometry grid;
    
    Plot plot = new Plot();
    
    public BlueprintAppState()
    {
        plot.setWidth(10);
        plot.setHeight(10);
    }
    
    float frustumSize = 1.0f;

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
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
    
        Geometry geo = gimmie();
        geo.setMaterial(assetManager.loadMaterial("Materials/wiremesh.j3m"));
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
        
        bgg.addLine(0, 0, 1, 1);
        
        Mesh m = bgg.bake(0.1f, 1.0f, 1.0f);
        
        Geometry geo = new Geometry("", m);
        
        return geo;
    }
}
