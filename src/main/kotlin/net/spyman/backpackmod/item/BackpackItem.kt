package net.spyman.backpackmod.item

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.spyman.backpackmod.backpack.BackpackType
import net.spyman.backpackmod.screen.BackpackScreenHandler

class BackpackItem(settings: BackpackItemSettings) : Item(settings) {

  val backpackType: BackpackType = settings.type

  /** Called when user right-click with this item in hand */
  override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
    if (!world.isClient) {
      val stack = user.getStackInHand(hand)
      user.openHandledScreen(BackpackScreenHandlerFactory(backpackType, stack.name))
    }

    return super.use(world, user, hand)
  }

  override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, ctx: TooltipContext) {
    if (ctx.isAdvanced) {
      tooltip.add(Text.translatable("tooltip.backpackmod.size", this.backpackType.size.size))
      tooltip.add(Text.translatable("tooltip.backpackmod.features"))
      tooltip.addAll(backpackType.features.map {
        Text.literal(" §7- §f").append(Text.translatable(it.translationKey))
      })
    }

    super.appendTooltip(stack, world, tooltip, ctx)
  }
}

private class BackpackScreenHandlerFactory(
  val type: BackpackType,
  val text: Text
) : ExtendedScreenHandlerFactory {
  override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
    buf.writeString(type.name)
  }

  override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler
    = BackpackScreenHandler(syncId, playerInventory, type, SimpleInventory(type.size.size))

  override fun getDisplayName(): Text = text
}