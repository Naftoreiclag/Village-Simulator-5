/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.ModelBuilder;

public class World
{
    Node trueRootNode;
    Node rootNode;
    AssetManager assetManager;
    
    List<Entity> entities = new ArrayList<Entity>();
    
    List<Plot> plots = new ArrayList<Plot>();
    
    World(Node rootNode, AssetManager assetManager)
    {
        this.rootNode = new Node();
        this.trueRootNode = rootNode;
        this.assetManager = assetManager;
    }
    
    public void disableRender()
    {
        this.trueRootNode.detachChild(rootNode);
    }
    public void enableRender()
    {
        this.trueRootNode.attachChild(rootNode);
    }
    
    public void addPlot(Plot p)
    {
        plots.add(p);
        
        drawPlot(p);
    }
    
    public <SomeEntity extends Entity> SomeEntity spawnEntity(Class<SomeEntity> entityType, Vector2f vector2f)
    {
        SomeEntity e;
        try
        {
            e = entityType.getConstructor().newInstance();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            
            return null;
        }
        
        e.assertWorld(this);
        e.assertNode(loadNode(e.getModelName()));
        e.teleport(vector2f);
        
        entities.add(e);
        
        return e;
    }
    public Node loadNode(String modelName)
    {
        
        //System.out.println(modelName);
        
        if(modelName == null)
        {
            Node body = new Node();
            body.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            rootNode.attachChild(body);

            return body;
        }
        
        Node body = (Node) assetManager.loadModel(modelName);
        body.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        rootNode.attachChild(body);
        
        return body;
        
    }
    
    public Control[] getControls(Node model)
    {
        int numControls = model.getNumControls();
        
        Control[] controls = new Control[numControls];
        for(int i = 0; i < numControls; ++ i)
        {
            controls[i] = model.getControl(i);
        }
        
        return controls;
    }

    void destroyEntity(Entity aThis)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void tick(float tpf)
    {
        for(Entity entity : entities)
        {
            entity.tick(tpf);
        }
    }
    
    private void drawPlot(Plot p)
    {
        ModelBuilder mb = new ModelBuilder();

        mb.addQuad(0, 0, 0, new Vector3f(0, 2, 0), 0, 0,
                    1, 0, 0, new Vector3f(0, 2, 0), 0, 0,
                    1, 0, 1, new Vector3f(0, 2, 0), 0, 0,
                    0, 0, 1, new Vector3f(0, 2, 0), 0, 0);

        Mesh m = mb.bake(p.getWidth(), 0, p.getHeight());
        
        Geometry geo = new Geometry("aaaa", m);
        geo.setLocalTranslation((float) p.getX(), 0.1f, (float) p.getZ());
        geo.setMaterial((Material) assetManager.loadMaterial("Materials/wiremesh.j3m"));
        geo.setShadowMode(RenderQueue.ShadowMode.Receive);
        
        System.out.println(geo.getLocalTranslation());
        
        
        rootNode.attachChild(geo);
        
        Node n = PlotNodifier.nodify(p);
        
        rootNode.attachChild(n);
    }

}
