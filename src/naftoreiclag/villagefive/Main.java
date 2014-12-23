/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

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
        main.showSettings = false;
        main.start();
    }
    
    OverworldAppState tas;
    BlueprintAppState bas;
    
    @Override
    public void simpleInitApp()
    {
        
        inputManager.addMapping(KeyKeys.lef, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(KeyKeys.rit, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(KeyKeys.fwd, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(KeyKeys.bwd, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(KeyKeys.rcl, new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping(KeyKeys.rcr, new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping(KeyKeys.dbg, new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping(KeyKeys.lmb, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        flyCam.setEnabled(false);
        
        tas = new OverworldAppState();
        bas = new BlueprintAppState();
        stateManager.attach(bas);
    }

    @Override
    public void simpleUpdate(float tpf)
    {
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
    }
}
