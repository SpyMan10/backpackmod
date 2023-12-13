package net.spyman.backpackmod.handledscreen

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.screen.BackpackScreenHandler
import kotlin.math.max

class BackpackScreen(
  handler: BackpackScreenHandler,
  inventory: PlayerInventory,
  title: Text
) : HandledScreen<BackpackScreenHandler>(handler, inventory, title) {

  private val texture = BackpackMod.identify("textures/gui/container/parts.png")

  init {
    this.backgroundWidth = max(this.handler.backpackType.size.width * 18 + 14, 176)
    this.backgroundHeight = max(this.handler.backpackType.size.height * 18 + 114, 114)
    this.playerInventoryTitleY = handler.backpackType.size.height * 18 + 20
  }

  override fun drawBackground(ctx: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
    ctx.matrices.push()
    ctx.matrices.translate(this.x.toFloat(), this.y.toFloat(), 0.0F)

    // Upper-left corner
    ctx.drawTexture(this.texture, 0, 0, 18.0F, 0.0F, 4, 4, 128, 32)

    ctx.drawTexture(
      this.texture,
      5, 0,
      this.backgroundWidth - 6, 3,
      18.0F, 0.0F,
      4, 4,
      128, 32
    )

    ctx.matrices.pop()
  }

  override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
    this.renderBackground(context, mouseX, mouseY, delta)
    super.render(context, mouseX, mouseY, delta)
    this.renderWithTooltip(context, mouseX, mouseY, delta)
  }
}