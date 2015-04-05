/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import java.util.Map;
import naftoreiclag.villagefive.InvItem;
import naftoreiclag.villagefive.ItemRegistry;
import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.util.CommandyString;
import naftoreiclag.villagefive.world.entity.PlayerEntity;

public class CommandInventory extends Command {

    @Override
    public boolean process(Console console, String input, OverworldAppState game) {
        
        if(input.toLowerCase().startsWith("inv")) {
            CommandyString inputs = new CommandyString(input.split(" "));
            
            PlayerEntity player = game.getPlayer();
            
            
            int numItems = 0;
            if(inputs.get(1).equals("")) {
                console.println("items: ");
                for(Map.Entry<Integer, InvItem> entry : player.inventory.items.entrySet()) {
                    InvItem i = entry.getValue();
                    
                    ++ numItems;
                    console.println("\t" + entry.getKey() + "\t" + i.getItemId());
                }


                console.println("");
                console.println("total item count: " + numItems);
            }
            
            else if(inputs.get(1).equalsIgnoreCase("use")) {
                
                console.println("using");
                
                String slot = inputs.get(2);
                int slotI;
                try {
                    slotI = Integer.valueOf(slot);
                } catch (NumberFormatException e) {
                    slotI = 0;
                }
                
                InvItem which = player.inventory.getItem(slotI);
                
                player.use(which);
                
            }
            
            
            return true;
        } else if(input.toLowerCase().startsWith("give")) {
            CommandyString inputs = new CommandyString(input.split(" "));
            
            String itemName = inputs.get(1);
            PlayerEntity e = game.getPlayer();
            
            InvItem i = ItemRegistry.genNewItem(itemName);
            
            if(i != null) {
                e.inventory.addItem(i);
                console.println("Item given!");
            } else {
                console.println("NO ITEM " + itemName);
            }
            
            return true;
        }
        
        return false;
        
    }

    @Override
    public String[] getHelpLines() {
        return new String[] {
            "INV\tLists your inventory contents.",
            "INV USE <SLOT>\tUse an item in your inventory.",
            "GIVE <item>\tCheats in an item for you."
        };
    }
    
}
