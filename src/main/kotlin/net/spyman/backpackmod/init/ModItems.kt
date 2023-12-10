package net.spyman.backpackmod.init

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.config.ConfigurationManager
import net.spyman.backpackmod.item.BackpackItem
import net.spyman.backpackmod.item.BackpackItemSettings

object ModItems {

  fun init() {
    ConfigurationManager.current.backpacks.forEach { type ->
      Registry.register(
        Registries.ITEM,
        BackpackMod.identify(type.name),
        BackpackItem(BackpackItemSettings(type).also {
          it.maxCount(1)
          it.rarity(type.rarity)
        })
      )
    }
  }
}