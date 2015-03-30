/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;

public class DevConsoleAppState extends AbstractAppState {
    
    private Main app;
    
    private Camera mainCam;
    
    private Camera cam;
    private ViewPort viewPort;
    
    private Node rootNode;
    
    Nifty nifty;
    
    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        
        app = (Main) application;
        
        mainCam = app.getCamera();
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/DeveloperConsole.xml", "hide");
        app.getGuiViewPort().addProcessor(niftyDisplay);

        /*
        viewPort = SAM.RENDER.createPostView(SAM.CONSOLE_VIEWPORT_NAME, cam);
        viewPort.setClearFlags(false, true, true);
        
        rootNode = new Node();
        viewPort.attachScene(rootNode);
        
        cam = new Camera(mainCam.getWidth(), mainCam.getHeight());
        cam.setParallelProjection(true);
        cam.setLocation(Vector3f.UNIT_Z);
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.setFrustum(-1000, 1000, (float) -mainCam.getWidth(), (float) 0, (float) mainCam.getHeight(), (float) 0);
        */
    }
    
    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        
        if(this.initialized)
        {
            if(enable) {
                nifty.gotoScreen("show");
            } else {
                nifty.gotoScreen("hide");
            }
        }
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    @Override
    public void cleanup() {
        
    }
}
