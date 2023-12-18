package net.spyman.backpackmod.recipe

import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RawShapedRecipe
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.registry.DynamicRegistryManager

class ShapedUpgradeRecipe(
  raw: RawShapedRecipe,
  result: ItemStack,
  showNotification: Boolean
) : ShapedRecipe("backpack", CraftingRecipeCategory.EQUIPMENT, raw, result, showNotification) {

  override fun craft(inventory: RecipeInputInventory, registryManager: DynamicRegistryManager): ItemStack {

    return super.craft(inventory, registryManager)
  }

  // Recipe should only work on 3x3 craft matrix
  override fun fits(width: Int, height: Int): Boolean =
    width == 3 && height == 3
}