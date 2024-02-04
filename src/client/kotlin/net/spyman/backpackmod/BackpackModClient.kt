package net.spyman.backpackmod

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.spyman.backpackmod.init.BACKPACK_SCREEN_HANDLER
import net.spyman.backpackmod.screen.BackpackScreen

object BackpackModClient : ClientModInitializer {

  override fun onInitializeClient() {
    BackpackMod.LOGGER.info("Initializing client BackpackMod...")
    BackpackMod.LOGGER.info("Registering screens...")
    HandledScreens.register(BACKPACK_SCREEN_HANDLER, ::BackpackScreen)
  }
}