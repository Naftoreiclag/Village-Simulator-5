/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.inventory;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import naftoreiclag.villagefive.InvItemEntity;
import naftoreiclag.villagefive.Inventory;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.gui.Collision;
import naftoreiclag.villagefive.gui.Element;
import naftoreiclag.villagefive.gui.Sprite;
import naftoreiclag.villagefive.gui.SpritePlane;
import naftoreiclag.villagefive.util.KeyKeys;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.entity.PlayerEntity;

public class InventoryRenderAppState extends AbstractAppState implements IInventoryUpdateListener, AnalogListener, ActionListener{
    Main app;
    
    Camera cam;
    ViewPort viewPort;
    
    SpritePlane plane;
    
    Element hotbar;
    Element[] slots;
    
    Element selectArrow;
    
    Vec2 mouseLoc = new Vec2();

    OverworldAppState game;
    PlayerEntity player;
    
    public void setGame(OverworldAppState aThis) {
        this.game = aThis;
        player = this.game.getPlayer();
        player.inventory.hook(this);
    }
    
    
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
        
        hotbar = new Sprite(SAM.ASSETS.loadTexture("Interface/hotbar.png"));
        selectArrow = new Sprite(SAM.ASSETS.loadTexture("Textures/select_arrow.png"));
        selectArrow.setOrigin(128, 32);
        
        hotbar.setOrigin(hotbar.width, hotbar.height);
        hotbar.setLoc(cam.getWidth() - 5, cam.getHeight() - 5);
        plane.attachElement(hotbar);
        
        slots = new Element[10];
        for(int i = 0; i < 10; ++ i) {
            slots[i] = new Collision(128, 128);
            slots[i].setLoc(-75, -75 - (i * 150));
            plane.attachElement(slots[i]);
            hotbar.attachElement(slots[i]);
        }
        
        SAM.INPUT.addListener(this, KeyKeys.mouse_move, KeyKeys.mouse_left);
        
    }
    
    @Override
    public void update(float tpf) {
    }

    
    
    public void onUpdate(Inventory inv, int slotIndex) {
        
        InvItemEntity egg = ((InvItemEntity) inv.items.get(slotIndex));
        
        if(egg != null) {
            Sprite melon = new Sprite(egg.entity.getIcon());
            melon.setWidthKeepRatio(128);

            plane.attachElement(melon);
            slots[slotIndex].attachElement(melon);
        }
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals(KeyKeys.mouse_left)) {
            Element clicked = plane.pick(mouseLoc);
            
            for(int i = 0; i < slots.length; ++ i) {
                if(clicked == slots[i]) {
                    player.selectedItem = i;
                }
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if(name.equals(KeyKeys.mouse_move)) {
            mouseLoc.set(SAM.INPUT.getCursorPosition());
        }
    }

}
