/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.addon;

import com.jme3.material.MatParam;
import com.jme3.material.MatParamTexture;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import naftoreiclag.villagefive.SAM;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

public class LuaMaterial {
    public LuaTexture diffuse;
    public LuaTexture glow;
    public LuaTexture bump;
    public LuaTexture specular;
    
    public LuaColor diffuseColor;
    public LuaColor ambientColor;
    public LuaColor rimColor;
    
    // If "changeShading" is false, then "shadingEnable" will have no effect.
    // This way, the default action for a LuaMaterial is to just inherit whatever shading is set.
    public boolean changeShading;
    public boolean shadingEnable;
    
    public boolean isMatcap;
    
    public final String dir;

    public LuaMaterial(String dir, LuaValue data) {
        this.dir = dir;
        
        // You can either specify "texture" or "diffuse" for the diffuse data, but if there are both, then "diffuse" overrides.
        LuaValue diffuseData = data.get("diffuse");
        if(diffuseData.isnil()) {
            // From "texture", if "diffuse" is nil
            diffuse = new LuaTexture(dir, data.get("texture"));
        } else {
            diffuse = new LuaTexture(dir, diffuseData);
        }

        glow = new LuaTexture(dir, data.get("glow"));
        bump = new LuaTexture(dir, data.get("bump"));
        specular = new LuaTexture(dir, data.get("specular"));
        
        try {
            isMatcap = data.get("matcap").checkboolean();
        } catch(LuaError error) {
            isMatcap = false;
        }
        
        rimColor = new LuaColor(data.get("rimColor"));
        diffuseColor = new LuaColor(data.get("diffuseColor"));
        ambientColor = new LuaColor(data.get("ambientColor"));
        
        try {
            shadingEnable = data.get("shading").checkboolean();
            changeShading = true;
        } catch(LuaError error) {
            changeShading = false;
        }
        
    }

    // Modify this spatial and all its children to use this material. Performs a pre-order search.
    public void modify(Spatial spatial) {
        if(spatial instanceof Geometry) {
            Geometry geo = (Geometry) spatial;

            geo.setMaterial(modifyAndReturn(geo.getMaterial()));
        }

        if(spatial instanceof Node) {
            List<Spatial> children = ((Node) spatial).getChildren();

            for(int i = 0; i < children.size(); ++i) {
                Spatial s = children.get(i);

                modify(s);
            }
        }
    }
    
    public static final int WEIRD = 0;
    public static final int UNSHADED = 1;
    public static final int LIGHTING = 2;
    public static final int BLOW = 3;

