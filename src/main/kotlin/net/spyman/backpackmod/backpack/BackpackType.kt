package net.spyman.backpackmod.backpack

import com.google.gson.*
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.Rarity
import net.spyman.backpackmod.config.ConfigurationManager
import net.spyman.backpackmod.inventory.BackpackStorage
import net.spyman.backpackmod.inventory.InventorySize
import java.lang.reflect.Type

data class BackpackType(
  /** Backpack registry name */
  val name: String,

  /** Backpack inventory size */
  val size: InventorySize,

  /** Item rarity */
  val rarity: Rarity,

  /** Backpack options */
  val features: Set<BackpackFeatures>
) {

  fun inventory(stack: ItemStack): Inventory {
    val nbt = stack.orCreateNbt.getCompound(ConfigurationManager.current.inventoryNbtKey)
    val stacks = BackpackStorage.load(nbt, this)
    val inventory = SimpleInventory()


    return inventory
  }

  /** Auto-Generated Member **/
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as BackpackType

    if (name != other.name) return false
    if (size != other.size) return false
    if (rarity != other.rarity) return false
    if (features != other.features) return false

    return true
  }

  /** Auto-Generated Member **/
  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + size.hashCode()
    result = 31 * result + rarity.hashCode()
    result = 31 * result + features.hashCode()

    return result
  }

  /** BackpackType serializer */
  companion object Serializer : JsonSerializer<BackpackType>, JsonDeserializer<BackpackType> {
    override fun serialize(obj: BackpackType, type: Type, ctx: JsonSerializationContext): JsonElement {
      val root = JsonObject()
      root.addProperty("name", obj.name)
      root.addProperty("rarity", obj.rarity.name.lowercase())
      root.add("size", ctx.serialize(obj.size))
      root.add("features", JsonArray().also {
        obj.features.forEach { v -> it.add(v.name.lowercase()) }
      })

      return root
    }

    override fun deserialize(json: JsonElement, type: Type, ctx: JsonDeserializationContext): BackpackType {
      val obj = json.asJsonObject
      val name = obj.get("name").asString
      val rarity = Rarity.valueOf(obj.get("rarity").asString.uppercase())
      val size = ctx.deserialize<InventorySize>(obj.get("size"), InventorySize::class.java)
      val features =
        obj.get("features").asJsonArray.map { v -> BackpackFeatures.valueOf(v.asString.uppercase()) }.toSet()

      return BackpackType(
        name,
        size,
        rarity,
        features
      )
    }
  }
}