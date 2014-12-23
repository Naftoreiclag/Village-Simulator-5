/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

// huh

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

public class KeyKeys
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
    }
}
