package net.spyman.backpackmod.inventory

import com.google.gson.*
import java.lang.reflect.Type

/**
 * InventorySize describe the size (width and height) of a
 * backpack inventory.
 */
data class InventorySize(
  val width: Int,
  val height: Int
) {

  init {
    // Size must be strictly > to 0
    if (width <= 0 || height <= 0)
      throw IllegalArgumentException("Inventory size width and height must be strictly > to 0")
  }

  val size: Int = this.width * this.height

  override fun equals(other: Any?): Boolean {
    if (other !is InventorySize) return false
    return this.width == other.width && this.height == other.height
  }

  override fun hashCode(): Int {
    var result = width
    result = 31 * result + height
    return result
  }

  /** Json serializer/deserializer for type: InventorySize */
  companion object Serializer : JsonSerializer<InventorySize>, JsonDeserializer<InventorySize> {

    override fun serialize(obj: InventorySize, type: Type, ctx: JsonSerializationContext): JsonElement {
      val root = JsonObject()
      root.addProperty("width", obj.width)
      root.addProperty("height", obj.height)

      return root
    }

    override fun deserialize(json: JsonElement, type: Type, ctx: JsonDeserializationContext): InventorySize {
      val obj = json.asJsonObject
      val width = obj.get("width").asInt
      val height = obj.get("height").asInt

      return InventorySize(width, height)
    }
  }
}
