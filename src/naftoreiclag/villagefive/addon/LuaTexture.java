/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;
import naftoreiclag.villagefive.SAM;
import org.luaj.vm2.LuaValue;

public class LuaTexture
{
    private final boolean nil;
    
    public String textureFile;

    public final String dir;
    
    LuaTexture (String dir, LuaValue data)
    {
        this.dir = dir;
        if(data.isnil())
        {
            this.nil = true;
        }
        else
        {
            LuaValue file = data.get("textureFile");

            if(file.isnil())
            {
                this.nil = true;
            }
            else
            {
                textureFile = file.checkjstring();
                this.nil = false;
            }
        }
    }
    
    public boolean isNil()
    {
        return nil;
    }

    public String getAbsolutePath() {
        return dir + textureFile;
    }

    Texture getTexture() {
        
        return SAM.ASSETS.loadTexture(this.getAbsolutePath());
    }
}
