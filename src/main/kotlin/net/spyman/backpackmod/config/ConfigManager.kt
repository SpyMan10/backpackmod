package net.spyman.backpackmod.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import net.spyman.backpackmod.BackpackMod
import net.spyman.backpackmod.backpack.BackpackType
import net.spyman.backpackmod.inventory.InventorySize
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString

object ConfigManager {

  val modDirPath = Path.of(FabricLoader.getInstance().configDir.toAbsolutePath().toString(), BackpackMod.MODID)
  val modConfigPath = Path.of(modDirPath.absolutePathString(), "config.json")

  lateinit var current: Config
    private set

  // Global GSON used
  private val gson: Gson = GsonBuilder()
    .setPrettyPrinting()
    .registerTypeAdapter(InventorySize::class.java, InventorySize.Serializer)
    .registerTypeAdapter(BackpackType::class.java, BackpackType.Serializer)
    .registerTypeAdapter(Config::class.java, Config.Serializer)
    .create()

  /**
   * Write the given configuration instance at the modConfigPath
   */
  fun save(cfg: Config) {
    // Creating required subdirectories
    Files.createDirectories(modDirPath)
    Files.writeString(modConfigPath, gson.toJson(cfg))
  }

  /**
   * Load the configuration located at modConfigPath
   */
  fun load(): Config? = if (Files.exists(modConfigPath))
    gson.fromJson(Files.readString(modConfigPath, Charsets.UTF_8), Config::class.java)
  else null

  /**
   * Load the configuration file located at modConfigPath or get default variant
   * if there is no file found.
   * The loaded configuration will also be assigned to the current field.
   */
  fun loadOrDefaultAndAssign(): Config {
    this.current = this.load() ?: Config.DEFAULT.also { save(it) }
    return current
  }
}