    // Modify this material and return the new version.
    // Returned value can be a completely different object, so be sure to do
    // yourMaterial = luaMat.modifyAndReturn(yourMaterial);
    public Material modifyAndReturn(Material material) {
        /*
         * Shading levels:
         * -1: Weird
         *  0: Unshaded.j3md
         *  1: Lighting.j3md
         *  2: ShaderBlow
         */
        
        // Figure out what the current material's shading capacity is at.
        int currentShadingLevel;
        String matDef = material.getMaterialDef().getAssetName();
        if(matDef.equals("Common/MatDefs/Misc/Unshaded.j3md")) {
            currentShadingLevel = UNSHADED;
        } else if (matDef.equals("Common/MatDefs/Light/Lighting.j3md")) {
            currentShadingLevel = LIGHTING;
        } else if (matDef.equals("ShaderBlow/MatDefs/LightBlow/LightBlow.j3md")) {
            currentShadingLevel = BLOW;
        } else {
            currentShadingLevel = WEIRD;
        }
        
        /****
         * IF THE SHADING WAS EXPLICITLY SET AT A CERTAIN VALUE, THEN UPGRADE OR DOWNGRADE IF NECESSARY.
         * THIS PROCESS WILL AVOID HAVING TO CONVERT BETWEEN TYPES. (i.e. If we need LIGHTING but already have BLOW, then do nothing.)
         ****/
        
        // If the user explicitly wants shading to be off, then do so
        if(changeShading && !shadingEnable) {
            // If the current level is anything but unshaded, then convert it to an unshaded material
            if(currentShadingLevel != UNSHADED) {
                // Make a new material to replace the current one with
                Material newMaterial = new Material(SAM.ASSETS, "Common/MatDefs/Misc/Unshaded.j3md");
                
                // If there is a recipe for converting from Type X to Unshaded, then use that
                if(currentShadingLevel == LIGHTING || currentShadingLevel == BLOW) {
                    // DiffuseMap -> ColorMap
                    MatParamTexture diffuseMap = material.getTextureParam("DiffuseMap");
                    if(diffuseMap != null) {
                        newMaterial.setTexture("ColorMap", diffuseMap.getTextureValue());
                    }
// LB -> U
                    // GlowMap -> GlowMap
                    MatParamTexture glowMap = material.getTextureParam("GlowMap");
                    if(glowMap != null) {
                        newMaterial.setTexture("GlowMap", glowMap.getTextureValue());
                    }
                }
                
                // Replace it
                material = newMaterial;
                currentShadingLevel = UNSHADED;
            }
        }
        
        // If the user explicitly wants shading to be on
        else if(changeShading && shadingEnable) {
            // If the current level is WEIRD or UNSHADED, then convert it to LIGHTING
            if(currentShadingLevel == WEIRD || currentShadingLevel == UNSHADED) {
                // Make a new material to replace the current one with
                Material newMaterial = new Material(SAM.ASSETS, "Common/MatDefs/Light/Lighting.j3md");
                
                // Some default color values
                newMaterial.setBoolean("UseMaterialColors", true);
                newMaterial.setColor("Ambient", ColorRGBA.White);
                newMaterial.setColor("Diffuse", ColorRGBA.White);
                
                // If there is a recipe for converting from Type X to LIGHTING, then use that
                if(currentShadingLevel == UNSHADED) {
                    // ColorMap -> DiffuseMap
                    MatParamTexture colorMap = material.getTextureParam("ColorMap");
                    if(colorMap != null) {
                        newMaterial.setTexture("DiffuseMap", colorMap.getTextureValue());
                    }
                    else
                    {
                        // TODO: support for solid colors
                    }
// U -> L
                    // GlowMap -> GlowMap
                    MatParamTexture glowMap = material.getTextureParam("GlowMap");
                    if(glowMap != null) {
                        newMaterial.setTexture("GlowMap", glowMap.getTextureValue());
                    }
                }
                
                // Replace it
                material = newMaterial;
                currentShadingLevel = LIGHTING;
            }
            
            // This means that if the current level is BLOW, then don't do anything.
        }
        
        // If we currently have a LIGHTING material, and BLOW features are needed, then upgrade.
        if(currentShadingLevel == LIGHTING) {
            // If blow features are needed, then upgrade
            if(!rimColor.isNil() || 
               isMatcap) {
                // Make a new material to replace the current one with
                Material newMaterial = new Material(SAM.ASSETS, "ShaderBlow/MatDefs/LightBlow/LightBlow.j3md");
                
                // Inherit color values
                newMaterial.setBoolean("UseMaterialColors", true);
                MatParam ambientColorGand = material.getParam("Ambient");
                if(ambientColorGand != null) {
                    Object ambientColorData = ambientColorGand.getValue();
                    if(ambientColorData instanceof ColorRGBA) {
                        newMaterial.setColor("Ambient", (ColorRGBA) ambientColorData);
                    }
                }
                MatParam diffuseColorGand = material.getParam("Diffuse");
                if(diffuseColorGand != null) {
                    Object diffuseColorData = diffuseColorGand.getValue();
                    if(diffuseColorData instanceof ColorRGBA) {
                        newMaterial.setColor("Diffuse", (ColorRGBA) diffuseColorData);
                    }
                }
                
                // DiffuseMap -> DiffuseMap
                MatParamTexture diffuseMap = material.getTextureParam("DiffuseMap");
                if(diffuseMap != null) {
                    newMaterial.setTexture("DiffuseMap", diffuseMap.getTextureValue());
                }
// L -> B
                // GlowMap -> GlowMap
                MatParamTexture glowMap = material.getTextureParam("GlowMap");
                if(glowMap != null) {
                    newMaterial.setTexture("GlowMap", glowMap.getTextureValue());
                }
                
                // Replace it
                material = newMaterial;
                currentShadingLevel = BLOW;
            }
        }
        
        /*****
         * NOW THAT THE PROPER MATERIAL TYPE HAS BEEN SETTLED, APPLY ANY TEXTURE OR COLOR CHANGES.
         *****/
        
        // Works with unshaded
        if(currentShadingLevel == UNSHADED) {
            if(!diffuse.isNil()) { material.setTexture("ColorMap", diffuse.getTexture()); }
            if(!diffuseColor.isNil()) { material.setColor("Color", diffuseColor.toColor()); }
        }
        // Works with lighting and blow
        if(currentShadingLevel == LIGHTING || currentShadingLevel == BLOW) {
            if(!diffuse.isNil()) { material.setTexture("DiffuseMap", diffuse.getTexture()); }
            if(!diffuseColor.isNil()) { material.setColor("Diffuse", diffuseColor.toColor()); }
            if(!ambientColor.isNil()) { material.setColor("Ambient", ambientColor.toColor()); }
        }
        // Works with any
        if(currentShadingLevel == UNSHADED || currentShadingLevel == LIGHTING || currentShadingLevel == BLOW) {
            if(!glow.isNil()) { material.setTexture("GlowMap", glow.getTexture()); }
        }
        
        System.out.println("fewafdsgerfsdh:" + currentShadingLevel);
        
        return material;
    }
}
