package net.spyman.backpackmod.inventory

import kotlin.math.max;

private const val SLOT_FRAGMENT_WIDTH = 18
private const val SLOT_FRAGMENT_HEIGHT = 18

private const val MINIMUM_MAT_PIX_WIDTH = 162
private const val MINIMUM_MAT_PIX_HEIGHT = 54

private const val SUGGESTED_INV_MAT_X = 7
private const val SUGGESTED_INV_MAT_Y = 17

private const val PLAYER_INV_WIDTH = 162
private const val PLAYER_INV_HEIGHT = 76

class ComponentLocator(val inventorySize: InventorySize) {

  /* Slot matrix width in pixels */
  val matPixWidth = this.inventorySize.width * SLOT_FRAGMENT_WIDTH

  /* Slot matrix height in pixels */
  val matPixHeight = this.inventorySize.height * SLOT_FRAGMENT_HEIGHT

  /* Total width including padding and content  */
  val canvasWidth = max(matPixWidth, MINIMUM_MAT_PIX_WIDTH) + 14

  /* Total height including padding and content  */
  val canvasHeight = max(matPixHeight, MINIMUM_MAT_PIX_HEIGHT) + 114

  /* Suggested slot matrix X pos */
  val matPixX =
    if (matPixWidth > MINIMUM_MAT_PIX_WIDTH) SUGGESTED_INV_MAT_X
    else (this.canvasWidth - this.matPixWidth) / 2

  /* Suggested slot matrix Y pos */
  val matPixY =
    if (matPixHeight > MINIMUM_MAT_PIX_HEIGHT) SUGGESTED_INV_MAT_Y
    else (this.canvasHeight - this.matPixHeight) / 2

  val playerInvX = (this.canvasWidth - PLAYER_INV_WIDTH) / 2
  val playerInvY = this.matPixHeight + 31

  val playerHotBarX = this.playerInvX
  val playerHotBarY = this.playerInvY + 58

  fun playerInventory(): PlayerSlotIterator = PlayerSlotIterator(this)
  fun inventory(): SlotIterator = SlotIterator(this)
}

data class SlotPos(val index: Int, val x: Int, val y: Int)

class SlotIterator(private val locator: ComponentLocator) : Iterator<SlotPos> {
  private var index = 0

  override fun hasNext(): Boolean = index < this.locator.inventorySize.count

  override fun next(): SlotPos = SlotPos(
    this.index++,
    this.locator.matPixX + ((this.index % this.locator.inventorySize.width) * SLOT_FRAGMENT_WIDTH),
    this.locator.matPixX + ((this.index / this.locator.inventorySize.height) * SLOT_FRAGMENT_HEIGHT)
  )
}

class PlayerSlotIterator(private val locator: ComponentLocator) : Iterator<SlotPos> {
  private var index = 0

  override fun hasNext(): Boolean = this.index < 36

  override fun next(): SlotPos = SlotPos(
    this.index++,
    this.locator.playerInvX + ((this.index % 9) * SLOT_FRAGMENT_WIDTH),
    this.locator.playerInvY + ((this.index / 4) * SLOT_FRAGMENT_HEIGHT) +
      (if (this.index >= 27) 4 else 0)
  )
}
