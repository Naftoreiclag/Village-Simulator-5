/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.KeyKeys;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication
{
    static int width;
    static int height;
    public static void main(String[] args)
    {
        AppSettings displ = new AppSettings(true);
        displ.setSettingsDialogImage("interface/splash.png");
        displ.setResolution(1280, 720);
        displ.setSamples(4);
        displ.setFrameRate(60);
        
        Main main = new Main();
        main.setSettings(displ);
        main.showSettings = false;
        
        width =  main.settings.getWidth();
        height = main.settings.getHeight();
        main.start();
    }
    
    OverworldAppState tas;
    BlueprintAppState bas;
    
    @Override
    public void simpleInitApp()
    {
        KeyKeys.hookInputs(inputManager);
        
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
