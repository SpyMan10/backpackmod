package net.spyman.backpackmod

import net.fabricmc.api.ModInitializer
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.spyman.backpackmod.config.ConfigurationManager
import net.spyman.backpackmod.init.ModItemGroups
import net.spyman.backpackmod.init.ModItems
import org.slf4j.LoggerFactory

object BackpackMod : ModInitializer {

  // Mod identifier
  const val modid = "backpackmod"

  // Default logger
  val logger = LoggerFactory.getLogger(modid)

  override fun onInitialize() {
    logger.info("Initializing BackpackMod...")
    logger.info("Loading configuration file located at: ${ConfigurationManager.modConfigPath} ...")
    logger.info("Note: If there is no configuration at the above path, the default config will be written")
    ConfigurationManager.loadOrDefaultAndAssign()

    logger.info("${ConfigurationManager.current.backpacks.size} Backpack type(s) found")

    for (b in ConfigurationManager.current.backpacks)
      logger.info("- ${b.name}")

    logger.info("Registering items...")
    ModItems.init()

    // Force init
    ModItemGroups.init()
  }

  fun identify(resource: String): Identifier = Identifier(modid, resource)
}
