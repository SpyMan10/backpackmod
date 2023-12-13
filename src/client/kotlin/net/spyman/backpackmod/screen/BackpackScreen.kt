package net.spyman.backpackmod.screen

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.spyman.backpackmod.BackpackMod
import kotlin.math.max

class BackpackScreen(
  handler: BackpackScreenHandler,
  inventory: PlayerInventory,
  title: Text
) : HandledScreen<BackpackScreenHandler>(handler, inventory, title) {

  init {
    this.backgroundWidth = max(this.handler.backpackType.size.width * 18 + 14, 176)
    this.backgroundHeight = max(this.handler.backpackType.size.height * 18 + 114, 114)
    this.playerInventoryTitleY = handler.backpackType.size.height * 18 + 20
  }

  override fun drawBackground(ctx: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
    ctx.matrices.push()
    ctx.matrices.translate(this.x.toFloat(), this.y.toFloat(), 0.0F)

    // Upper-left corner
    ctx.drawTexture(texture, 0, 0, 18.0F, 0.0F, 4, 4, 128, 32)

    ctx.drawTexture(
      texture,
      5, 0,
      this.backgroundWidth - 6, 3,
      18.0F, 0.0F,
      4, 4,
      128, 32
    )

    // Drawing each slots
    this.handler.slots.forEach {
      ctx.drawTexture(texture, it.x - 1, it.y - 1, 0.0F, 0.0F, 18, 18, 128, 32)
    }

    ctx.matrices.pop()
  }

  override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
    this.renderBackground(context, mouseX, mouseY, delta)
    super.render(context, mouseX, mouseY, delta)
    this.drawMouseoverTooltip(context, mouseX, mouseY)
  }

  companion object {
    private val texture = BackpackMod.identify("textures/gui/container/parts.png")
  }
}