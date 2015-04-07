/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui.constructs;

import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.gui.BitmapFont;
import naftoreiclag.villagefive.gui.Collision;
import naftoreiclag.villagefive.gui.FancySkin;
import naftoreiclag.villagefive.gui.Text;

public class Button extends Collision {
    
    private Text text;
    private FancySkin skin;
    
    public Button(BitmapFont font, double fontSize, String message) {
        super(font.getWidth(message) * (fontSize / font.charHeight) + 20, font.getHeight(message) * (fontSize / font.charHeight) + 20);
        
        text = new Text(font, fontSize, message);
        skin = new FancySkin(SAM.ASSETS.loadTexture("Textures/action_button.png"), width + 20, height + 20);
        
        this.attachElement(skin);
        this.attachElement(text);
    }

}
