package net.spyman.backpackmod.init

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.spyman.backpackmod.backpack.Backpack
import net.spyman.backpackmod.config.ConfigManager
import net.spyman.backpackmod.inventory.BackpackScreenHandler

object ModScreenHandlers {

  val BACKPACK_SCREEN_HANDLER = ExtendedScreenHandlerType<BackpackScreenHandler> { sync, playerInv, buf ->
    val name = buf.readString()
    val backpack = Backpack(ConfigManager.current.backpacks.find { it.name == name }!!)

    return@ExtendedScreenHandlerType backpack.createScreenHandler(sync, playerInv)
  }

  fun init() {
    Registry.register(Registries.SCREEN_HANDLER, BackpackScreenHandler.ID, BACKPACK_SCREEN_HANDLER)
  }
}