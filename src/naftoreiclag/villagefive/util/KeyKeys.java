/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

// huh

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import naftoreiclag.villagefive.SAM;

public class KeyKeys implements ActionListener
{
    public static final String move_forward = "Forward";
    public static final String move_backward = "Backward";
    public static final String move_left = "Left";
    public static final String move_right = "Right";
    
    public static final String rotate_camera_left = "Rotate The Camera Leftwards";
    public static final String rotate_camera_right = "Rotate The Camera Rightwards";
    
    public static final String mouse_left = "Left Mouse Button";
    public static final String console = "Developer Console";
    public static final int consoleKey = KeyInput.KEY_GRAVE;
    
    public static final String mouse_scroll_up = "Mouse Scroll Up";
    public static final String mouse_scroll_down = "Mouse Scroll Down";
    
    public static final String mouse_move = "Mouse Move";
    
    public static final String save = "save";
    public static final String load = "load";
    
    public static final String interact = "interact";
    
    public static final String openInv = "toggle inventory";
    
    public static final String fastforward = "Fast Forward";
    
    public static final String num_0 = "Number 0";
    public static final String num_1 = "Number 1";
    public static final String num_2 = "Number 2";
    public static final String num_3 = "Number 3";
    public static final String num_4 = "Number 4";
    public static final String num_5 = "Number 5";
    public static final String num_6 = "Number 6";
    public static final String num_7 = "Number 7";
    public static final String num_8 = "Number 8";
    public static final String num_9 = "Number 9";
    
    public static void setupInputManager()
    {
        SAM.INPUT.addMapping(KeyKeys.console, new KeyTrigger(consoleKey));
        
        SAM.INPUT.addMapping(KeyKeys.move_left, new KeyTrigger(KeyInput.KEY_A));
        SAM.INPUT.addMapping(KeyKeys.move_right, new KeyTrigger(KeyInput.KEY_D));
        SAM.INPUT.addMapping(KeyKeys.move_forward, new KeyTrigger(KeyInput.KEY_W));
        SAM.INPUT.addMapping(KeyKeys.move_backward, new KeyTrigger(KeyInput.KEY_S));
        SAM.INPUT.addMapping(KeyKeys.rotate_camera_left, new KeyTrigger(KeyInput.KEY_Q));
        SAM.INPUT.addMapping(KeyKeys.rotate_camera_right, new KeyTrigger(KeyInput.KEY_E));
        SAM.INPUT.addMapping(KeyKeys.mouse_left, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        SAM.INPUT.addMapping(KeyKeys.mouse_scroll_up, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        SAM.INPUT.addMapping(KeyKeys.mouse_scroll_down, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        
        SAM.INPUT.addMapping(KeyKeys.mouse_move, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        SAM.INPUT.addMapping(KeyKeys.mouse_move, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        SAM.INPUT.addMapping(KeyKeys.mouse_move, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        SAM.INPUT.addMapping(KeyKeys.mouse_move, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        
        SAM.INPUT.addMapping(KeyKeys.num_0, new KeyTrigger(KeyInput.KEY_0));
        SAM.INPUT.addMapping(KeyKeys.num_1, new KeyTrigger(KeyInput.KEY_1));
        SAM.INPUT.addMapping(KeyKeys.num_2, new KeyTrigger(KeyInput.KEY_2));
        SAM.INPUT.addMapping(KeyKeys.num_3, new KeyTrigger(KeyInput.KEY_3));
        SAM.INPUT.addMapping(KeyKeys.num_4, new KeyTrigger(KeyInput.KEY_4));
        SAM.INPUT.addMapping(KeyKeys.num_5, new KeyTrigger(KeyInput.KEY_5));
        SAM.INPUT.addMapping(KeyKeys.num_6, new KeyTrigger(KeyInput.KEY_6));
        SAM.INPUT.addMapping(KeyKeys.num_7, new KeyTrigger(KeyInput.KEY_7));
        SAM.INPUT.addMapping(KeyKeys.num_8, new KeyTrigger(KeyInput.KEY_8));
        SAM.INPUT.addMapping(KeyKeys.num_9, new KeyTrigger(KeyInput.KEY_9));
        
        SAM.INPUT.addMapping(KeyKeys.interact, new KeyTrigger(KeyInput.KEY_R));
        
        SAM.INPUT.addMapping(KeyKeys.openInv, new KeyTrigger(KeyInput.KEY_APOSTROPHE));
        
        SAM.INPUT.addMapping(KeyKeys.fastforward, new KeyTrigger(KeyInput.KEY_F));
        
        SAM.INPUT.addMapping(KeyKeys.load, new KeyTrigger(KeyInput.KEY_O));
        SAM.INPUT.addMapping(KeyKeys.save, new KeyTrigger(KeyInput.KEY_P));
        
        SAM.INPUT.addListener(global, KeyKeys.console);
        
        SAM.INPUT.addListener(global, KeyKeys.fastforward);
        SAM.INPUT.addListener(global, KeyKeys.save);
        SAM.INPUT.addListener(global, KeyKeys.load);
    }

    public static KeyKeys global = new KeyKeys();
    public static boolean p_fastforward;
    public static boolean p_save;
    public static boolean p_load;
    
    public static boolean p_console;
    
    public void onAction(String name, boolean isPressed, float tpf)
    {
        if(name.equals(console))
        {
            p_console = isPressed;
        }
        if(name.equals(fastforward))
        {
            p_fastforward = isPressed;
        }
        if(name.equals(save))
        {
            p_save = isPressed;
        }
        if(name.equals(load))
        {
            p_load = isPressed;
        }
    }
}
