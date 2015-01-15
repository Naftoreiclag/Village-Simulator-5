/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

public class PluginEntity
{
    public final Plugin parent;
    public final String name;
    
    public PluginEntity(Plugin parent, String name)
    {
        this.parent = parent;
        this.name = name;
    }
}