package net.spyman.backpackmod.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.backpack.Backpack
import net.spyman.backpackmod.backpack.BackpackFeature
import net.spyman.backpackmod.config.ConfigManager
import net.spyman.backpackmod.inventory.BackpackScreenHandler
import net.spyman.backpackmod.item.BackpackItem
import net.spyman.backpackmod.item.BackpackItemSettings
import net.spyman.backpackmod.item.WearableBackpackItem
import net.spyman.backpackmod.recipe.ShapedUpgradeRecipe

val MAIN_GROUP: ItemGroup = FabricItemGroup.builder()
  .displayName(Text.translatable("itemGroup.${BackpackMod.MODID}.main_item_group"))
  .entries { _, entries ->
    Registries.ITEM.ids
      .filter { it.namespace == BackpackMod.MODID }
      .forEach { entries.add(Registries.ITEM[it]) }
  }
  .icon { ItemStack(Items.APPLE) }
  .build()

val BACKPACK_SCREEN_HANDLER = ExtendedScreenHandlerType<BackpackScreenHandler> { sync, playerInv, buf ->
  val name = buf.readString()
  val backpack = Backpack(ConfigManager.current.backpacks.find { it.name == name }!!)

  return@ExtendedScreenHandlerType backpack.createScreenHandler(sync, playerInv)
}

val SHAPED_UPGRADE_RECIPE_SERIALIZER: RecipeSerializer<ShapedUpgradeRecipe> = ShapedUpgradeRecipe.Serializer

fun registerContent() {
  ConfigManager.current.backpacks.forEach { type ->
    val settings = BackpackItemSettings(type).also {
      it.maxCount(1)
      it.rarity(type.rarity)
    }

    Registry.register(
      Registries.ITEM,
      BackpackMod.identify(type.name),
        if (BackpackFeature.WEARABLE in type.features) WearableBackpackItem(settings)
        else BackpackItem(settings)
    )
  }

  Registry.register(
    Registries.ITEM_GROUP,
    BackpackMod.identify("main_item_group"),
    MAIN_GROUP
  )

  Registry.register(Registries.SCREEN_HANDLER, BackpackScreenHandler.ID, BACKPACK_SCREEN_HANDLER)
  Registry.register(
    Registries.RECIPE_SERIALIZER,
    BackpackMod.identify("crafting_shaped_upgrade"),
    SHAPED_UPGRADE_RECIPE_SERIALIZER
  )
}