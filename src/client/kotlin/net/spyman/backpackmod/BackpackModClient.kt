package net.spyman.backpackmod

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.spyman.backpackmod.handledscreen.BackpackScreen
import net.spyman.backpackmod.init.ModScreenHandlers
import org.slf4j.LoggerFactory

object BackpackModClient : ClientModInitializer {

  override fun onInitializeClient() {
    BackpackMod.logger.info("Initializing client BackpackMod...")
    BackpackMod.logger.info("Registering screens...")
    HandledScreens.register(ModScreenHandlers.backpackScreenHandler, ::BackpackScreen)
  }
}