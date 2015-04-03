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
    
    public static enum ShadingOptions {
        TRUE,
        FALSE,
        NULL
    }
    public ShadingOptions shadingOption;
    
    public boolean isMatcap;
    
    public final String dir;
    
    private LuaMaterial(String dir) {
        this.dir = dir;
    }

    public static LuaMaterial create(String dir, LuaValue data) {
        if(data.isnil()) {
            return null;
        }
        
        LuaMaterial ret = new LuaMaterial(dir);
        
        // You can either specify "texture" or "diffuse" for the diffuse data, but if there are both, then "diffuse" overrides.
        LuaValue diffuseData = data.get("diffuse");
        if(diffuseData.isnil()) {
            // From "texture", if "diffuse" is nil
            ret.diffuse = LuaTexture.create(dir, data.get("texture"));
        } else {
            ret.diffuse = LuaTexture.create(dir, diffuseData);
        }

        ret.glow = LuaTexture.create(dir, data.get("glow"));
        ret.bump = LuaTexture.create(dir, data.get("bump"));
        ret.specular = LuaTexture.create(dir, data.get("specular"));
        
        try {
            ret.isMatcap = data.get("matcap").checkboolean();
        } catch(LuaError _) {
            ret.isMatcap = false;
        }
        
        ret.rimColor = LuaColor.create(data.get("rimColor"));
        ret.diffuseColor = LuaColor.create(data.get("diffuseColor"));
        ret.ambientColor = LuaColor.create(data.get("ambientColor"));
        
        try {
            ret.shadingOption = data.get("shading").checkboolean() ? ShadingOptions.TRUE : ShadingOptions.FALSE;
        } catch(LuaError _) {
            ret.shadingOption = ShadingOptions.NULL;
        }
        
        return ret;
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
    public static final int MATCAP = 4;

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
        } else if (matDef.equals("ShaderBlow/MatDefs/MatCap/MatCap.j3md")) {
            currentShadingLevel = MATCAP;
        } else {
            currentShadingLevel = WEIRD;
        }
        
        /****
         * IF THE SHADING WAS EXPLICITLY SET AT A CERTAIN VALUE, THEN UPGRADE OR DOWNGRADE IF NECESSARY.
         * THIS PROCESS WILL AVOID HAVING TO CONVERT BETWEEN TYPES. (i.e. If we need LIGHTING but already have BLOW, then do nothing.)
         ****/
        
        // If the user explicitly wants shading to be off, then do so
        if(shadingOption == ShadingOptions.FALSE) {
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
        else if(shadingOption == ShadingOptions.TRUE) {
            // If isMatcap is true, then use matcap instead
            if(isMatcap) {
                material = new Material(SAM.ASSETS, "ShaderBlow/MatDefs/MatCap/MatCap.j3md");
                currentShadingLevel = MATCAP;
// -> M
            } else {
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
            }
            
            // This means that if the current level is BLOW, then don't do anything.
        }
        
        // If we currently have a LIGHTING material, and BLOW features are needed, then upgrade.
        if(currentShadingLevel == LIGHTING) {
            // If blow features are needed, then upgrade
            if(rimColor != null || 
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
        
        if(currentShadingLevel == UNSHADED) {
            if(diffuse != null) { material.setTexture("ColorMap", diffuse.getTexture()); }
            if(diffuseColor != null) { material.setColor("Color", diffuseColor.toColor()); }
        }
        if(currentShadingLevel == LIGHTING || currentShadingLevel == BLOW) {
            if(diffuse != null) { material.setTexture("DiffuseMap", diffuse.getTexture()); }
            if(diffuseColor != null) { material.setColor("Diffuse", diffuseColor.toColor()); }
            if(ambientColor != null) { material.setColor("Ambient", ambientColor.toColor()); }
            
            // Of course, if any color is defined then it should be used
            if(diffuseColor != null || ambientColor != null) {
                material.setBoolean("UseMaterialColors", true);
            }
        }
        if(currentShadingLevel == UNSHADED || currentShadingLevel == LIGHTING || currentShadingLevel == BLOW) {
            if(glow != null) { material.setTexture("GlowMap", glow.getTexture()); }
        }
        if(currentShadingLevel == MATCAP) {
            if(diffuse != null) { material.setTexture("DiffuseMap", diffuse.getTexture()); }
        }
        
        return material;
    }
}
