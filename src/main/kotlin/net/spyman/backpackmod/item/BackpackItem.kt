package net.spyman.backpackmod.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.spyman.backpackmod.backpack.Backpack
import net.spyman.backpackmod.backpack.BackpackType
import java.nio.file.Files
import java.nio.file.Path

class BackpackItem(settings: BackpackItemSettings) : Item(settings) {

  private val type: BackpackType = settings.type

  fun backpack(): Backpack = Backpack(type)

  /** Called when user right-click with this item in hand */
  override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
    if (!world.isClient) {
      val stack = user.getStackInHand(hand)
      val backpack = Backpack(type, stack)
      user.openHandledScreen(backpack.createScreenHandlerFactory(stack.name))
    }

    // Opening sound
    user.playSound(
      SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
      SoundCategory.PLAYERS,
      world.random.nextFloat(),
      world.random.nextFloat()
    )

    return super.use(world, user, hand)
  }

  override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, ctx: TooltipContext) {
    if (ctx.isAdvanced) {
      tooltip.add(Text.translatable("tooltip.backpackmod.size", this.type.size.count))
      tooltip.add(
        Text.literal(" §7- ")
          .append(
            Text.translatable("tooltip.backpackmod.size.width", this.type.size.width)
          )
      )
      tooltip.add(
        Text.literal(" §7- ")
          .append(Text.translatable("tooltip.backpackmod.size.height", this.type.size.height))
      )
      tooltip.add(Text.translatable("tooltip.backpackmod.features"))
      tooltip.addAll(type.features.map {
        Text.literal(" §7- §f").append(Text.translatable(it.translationKey))
      })
    }

    super.appendTooltip(stack, world, tooltip, ctx)
  }
}
