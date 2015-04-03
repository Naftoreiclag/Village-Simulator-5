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

public final class LuaTexture
{
    public final String textureFile;
    public final String dir;
    
    private LuaTexture(String dir, String textureFile) {
        this.dir = dir;
        this.textureFile = textureFile;
    }
    
    public static LuaTexture create(String dir, LuaValue data)
    {
        if(data.isnil()) {
            return null;
        }
        
        LuaValue file = data.get("textureFile");
        if(file.isnil()) {
            return null;
        }
        
        String textureFile = file.checkjstring();
        return new LuaTexture(dir, textureFile);
    }

    public String getAbsolutePath() {
        return dir + textureFile;
    }

    Texture getTexture() {
        
        return SAM.ASSETS.loadTexture(this.getAbsolutePath());
    }
}
