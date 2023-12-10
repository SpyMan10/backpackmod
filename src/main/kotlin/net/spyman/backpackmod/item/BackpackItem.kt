package net.spyman.backpackmod.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.backpack.BackpackType

class BackpackItem(settings: BackpackItemSettings) : Item(settings) {

  val backpackType: BackpackType = settings.type

  /** Called when user right-click with this item in hand */
  override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
    if (!world.isClient) {
      user.sendMessage(Text.of("Hello World from BackpackMod"))
    }

    return super.use(world, user, hand)
  }

  override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
    tooltip.add(
      BackpackMod.tr("tooltip.inventory.size")
        .formatted(Formatting.GRAY, Formatting.RESET)
        .append(BackpackMod.tr("tooltip.inventory.size.value"))
        .formatted(Formatting.WHITE)
    )

    super.appendTooltip(stack, world, tooltip, context)
  }
}