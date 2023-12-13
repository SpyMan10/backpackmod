package net.spyman.backpackmod.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.spyman.backpackmod.BackpackMod

object ModItemGroups {

  val mainGroup = FabricItemGroup.builder()
    .displayName(Text.translatable("itemGroup.backpackmod.main_item_group"))
    .entries { _, entries ->
      Registries.ITEM.ids
        .filter { it.namespace == BackpackMod.modid }
        .forEach { entries.add(Registries.ITEM[it]) }
    }
    .icon { ItemStack(Items.APPLE) }
    .build()

  fun init() {
    Registry.register(
      Registries.ITEM_GROUP,
      BackpackMod.identify("main_item_group"),
      mainGroup
    )
  }
}