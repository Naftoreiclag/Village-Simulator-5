/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.util.ModelBuilder;

public class HouseEditor
{
    Vector2f lookLoc = Vector2f.ZERO.clone();
    
    final Node trueRootNode;
    final Node rootNode;
    AssetManager assetManager;
    final PlotSerial plot;
    final ReiCamera cam;
    
    float camDir;
    
    public HouseEditor(Node rootNode, AssetManager assetManager, PlotSerial plot, ReiCamera cam)
    {
        this.rootNode = new Node();
        this.trueRootNode = rootNode;
        this.assetManager = assetManager;
        
        
        this.plot = plot;
        attachPlotGeo();
        
        this.cam = cam;
    }
    
    public void tick(float tpf)
    {
        
        if(!enabled)
        {
            return;
        }
        
        cam.setLocation((new Vector3f(FastMath.cos(FastMath.HALF_PI - camDir) * 15.0f, 7.0f, FastMath.sin(FastMath.HALF_PI - camDir) * 15.0f)));
        cam.lookAt(lookLoc.x, 0, lookLoc.y);
    }
    
    public void disableRender()
    {
        this.trueRootNode.detachChild(rootNode);
    }
    public void enableRender()
    {
        this.trueRootNode.attachChild(rootNode);
    }
    
    private void attachPlotGeo()
    {
        ModelBuilder mb = new ModelBuilder();

        mb.addQuad(0, 0, 0, new Vector3f(0, 2, 0), 0, 0,
                    1, 0, 0, new Vector3f(0, 2, 0), 0, 0,
                    1, 0, 1, new Vector3f(0, 2, 0), 0, 0,
                    0, 0, 1, new Vector3f(0, 2, 0), 0, 0);

        Mesh m = mb.bake(plot.getWidth(), 0, plot.getHeight());
        
        Geometry geo = new Geometry("aaaa", m);
        geo.setLocalTranslation((float) plot.getX(), 0.1f, (float) plot.getZ());
        geo.setMaterial((Material) assetManager.loadMaterial("Materials/wiremesh.j3m"));
        geo.setShadowMode(RenderQueue.ShadowMode.Receive);
        
        System.out.println(geo.getLocalTranslation());
        
        rootNode.attachChild(geo);
    }

    void cleanup()
    {
        
    }
    private boolean enabled = true;
    
    void disableInput()
    {
        this.enabled = false;
    }
    void enableInput()
    {
        this.enabled = true;
    }
}
