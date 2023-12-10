package net.spyman.backpackmod.config

import com.google.gson.*
import net.minecraft.util.Rarity
import net.spyman.backpackmod.backpack.BackpackFeatures
import net.spyman.backpackmod.backpack.BackpackType
import net.spyman.backpackmod.inventory.InventorySize
import java.lang.reflect.Type

data class Configuration(
  /** Set to true to enable ender backpack */
  //val isEnderBackpackEnabled: Boolean,

  /**
   * NBT storage key, please don't modify it after started using this mod,
   * or every backpack in the world will stop working.
   */
  val inventoryNbtKey: String,

  /**
   * A list of backpacks to add to the game
   */
  val backpacks: Array<BackpackType>
) {

  /** Auto-Generated Member **/
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Configuration

    //if (isEnderBackpackEnabled != other.isEnderBackpackEnabled) return false
    if (inventoryNbtKey != other.inventoryNbtKey) return false
    if (!backpacks.contentEquals(other.backpacks)) return false

    return true
  }

  /** Auto-Generated Member **/
  override fun hashCode(): Int {
    //var result = isEnderBackpackEnabled.hashCode()
    var result = inventoryNbtKey.hashCode()
    result = 31 * result + backpacks.contentHashCode()
    return result
  }

  companion object {
    val default = Configuration(
      "@BackpackModData", arrayOf(
        BackpackType(
          "leather_backpack", InventorySize(9, 2), Rarity.COMMON, setOf(
            BackpackFeatures.WEARABLE
          )
        ),
        BackpackType(
          "copper_backpack", InventorySize(9, 3), Rarity.COMMON, setOf(
            BackpackFeatures.WEARABLE
          )
        ),
        BackpackType(
          "iron_backpack", InventorySize(9, 4), Rarity.UNCOMMON, setOf(
            BackpackFeatures.WEARABLE,
            BackpackFeatures.RENAME_ALLOWED
          )
        ),
        BackpackType(
          "golden_backpack", InventorySize(9, 5), Rarity.RARE, setOf(
            BackpackFeatures.WEARABLE,
            BackpackFeatures.RENAME_ALLOWED
          )
        ),
        BackpackType(
          "amethyst_backpack", InventorySize(9, 6), Rarity.RARE, setOf(
            BackpackFeatures.WEARABLE,
            BackpackFeatures.RENAME_ALLOWED
          )
        ),
        BackpackType(
          "diamond_backpack", InventorySize(12, 6), Rarity.EPIC, setOf(
            BackpackFeatures.WEARABLE,
            BackpackFeatures.RENAME_ALLOWED
          )
        ),
        BackpackType(
          "netherite_backpack", InventorySize(12, 9), Rarity.EPIC, setOf(
            BackpackFeatures.WEARABLE,
            BackpackFeatures.RENAME_ALLOWED,
            BackpackFeatures.FIREPROOF
          )
        ),
      )
    )
  }

  object Serializer : JsonSerializer<Configuration>, JsonDeserializer<Configuration> {
    override fun serialize(obj: Configuration, type: Type, ctx: JsonSerializationContext): JsonElement {
      val root = JsonObject()
      root.addProperty("data-key", obj.inventoryNbtKey)
      root.add("backpacks", JsonArray().also {
        obj.backpacks.forEach { v -> it.add(ctx.serialize(v)) }
      })

      return root
    }

    override fun deserialize(json: JsonElement, type: Type, ctx: JsonDeserializationContext): Configuration {
      val obj = json.asJsonObject
      val dataKey = obj.get("data-key").asString
      val backpacks = obj.getAsJsonArray("backpacks").map { v ->
        ctx.deserialize<BackpackType>(v, BackpackType::class.java)
      }.toTypedArray()

      return Configuration(dataKey, backpacks)
    }
  }
}

