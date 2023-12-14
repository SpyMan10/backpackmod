package net.spyman.backpackmod.init

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.inventory.SimpleInventory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.config.ConfigurationManager
import net.spyman.backpackmod.screen.BackpackScreenHandler

object ModScreenHandlers {

  val backpackScreenHandler = ExtendedScreenHandlerType<BackpackScreenHandler> { sync, inv, buf ->
    // Should never be null
    for (b in ConfigurationManager.current.backpacks)
      BackpackMod.logger.info(b.name)

    val name = buf.readString()
    BackpackMod.logger.info(name)

    val type = ConfigurationManager.current.backpacks.find { it.name == name }!!

    return@ExtendedScreenHandlerType BackpackScreenHandler(
      sync,
      inv,
      type,
      SimpleInventory(type.size.size)
    )
  }

  fun init() {
    Registry.register(Registries.SCREEN_HANDLER, BackpackScreenHandler.id, backpackScreenHandler)
  }
}