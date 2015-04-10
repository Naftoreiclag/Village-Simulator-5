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
import naftoreiclag.villagefive.InvItem;
import naftoreiclag.villagefive.Inventory;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.gui.Anchor;
import naftoreiclag.villagefive.gui.BitmapFont;
import naftoreiclag.villagefive.gui.Collision;
import naftoreiclag.villagefive.gui.Element;
import naftoreiclag.villagefive.gui.FancySkin;
import naftoreiclag.villagefive.gui.HorizontalAutoplacer;
import naftoreiclag.villagefive.gui.HorizontalAutoplacer.Placement;
import naftoreiclag.villagefive.gui.Sprite;
import naftoreiclag.villagefive.gui.SpritePlane;
import naftoreiclag.villagefive.gui.Text;
import naftoreiclag.villagefive.gui.constructs.Button;
import naftoreiclag.villagefive.util.KeyKeys;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.entity.PlayerEntity;

public class InventoryRenderAppState extends AbstractAppState implements AnalogListener, ActionListener{
    Main app;
    
    Camera cam;
    ViewPort viewPort;
    
    SpritePlane plane;
    
    Element hotbar;
    Element[] slots;
    
    Element selectArrow;
    Element thinkAbout;
    
    Element topRight;
    Element topMid;
    Element topLeft;
    Element bottomRight;
    Element bottomMid;
    Element bottomLeft;
    
    Anchor actionAnchor;
    
    BitmapFont goudyBookletterFont;
    BitmapFont rainstormFont;
    BitmapFont tuffyFont;
    
    Vec2 mouseLoc = new Vec2();

    PlayerEntity player;
    
    public void setGame(OverworldAppState game) {
        player = game.getPlayer();
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
        
        topRight = new Anchor(1, 1);
        topRight.setLoc(-5, -5);
        plane.attachElement(topRight);
        
        topMid = new Anchor(0.5, 1);
        topMid.setLoc(0, -5);
        plane.attachElement(topMid);
        
        topLeft = new Anchor(0, 1);
        topLeft.setLoc(5, -5);
        plane.attachElement(topLeft);
        
        bottomRight = new Anchor(1, 0);
        bottomRight.setLoc(-5, 5);
        plane.attachElement(bottomRight);
        
        bottomMid = new Anchor(0.5, 0);
        bottomMid.setLoc(0, 5);
        plane.attachElement(bottomMid);
        
        bottomLeft = new Anchor(0, 0);
        bottomLeft.setLoc(5, 5);
        plane.attachElement(bottomLeft);
        
        hotbar = new Sprite(SAM.ASSETS.loadTexture("Interface/hotbar.png"));
        selectArrow = new Sprite(SAM.ASSETS.loadTexture("Textures/select_arrow.png"));
        selectArrow.setOrigin(128, 32);
        thinkAbout = new Sprite(SAM.ASSETS.loadTexture("Textures/think_about.png"));
        thinkAbout.setOrigin(158, 258);
        thinkAbout.setLoc(cam.getWidth() / 2, cam.getHeight() - 200);
        //plane.attachElement(thinkAbout);
        
        hotbar.setOrigin(hotbar.width, hotbar.height);
        topRight.attachElement(hotbar);
        
        slots = new Element[10];
        for(int i = 0; i < 10; ++ i) {
            slots[i] = new Collision(128, 128);
            slots[i].setLoc(-75, -75 - (i * 150));
            hotbar.attachElement(slots[i]);
        }
        
        SAM.INPUT.addListener(this, KeyKeys.mouse_move, KeyKeys.mouse_left);
        
        goudyBookletterFont = new BitmapFont(SAM.ASSETS.loadTexture("Interface/Fonts/Goudy-Bookletter-37.png"), 12, 37, 5);
        rainstormFont = new BitmapFont(SAM.ASSETS.loadTexture("Interface/Fonts/Rainstorm-40.png"), 11, 40, 5);
        tuffyFont = new BitmapFont(SAM.ASSETS.loadTexture("Interface/Fonts/Tuffy-Bold-30.png"), 5, 19, 5);
        
        placer = new HorizontalAutoplacer(20, Placement.center);
        plane.attachElement(placer);
        for(int i = 0; i < 10; ++ i) {
            Element sprite = new Sprite(SAM.ASSETS.loadTexture("Textures/select_arrow.png"));
            placer.attachElement(sprite);
        }
        sprite2 = new Sprite(SAM.ASSETS.loadTexture("Textures/select_arrow.png"));
        placer.attachElement(sprite2);
        
        actionAnchor = new Anchor(0.5, 0);
        actionAnchor.setLoc(0, 125);
        plane.attachElement(actionAnchor);
        
        actionAnchor.attachElement(placer);
        
        button1 = new Button(goudyBookletterFont, 30, "Hello world!");
        button1.setLoc(200, 200);
        plane.attachElement(button1);
        button2 = new Button(rainstormFont, 30, "Hello world!");
        button2.setLoc(200, 300);
        plane.attachElement(button2);
        button3 = new Button(tuffyFont, 30, "Hello world!");
        button3.setLoc(200, 400);
        plane.attachElement(button3);
        
    }
    
    Button button1;
    Button button2;
    Button button3;
    Element sprite2;
    HorizontalAutoplacer placer;
    Element testText;
    double testSc = 1;
    @Override
    public void update(float tpf) {
        testSc += tpf;
    }

    
    
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals(KeyKeys.mouse_left)) {
            Element clicked = plane.pick(mouseLoc);
            
            for(int i = 0; i < slots.length; ++ i) {
                if(clicked == slots[i]) {
                    player.setSelectedItemIndex(i);
                    
                    
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

    public void onPlayerSelects(int slotIndex) {
        
        if(slotIndex >= 10) {
            return;
        }
        
        thinkAbout.removeAllElements();
        InvItem selected = player.inventory.getItem(slotIndex);
        if(selected != null) {
            Sprite a = new Sprite(selected.entity.getIcon());
            thinkAbout.attachElement(a);
            a.setWidthKeepRatio(190);
        }

        slots[slotIndex].attachElement(selectArrow);
    }
    
    public void onInvUpdate(int slotIndex) {
        if(slotIndex >= 10) {
            return;
        }
        
        InvItem egg = player.inventory.items.get(slotIndex);
        
        // Remove the old one
        slots[slotIndex].removeAllElements();
        
        // If it's something, then show it
        if(egg != null) {
            Sprite melon = new Sprite(egg.entity.getIcon());
            melon.setWidthKeepRatio(128);

            slots[slotIndex].attachElement(melon);
        }
    }

}
