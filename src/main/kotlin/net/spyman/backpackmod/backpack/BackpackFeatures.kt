package net.spyman.backpackmod.backpack

import net.spyman.backpackmod.BackpackMod

enum class BackpackFeatures {
  /**
   * Immune to fire and lava.
   * This item cannot be destroyed in fire source (like netherite items)
   */
  FIREPROOF,

  /** Can be placed in chest slot */
  WEARABLE,

  /** This backpack can have a custom */
  RENAMEABLE;

  val translationKey = "feature.${BackpackMod.modid}.${this.name.lowercase()}"
}