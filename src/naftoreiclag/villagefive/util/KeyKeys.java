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

public class KeyKeys implements ActionListener
{
    public static final String move_forward = "Forward";
    public static final String move_backward = "Backward";
    public static final String move_left = "Left";
    public static final String move_right = "Right";
    
    public static final String rotate_camera_left = "Rotate The Camera Leftwards";
    public static final String rotate_camera_right = "Rotate The Camera Rightwards";
    
    public static final String mouse_left = "Left Mouse Button";
    public static final String debug = "Debugg";
    
    public static final String mouse_scroll_up = "Mouse Scroll Up";
    public static final String mouse_scroll_down = "Mouse Scroll Down";
    
    public static final String mouse_move_up = "Mouse Move U";
    public static final String mouse_move_down = "Mouse Move D";
    public static final String mouse_move_left = "Mouse Move L";
    public static final String mouse_move_right = "Mouse Move R";
    
    public static final String save = "save";
    public static final String load = "load";
    
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
    
    public static void hookInputs(InputManager inputManager)
    {
        inputManager.addMapping(KeyKeys.move_left, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(KeyKeys.move_right, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(KeyKeys.move_forward, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(KeyKeys.move_backward, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(KeyKeys.rotate_camera_left, new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping(KeyKeys.rotate_camera_right, new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping(KeyKeys.debug, new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping(KeyKeys.mouse_left, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addMapping(KeyKeys.mouse_scroll_up, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(KeyKeys.mouse_scroll_down, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        
        inputManager.addMapping(KeyKeys.mouse_move_up, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(KeyKeys.mouse_move_down, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(KeyKeys.mouse_move_left, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(KeyKeys.mouse_move_right, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        
        inputManager.addMapping(KeyKeys.num_0, new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping(KeyKeys.num_1, new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping(KeyKeys.num_2, new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping(KeyKeys.num_3, new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping(KeyKeys.num_4, new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping(KeyKeys.num_5, new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping(KeyKeys.num_6, new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping(KeyKeys.num_7, new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping(KeyKeys.num_8, new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping(KeyKeys.num_9, new KeyTrigger(KeyInput.KEY_9));
        
        inputManager.addMapping(KeyKeys.openInv, new KeyTrigger(KeyInput.KEY_X));
        
        inputManager.addMapping(KeyKeys.fastforward, new KeyTrigger(KeyInput.KEY_F));
        
        
        inputManager.addMapping(KeyKeys.load, new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping(KeyKeys.save, new KeyTrigger(KeyInput.KEY_P));
        
        inputManager.addListener(global, KeyKeys.fastforward);
        inputManager.addListener(global, KeyKeys.save);
        inputManager.addListener(global, KeyKeys.load);
    }

    public static KeyKeys global = new KeyKeys();
    public static boolean p_fastforward;
    public static boolean p_save;
    public static boolean p_load;
    
    public void onAction(String name, boolean isPressed, float tpf)
    {
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
