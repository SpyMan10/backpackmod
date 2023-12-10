package net.spyman.backpackmod.inventory

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.collection.DefaultedList
import net.spyman.backpackmod.backpack.BackpackType

object BackpackStorage {

  /**
   * Serialize the given stacks as NBT data and return it
   */
  fun serialize(stacks: Collection<ItemStack>, type: BackpackType): NbtCompound {
    val nbt = NbtCompound()
    val list = NbtList()

    stacks.forEachIndexed { index, stack ->
      if (!stack.isEmpty) {
        val stackNbt = NbtCompound()
        stackNbt.putInt("slot-index", index)
        stackNbt.put("stack", stack.writeNbt(NbtCompound()))
      }
    }

    nbt.put("items", list)

    return nbt
  }

  /**
   * Deserialize stacks from the given NBT data and return it
   */
  fun load(nbt: NbtCompound, type: BackpackType): DefaultedList<ItemStack> {
    val stacks = DefaultedList.ofSize(type.size.size, ItemStack.EMPTY)
    val list = nbt.getList("items", NbtList.COMPOUND_TYPE.toInt())

    for (i in 0..<list.size) {
      val nbtCompound = list.getCompound(i)
      val index = nbtCompound.getInt("slot-index")
      stacks[index] = ItemStack.fromNbt(nbtCompound.getCompound("stack"))
    }

    return stacks
  }
}