package net.spyman.backpackmod

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import net.spyman.backpackmod.config.ConfigManager
import net.spyman.backpackmod.init.ModItemGroups
import net.spyman.backpackmod.init.ModItems
import net.spyman.backpackmod.init.ModScreenHandlers
import org.slf4j.LoggerFactory

object BackpackMod : ModInitializer {

  // Mod identifier
  const val modid = "backpackmod"

  // Default logger
  val logger = LoggerFactory.getLogger(modid)

  override fun onInitialize() {
    logger.info("Initializing BackpackMod...")
    logger.info("Loading configuration file located at: ${ConfigManager.modConfigPath} ...")
    logger.info("Note: If there is no configuration, the default config will be written and used by default")
    ConfigManager.loadOrDefaultAndAssign()

    logger.info("Found ${ConfigManager.current.backpacks.size} different backpack type(s)")

    for (b in ConfigManager.current.backpacks)
      logger.info("- ${b.name}")

    logger.info("Registering items...")
    ModItems.init()

    // Force init
    ModItemGroups.init()

    ModScreenHandlers.init()
  }

  fun identify(resource: String): Identifier = Identifier(modid, resource)
}
