package net.spyman.backpackmod.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.backpack.Backpack
import net.spyman.backpackmod.config.ConfigManager
import net.spyman.backpackmod.init.ModScreenHandlers
import kotlin.math.max

class BackpackScreenHandler(
  syncId: Int,
  private val playerInventory: PlayerInventory,
  val backpack: Backpack
) : ScreenHandler(ModScreenHandlers.BACKPACK_SCREEN_HANDLER, syncId) {

  private val inventory: Inventory = SimpleInventory(this.backpack.type.size.count)

  init {
    this.backpack.useItemStack {
      this.backpack.readContent(it.orCreateNbt.getCompound(ConfigManager.current.inventoryNbtKey))
        .forEachIndexed(this.inventory::setStack)
    }

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
    this.backpack.useItemStack { stack ->
      stack.orCreateNbt.put(ConfigManager.current.inventoryNbtKey, this.backpack.writeContent(this.inventory.let {
        val stacks = mutableListOf<ItemStack>()

        for (index in 0..<it.size()) stacks.add(this.inventory.getStack(index))

        return@let stacks
      }))
    }

    this.inventory.onClose(player)
    super.onClosed(player)
  }

  //TODO: missing slotClick() override to prevent player from moving backpack ItemStack while ScreenHandler is opened

  companion object {
    val ID = BackpackMod.identify("backpack_generic_screen_handler")
  }
}