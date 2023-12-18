package net.spyman.backpackmod.screen

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.inventory.BackpackScreenHandler
import kotlin.math.max

class BackpackScreen(
  handler: BackpackScreenHandler,
  inventory: PlayerInventory,
  title: Text
) : HandledScreen<BackpackScreenHandler>(handler, inventory, title) {

  init {
    this.backgroundWidth = max(this.handler.backpack.type.size.width * 18 + 14, 176)
    this.backgroundHeight = max(this.handler.backpack.type.size.height * 18 + 114, 114)
    this.playerInventoryTitleY = handler.backpack.type.size.height * 18 + 20
  }

  override fun drawBackground(ctx: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
    ctx.matrices.push()
    ctx.matrices.translate(this.x.toFloat(), this.y.toFloat(), 0.0F)

    // Top
    ctx.drawTexture(
      texture,
      4, 0,
      this.backgroundWidth - 7, 3,
      23.0F, 0.0F,
      1, 3,
      128, 32
    )

    // Left
    ctx.drawTexture(
      texture,
      this.backgroundWidth - 3, 3,
      3, this.backgroundHeight - 8,
      25.0F, 4.0F,
      3, 1,
      128, 32
    )

    // Bottom
    ctx.drawTexture(
      texture,
      3, this.backgroundHeight - 4,
      this.backgroundWidth - 7, 3,
      22.0F, 7.0F,
      1, 3,
      128, 32
    )

    // Right
    ctx.drawTexture(
      texture,
      0, 4,
      3, this.backgroundHeight - 8,
      18.0F, 5.0F,
      3, 1,
      128, 32
    )

    // GUI Background #C6C6C6
    ctx.fill(3, 3, this.backgroundWidth - 3, this.backgroundHeight - 4, 0xFFC6C6C6.toInt())

    // Upper-left corner
    ctx.drawTexture(texture, 0, 0, 18.0F, 0.0F, 4, 4, 128, 32)
    // Upper-right corner
    ctx.drawTexture(texture, this.backgroundWidth - 3, 1, 25.0F, 1.0F, 2, 2, 128, 32)
    // Lower-right corner
    ctx.drawTexture(texture, this.backgroundWidth - 4, this.backgroundHeight - 5, 24.0F, 6.0F, 4, 4, 128, 32)
    // Lower-left corner
    ctx.drawTexture(texture, 1, this.backgroundHeight - 4, 19.0F, 7.0F, 2, 2, 128, 32)

    // Drawing each slots
    this.handler.slots.forEach {
      ctx.drawTexture(texture, it.x - 1, it.y - 1, 0.0F, 0.0F, 18, 18, 128, 32)
    }

    ctx.matrices.pop()
  }

  override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
    super.render(context, mouseX, mouseY, delta)
    this.drawMouseoverTooltip(context, mouseX, mouseY)
  }

  companion object {
    private val texture = BackpackMod.identify("textures/gui/container/parts.png")
  }
}