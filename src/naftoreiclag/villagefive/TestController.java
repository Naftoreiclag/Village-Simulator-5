/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class TestController implements ScreenController
{
    Nifty nifty;
    Screen screen;
    
    Button ok;
    Button cancel;

    public void bind(Nifty nifty, Screen screen)
    {
        this.nifty = nifty;
        this.screen = screen;
        
        //ok = (Button) screen.findElementByName("button_ok");
        //cancel = (Button) screen.findElementByName("button_cancel");
    }
    
    public void hello()
    {
        System.out.println("button clicked");
    }

    public void onStartScreen()
    {
        System.out.println("bind " + screen.getScreenId());
        System.out.println("start screen");
    }

    public void onEndScreen()
    {
        System.out.println("end screen");
    }

}
