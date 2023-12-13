package net.spyman.backpackmod.screen

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.backpack.BackpackType
import net.spyman.backpackmod.init.ModScreenHandlers

class BackpackScreenHandler(
  syncId: Int,
  private val playerInventory: PlayerInventory,
  val backpackType: BackpackType,
  private val backpackInventory: Inventory
) : ScreenHandler(ModScreenHandlers.backpackScreenHandler, syncId) {

  init {
    this.initializeSlots()
  }

  /** Internal usage only */
  private fun initializeSlots() {
    // Backpack inventory (depending on inventory size)
    for (n in 0..< this.backpackType.size.height) {
      for (m in 0..<this.backpackType.size.width) {
        this.addSlot(
          BackpackSlot(
            this.backpackInventory, m + n * this.backpackType.size.width, 8 + m * 18, 18 + n * 1
          )
        )
      }
    }

    // Player inventory (27 slots)
    for (n in 0..2) {
      for (m in 0..8) {
        this.addSlot(
          Slot(
            this.playerInventory,
            m + n * 9 + 9,
            8 + (this.backpackType.size.width * 18 - 162) / 2 + m * 18,
            31 + (this.backpackType.size.height + n) * 18
          )
        )
      }
    }

    // Player hot bar (9 slots)
    for (n in 0..8) {
      this.addSlot(
        Slot(
          this.playerInventory,
          n,
          8 + (this.backpackType.size.width * 18 - 162) / 2 + n * 18,
          89 + this.backpackType.size.height * 18
        )
      )
    }
  }

  // Everything can use it
  override fun canUse(player: PlayerEntity): Boolean = true

  override fun quickMove(player: PlayerEntity, index: Int): ItemStack {
    var stack = ItemStack.EMPTY
    val slot = this.slots[index]

    if (slot.hasStack()) {
      val stack2 = slot.stack
      stack = stack2.copy()

      if (index < this.backpackInventory.size()) {
        if (!this.insertItem(stack2, this.backpackInventory.size(), this.slots.size, true)) {
          return ItemStack.EMPTY
        }
      } else if (!this.insertItem(stack2, 0, this.backpackInventory.size(), false)) {
        return ItemStack.EMPTY
      }

      if (stack2.isEmpty) {
        slot.stack = ItemStack.EMPTY
      } else {
        slot.markDirty()
      }
    }

    return stack
  }

  override fun onClosed(player: PlayerEntity) {
    this.backpackInventory.onClose(player)
    super.onClosed(player)
  }

  //TODO: missing slotClick() override to prevent player from moving backpack ItemStack while ScreenHandler is opened

  companion object {
    val id = BackpackMod.identify("backpack_generic_screen_handler")
  }
}