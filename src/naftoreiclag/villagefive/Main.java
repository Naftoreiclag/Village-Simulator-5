/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import org.yaml.snakeyaml.Yaml;

public class Main extends SimpleApplication
{
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
    
    public Space space = new Space();
    
    public SafeEntityHandler trainingWheels = new SafeEntityHandler(rootNode, space);
    
    public Entity player;
    
    public World world;
    
    @Override
    public void simpleInitApp()
    {
        foobar();
        
        Node body = (Node) assetManager.loadModel("Models/perry/Cube.mesh.xml");
        body.setMaterial((Material) assetManager.loadMaterial("Materials/perry.j3m"));
        body.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        player = new PlayerEntity(body);
        
        trainingWheels.attachEntity(player);
        trainingWheels.detachEntity(player);
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        trainingWheels.update(tpf);
        space.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }
    
    private void foobar()
    {
        flyCam.setEnabled(false);
        Node chasePnt = new Node();
        chasePnt.setLocalTranslation(0, 2.0f, 0);
        rootNode.attachChild(chasePnt);
        ChaseCamera chaseCam = new ChaseCamera(cam, chasePnt, inputManager);
        
        viewPort.setBackgroundColor(new ColorRGBA(66f / 255f, 176f / 255f, 255f / 255f, 1.0f));
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);
        
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
        
        DebugGrid gridMeshThing = new DebugGrid();
        gridMeshThing.buildGeometry();
        Geometry even = new Geometry("84903401239015290", gridMeshThing.evenCells);
        even.move(-DebugGrid.width / 2, 0, -DebugGrid.height / 2);
        even.setMaterial((Material) assetManager.loadMaterial("Materials/40cm.j3m"));
        even.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(even);
    }
}
