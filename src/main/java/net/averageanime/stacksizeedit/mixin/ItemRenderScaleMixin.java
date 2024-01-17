package net.averageanime.stacksizeedit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Pseudo
@Mixin(targets = "net.devtech.stacc.ItemCountRenderHandler")
public class ItemRenderScaleMixin {
    @Inject(method = "scale", at = @At("RETURN"), cancellable = true, remap = false)
    private void forceScale(CallbackInfoReturnable<Float> cir) {
        float intToFloat = (float) (net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().itemCountScaleInt+50)/100;
        if (net.averageanime.stacksizeedit.StackSizeEdit.getStacker().getStackerConfig().fontOverride) {
            cir.setReturnValue(cir.getReturnValue());
        } else {
            cir.setReturnValue(intToFloat);
        }
    }
}
