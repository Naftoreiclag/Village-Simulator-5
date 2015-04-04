/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.inventory;

import naftoreiclag.villagefive.InvItem;
import naftoreiclag.villagefive.Inventory;

public interface IInventoryUpdateListener {
    public abstract void onUpdate(Inventory inv, int slotIndex);
}
