package net.spyman.backpackmod

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import net.spyman.backpackmod.config.ConfigManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import net.spyman.backpackmod.init.registerContent

object BackpackMod : ModInitializer {

  // Mod identifier
  const val MODID = "backpackmod"

  // Default logger
  val LOGGER: Logger = LoggerFactory.getLogger(MODID)

  override fun onInitialize() {
    LOGGER.info("Initializing BackpackMod...")
    LOGGER.info("Loading configuration file located at: ${ConfigManager.modConfigPath} ...")
    LOGGER.info("Note: If there is no configuration, the default config will be written and used by default")
    ConfigManager.loadOrDefaultAndAssign()

    LOGGER.info("Found ${ConfigManager.current.backpacks.size} different backpack type(s)")

    for (b in ConfigManager.current.backpacks)
      LOGGER.info("- ${b.name}")

    LOGGER.info("Registering content...")
    registerContent()
  }

  fun identify(resource: String): Identifier = Identifier(MODID, resource)
}
