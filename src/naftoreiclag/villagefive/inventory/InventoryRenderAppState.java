/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.inventory;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.gui.Sprite;
import naftoreiclag.villagefive.gui.SpritePlane;

public class InventoryRenderAppState extends AbstractAppState {
    Main app;
    
    Camera cam;
    ViewPort viewPort;
    
    Sprite mainInventory;
    Sprite test;
    SpritePlane plane;
    
    double testScale = 1;
    
    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        
        this.app = (Main) application;
        
        Camera appCam = app.getCamera();
        cam = new Camera(appCam.getWidth(), appCam.getHeight());
        cam.setParallelProjection(true);
        cam.setLocation(Vector3f.UNIT_Z);
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.setFrustum(-1000, 1000, (float) -appCam.getWidth(), (float) 0, (float) appCam.getHeight(), (float) 0);
        viewPort = app.getRenderManager().createPostView("Inventory HUD", cam);
        viewPort.setClearFlags(false, true, true);
        
        plane = new SpritePlane(viewPort);
        mainInventory = new Sprite("Interface/inv.png");
        test = new Sprite("Interface/melon.png");
        plane.attachElement(mainInventory);
        plane.attachElement(test);
        test.setLoc(300, 300);
        mainInventory.attachElement(test);
    }
    
    @Override
    public void update(float tpf) {
        testScale += tpf * 0.2;
        
        mainInventory.setScale(testScale);
    }

    OverworldAppState game;
    
    public void setGame(OverworldAppState aThis) {
        this.game = aThis;
    }

}
