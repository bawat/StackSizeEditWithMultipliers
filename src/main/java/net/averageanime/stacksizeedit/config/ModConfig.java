package net.averageanime.stacksizeedit.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.Arrays;
import java.util.List;

@Config(name = "stacksizeedit")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Category("StackSettings")
    @Comment("Do not set larger than 2147483647 or below 1.")
    public int maxStacker = 99;
    @Comment("Format: §emod:name:stack_size §ror §e#tag:name:stack_size.")
    @ConfigEntry.Category("StackSettings")
    public List<String> itemOverride = List.of(
            "#c:not_stackable:1",
            "#c:16_stackable:16"
    );

    @Comment("Set this to §aYes §fif you want font size to scale with amount.")
    @ConfigEntry.Category("TextSettings")
    public boolean fontOverride = true;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 50)
    @ConfigEntry.Category("TextSettings")
    @Comment("Only applies if Variable Font Override is set to §cNo.")
    public int itemCountScaleInt = 25;
}
