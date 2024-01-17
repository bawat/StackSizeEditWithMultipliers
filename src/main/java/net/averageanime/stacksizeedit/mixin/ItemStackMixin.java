package net.averageanime.stacksizeedit.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Item.Settings.class)
public class ItemStackMixin {
    @ModifyVariable(method = "maxCount", at = @At("HEAD"), argsOnly = true)
    private int forceCount(int original) {
        return Integer.MAX_VALUE;
    }
}
