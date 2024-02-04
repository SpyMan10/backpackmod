package net.spyman.backpackmod.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.codecs.PrimitiveCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.RawShapedRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.dynamic.Codecs
import net.spyman.backpackmod.config.ConfigManager
import net.spyman.backpackmod.init.SHAPED_UPGRADE_RECIPE_SERIALIZER
import kotlin.jvm.optionals.getOrNull

/**
 * Custom Codec type used by ShapedUpgradeRecipe
 */
object CharCodec : PrimitiveCodec<Char> {
  override fun <T : Any> read(ops: DynamicOps<T>, input: T): DataResult<Char> =
    ops.getStringValue(input)
      .flatMap {
        if (it.length != 1)
          DataResult.error<Char> { "Must be a single character instead of a string" }
        else DataResult.success(it[0])
      }

  override fun <T : Any> write(ops: DynamicOps<T>, value: Char): T =
    ops.createString(value.toString())
}

class ShapedUpgradeRecipe(
  group: String,
  category: CraftingRecipeCategory,
  private val raw: RawShapedRecipe,
  private val result: ItemStack,
  showNotification: Boolean,
  private val nbtSourceKey: Char
) : ShapedRecipe(group, category, raw, result, showNotification) {

  init {
    raw.data.ifPresent { shaped ->
      val duplicate = shaped.pattern.sumOf {
        it.count { ch -> ch == nbtSourceKey }
      }

      if (duplicate > 1)
        throw IllegalArgumentException("Recipe pattern lines contains more than one NBT source key char.")
    }
  }

  override fun craft(inventory: RecipeInputInventory, manager: DynamicRegistryManager): ItemStack {
    val recipeData = this.raw.data.getOrNull() ?: return ItemStack.EMPTY
    val ingredient = recipeData.key[this.nbtSourceKey] ?: return ItemStack.EMPTY
    val nbtSourceStack = inventory.heldStacks.find { ingredient.test(it) }
    val craftingResult = super.craft(inventory, manager)

    if (nbtSourceStack != null) {
      // Copy NBT
      craftingResult.orCreateNbt.put(
        ConfigManager.current.inventoryNbtKey,
        nbtSourceStack.orCreateNbt.getCompound(ConfigManager.current.inventoryNbtKey)
      )
    }

    return craftingResult
  }

  override fun getSerializer(): RecipeSerializer<*> = SHAPED_UPGRADE_RECIPE_SERIALIZER

  companion object Serializer : RecipeSerializer<ShapedUpgradeRecipe> {
    private val CODEC: Codec<ShapedUpgradeRecipe> = RecordCodecBuilder.create<ShapedUpgradeRecipe> { inst ->
      inst.group(
        Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "")
          .forGetter { it.group },
        CraftingRecipeCategory.CODEC
          .fieldOf("category")
          .orElse(CraftingRecipeCategory.MISC)
          .forGetter { it.category },
        RawShapedRecipe.CODEC
          .forGetter { it.raw },
        ItemStack.RECIPE_RESULT_CODEC
          .fieldOf("result")
          .forGetter { it.result },
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "show_notification", true)
          .forGetter { it.showNotification() },
        CharCodec
          .fieldOf("nbt_source_key")
          .forGetter { it.nbtSourceKey }
      ).apply(inst, ::ShapedUpgradeRecipe)
    }

    override fun codec(): Codec<ShapedUpgradeRecipe> = CODEC

    override fun read(buf: PacketByteBuf): ShapedUpgradeRecipe =
      ShapedUpgradeRecipe(
        buf.readString(),
        buf.readEnumConstant(CraftingRecipeCategory::class.java),
        RawShapedRecipe.readFromBuf(buf),
        buf.readItemStack(),
        buf.readBoolean(),
        buf.readChar()
      )

    override fun write(buf: PacketByteBuf, recipe: ShapedUpgradeRecipe) {
      buf.writeString(recipe.group)
      buf.writeEnumConstant(recipe.category)
      recipe.raw.writeToBuf(buf)
      buf.writeItemStack(recipe.result)
      buf.writeBoolean(recipe.showNotification())
      buf.writeChar(recipe.nbtSourceKey.code)
    }
  }
}
