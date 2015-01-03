/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.KeyKeys;
import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;

public class Main extends SimpleApplication implements ActionListener
{
    static int width;
    static int height;
    public static void main(String[] args)
    {
        AppSettings displ = new AppSettings(true);
        displ.setSettingsDialogImage("interface/splash.png");
        displ.setResolution(1280, 720);
        displ.setSamples(4);
        //displ.setFrameRate(60);
        
        Main main = new Main();
        main.setSettings(displ);
        main.showSettings = false;
        
        width =  main.settings.getWidth();
        height = main.settings.getHeight();
        main.start();
    }
    
    public static Material mat_debug_wireframe;
    public static Material mat_debug;
    public static Material mat_grass;
    public static Material mat_debug_lighting;
    public static Material mat_debug_bricks;
    public static Material mat_debug_gold;
    
    OverworldAppState tas;
    PlotEditorAppState bas;
    
    @Override
    public void simpleInitApp()
    {
        KeyKeys.hookInputs(inputManager);
        
        ModelManipulator.assetManager = this.assetManager;
        
        inputManager.addListener(this, KeyKeys.num_9, KeyKeys.num_8);
        
        mat_debug_wireframe = assetManager.loadMaterial("Materials/wiremesh.j3m");
        mat_debug = assetManager.loadMaterial("Materials/debug.j3m");
        mat_debug_lighting = assetManager.loadMaterial("Materials/debugLighting.j3m");
        mat_debug_bricks = assetManager.loadMaterial("Materials/Bricks.j3m");
        mat_debug_gold = assetManager.loadMaterial("Materials/TestMaterial.j3m");
        mat_grass = assetManager.loadMaterial("Materials/camograss.j3m");
        
        flyCam.setEnabled(false);
        
        tas = new OverworldAppState();
        bas = new PlotEditorAppState();
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

    public void onAction(String name, boolean isPressed, float tpf)
    {
        if(name.equals(KeyKeys.num_9))
        {
            stateManager.detach(bas);
            tas.gimmiePlot(bas.plotData);
            stateManager.attach(tas);
        }
        if(name.equals(KeyKeys.num_8))
        {
            stateManager.detach(tas);
            stateManager.attach(bas);
        }
    }

}
