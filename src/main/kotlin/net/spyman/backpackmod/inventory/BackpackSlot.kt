package net.spyman.backpackmod.screen

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.spyman.backpackmod.item.BackpackItem

/**
 * Filter for backpack, prevent against "backpack-ception"
 */
class BackpackSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
  override fun canInsert(stack: ItemStack): Boolean = stack.item !is BackpackItem
}
