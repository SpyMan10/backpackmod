package net.spyman.backpackmod.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.backpack.Backpack
import net.spyman.backpackmod.backpack.toIndexedCollection
import net.spyman.backpackmod.backpack.toInventory
import net.spyman.backpackmod.config.ConfigManager
import net.spyman.backpackmod.init.BACKPACK_SCREEN_HANDLER
import kotlin.math.max

class BackpackScreenHandler(
  syncId: Int,
  private val playerInventory: PlayerInventory,
  val backpack: Backpack
) : ScreenHandler(BACKPACK_SCREEN_HANDLER, syncId) {

  private val inventory: Inventory = this.backpack.supply({
    this.backpack
      .readContent(it.orCreateNbt.getCompound(ConfigManager.current.inventoryNbtKey))
      .toInventory(this.backpack.type.size.count)
  }, SimpleInventory(this.backpack.type.size.count))

  init {
    this.initializeSlots()
  }

  override fun canUse(player: PlayerEntity): Boolean = true

  /** Internal usage only */
  private fun initializeSlots() {
    val w = max(this.backpack.type.size.width * 18, 162)

    // Backpack inventory (depending on inventory size)
    for (n in 0..<this.backpack.type.size.height) {
      for (m in 0..<this.backpack.type.size.width) {
        this.addSlot(
          BackpackSlot(
            this.inventory, m + n * this.backpack.type.size.width, 8 + m * 18, 18 + n * 18
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
            8 + (w - 162) / 2 + m * 18,
            31 + (this.backpack.type.size.height + n) * 18
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
          8 + (w - 162) / 2 + n * 18,
          89 + this.backpack.type.size.height * 18
        )
      )
    }
  }

  override fun quickMove(player: PlayerEntity, index: Int): ItemStack {
    var stack = ItemStack.EMPTY
    val slot = this.slots[index]

    if (slot.hasStack()) {
      val stack2 = slot.stack
      stack = stack2.copy()

      if (index < this.inventory.size()) {
        if (!this.insertItem(stack2, this.inventory.size(), this.slots.size, true)) {
          return ItemStack.EMPTY
        }
      } else if (!this.insertItem(stack2, 0, this.inventory.size(), false)) {
        return ItemStack.EMPTY
      }

      if (stack2.isEmpty) slot.stack = ItemStack.EMPTY
      else slot.markDirty()
    }

    return stack
  }

  override fun onClosed(player: PlayerEntity) {
    this.backpack.useItemStack {
      it.orCreateNbt.put(
        ConfigManager.current.inventoryNbtKey,
        this.backpack.writeContent(this.inventory.toIndexedCollection())
      )
    }

    this.inventory.onClose(player)
    super.onClosed(player)
  }

  override fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType, player: PlayerEntity) {
    val offset = player.inventory.selectedSlot + 27 + this.inventory.size()

    // Lock the ItemStack to prevent player from moving backpack while the container is still open
    if (slotIndex >= 0 && offset == slotIndex && actionType != SlotActionType.CLONE) return;

    super.onSlotClick(slotIndex, button, actionType, player)
  }

  companion object {
    val ID = BackpackMod.identify("backpack_generic_screen_handler")
  }
}