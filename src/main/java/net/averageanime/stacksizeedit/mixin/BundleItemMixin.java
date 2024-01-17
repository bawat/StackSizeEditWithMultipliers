package net.averageanime.stacksizeedit.mixin;

import net.minecraft.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {

    @ModifyConstant(method = "getAmountFilled", constant = @Constant(floatValue = 64.0f))
    private static float replaceAmountFilled(float constant) {
        return (float) net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().maxStacker;
    }

    @ModifyConstant(method = "onStackClicked", constant = @Constant(intValue = 64))
    private static int replaceOnStackClicked(int constant) {
        return net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().maxStacker;
    }

    @ModifyConstant(method = "getItemBarStep", constant = @Constant(intValue = 64))
    private static int replaceGetItemBarStep(int constant) {
        return net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().maxStacker;
    }

    @ModifyConstant(method = "addToBundle", constant = @Constant(intValue = 64))
    private static int replaceAddToBundle(int constant) {
        return net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().maxStacker;
    }

    @ModifyConstant(method = "getItemOccupancy", constant = @Constant(intValue = 64))
    private static int replaceGetItemOccupancy(int constant) {
        return net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().maxStacker;
    }

    @ModifyConstant(method = "appendTooltip", constant = @Constant(intValue = 64))
    private static int replaceAppendTooltip(int constant) {
        return net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().maxStacker;
    }
}
