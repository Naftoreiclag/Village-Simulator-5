/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.world.World;

public abstract class Command
{
    public abstract boolean process(Console console, String input, OverworldAppState game);

    public abstract String[] getHelpLines();
}
