package net.spyman.backpackmod.backpack

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.spyman.backpackmod.inventory.BackpackScreenHandler

/** This class allow you to manipulate backpack. */
class Backpack(
  val type: BackpackType,

  /**
   * ItemStack holding backpack inventory.
   * This field should always be null on client side !
   */
  private val stack: ItemStack? = null
) {
  /** Create new ExtendedScreenFactoryHandler and return it. */
  fun createScreenHandlerFactory(displayName: Text): ExtendedScreenHandlerFactory =
    BackpackScreenHandlerFactory(displayName, this)

  /** Create new BackpackScreenHandler for client side. */
  fun createScreenHandler(sync: Int, playerInventory: PlayerInventory): BackpackScreenHandler =
    BackpackScreenHandler(sync, playerInventory, this)

  fun readContent(nbt: NbtCompound): DefaultedList<ItemStack> {
    val list = DefaultedList.ofSize<ItemStack>(this.type.size.count, ItemStack.EMPTY)
    val nbtList = nbt.getList("Items", NbtList.COMPOUND_TYPE.toInt())

    for (i in 0..<nbtList.size) {
      val slotData = nbtList.getCompound(i)
      val slotIndex = slotData.getInt("Slot")
      list[slotIndex] = ItemStack.fromNbt(slotData.getCompound("Content"))
    }

    return list
  }

  fun writeContent(content: List<ItemStack>): NbtCompound {
    val nbt = NbtCompound()
    val nbtList = NbtList()

    for ((index, stack) in content.withIndex()) {
      // Do not write empty ItemStack ()
      if (!stack.isEmpty) {
        val slotNbt = NbtCompound()
        slotNbt.putInt("Slot", index)
        slotNbt.put("Content", stack.writeNbt(NbtCompound()))
        nbtList.add(slotNbt)
      }
    }

    nbt.put("Items", nbtList)

    return nbt
  }

  /** Rename the given backpack */
  fun rename(text: Text) = this.useItemStack { it.setCustomName(text) }

  /** Return true if the current BackpackType has the given feature. */
  fun hasFeature(feature: BackpackFeature): Boolean = feature in type.features

  fun useItemStack(callback: (ItemStack) -> Unit) {
    if (this.stack != null) callback(this.stack)
  }
}

/** Backpack ScreenHandlerFactory Impl. */
private class BackpackScreenHandlerFactory(
  val title: Text, val backpack: Backpack
) : ExtendedScreenHandlerFactory {

  override fun createMenu(sync: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
    BackpackScreenHandler(sync, playerInventory, this.backpack)

  override fun getDisplayName(): Text = title

  override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
    buf.writeString(this.backpack.type.name)
  }
}