package net.averageanime.stacksizeedit;

import net.averageanime.stacksizeedit.mixin.ItemAccess;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.averageanime.stacksizeedit.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StackSizeEdit implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger("Stack Size Edit");
	private static net.averageanime.stacksizeedit.StackSizeEdit stackSizeEdit;
	static ConfigHolder<ModConfig> StackSizeEditConfig;

	@Override
	public void onInitialize() {
		stackSizeEdit = this;
		StackSizeEditConfig = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		StackSizeEditConfig.registerSaveListener((configHolder, stacksizeeditConfig1) -> {
			loadStackSizeEdit("save");
			return ActionResult.success(true);
		});
		ServerLifecycleEvents.SERVER_STARTED.register(server -> loadStackSizeEdit("load"));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> loadStackSizeEdit("reload"));
	}

	public static void loadStackSizeEdit(String configMsg) {
		LOGGER.info("Stack Size Edit: Attempting to " + configMsg + " config...");
		Set<String> invalidSet = new HashSet<>();

		for (Item item : Registries.ITEM) {
			if (!item.isDamageable()) {
				if (StackSizeEditConfig.getConfig().enableMaxSize) {
					net.averageanime.stacksizeedit.StackSizeEdit.setMax(item, StackSizeEditConfig.getConfig().maxStacker);
				} else {
					net.averageanime.stacksizeedit.StackSizeEdit.setMax(item, item.getMaxCount());
				}
			}

			net.averageanime.stacksizeedit.StackSizeEdit.setMax(item, net.averageanime.stacksizeedit.StackSizeEdit.overrideItem(item, StackSizeEditConfig.getConfig().itemOverride, invalidSet));
		}

		if (!invalidSet.isEmpty()) {
			LOGGER.error("Stack Size Edit: Invalid override entries!");
			LOGGER.warn("Stack Size Edit: The following entries were invalid:");
			for (String invalid : invalidSet) {
				LOGGER.warn("Stack Size Edit: \"" + invalid + "\"");
			}
			LOGGER.warn("Stack Size Edit: Make sure to use the format, \"mod:item:max_stack\", or \"#tag:item:max_stack\".");
		}

		LOGGER.info(configMsg.equals("save") ? "Stack Size Edit: Config saved!" : "Stack Size Edit: Config " + configMsg + "ed!");
	}

	public static void setMax(Item item, int max) {
		if (max > 0) {
				((ItemAccess) item).setMaxCount(max);
		}
	}

	public ModConfig getStackerConfig() {
		return StackSizeEditConfig.getConfig();
	}

	public static net.averageanime.stacksizeedit.StackSizeEdit getStacker() {
		return stackSizeEdit;
	}

	public static boolean isValid(String overrideEntry, String[] splitEntry, Set<String> invalidSet) {
		if (splitEntry.length != 3) {
			invalidSet.add(overrideEntry);
			return false;
		}
		try {
			int max = Integer.parseInt(splitEntry[2]);
		} catch (NumberFormatException e) {
			invalidSet.add(overrideEntry);
			return false;
		}
		return true;
	}

	public static Integer overrideItem(Item item, List<String> overrideList, Set<String> invalidSet) {
		for (String overrideEntry : overrideList) {
			if (overrideEntry.startsWith("#")) {
				String[] splitEntry = overrideEntry.trim().substring(1).split(":"); // split into three parts: tag id, item name, max count
				if (isValid(overrideEntry, splitEntry, invalidSet)) {
					List<TagKey<Item>> itemStream = item.getRegistryEntry().streamTags().toList();
					for (TagKey<Item> tagKey : itemStream) {
						if (item.getRegistryEntry().isIn(TagKey.of(RegistryKeys.ITEM, new Identifier(splitEntry[0], splitEntry[1])))) {
							return Integer.parseInt(splitEntry[2]);
						}
					}
				}
			} else {
				String[] splitEntry = overrideEntry.trim().split(":"); // split into three parts: tag id, item name, max count
				if (isValid(overrideEntry, splitEntry, invalidSet)) {
					if (Registries.ITEM.getId(item).toString().equalsIgnoreCase(splitEntry[0] + ":" + splitEntry[1])) {
						return Integer.parseInt(splitEntry[2]);
					}
				}
			}
		}
		return 0;
	}
}