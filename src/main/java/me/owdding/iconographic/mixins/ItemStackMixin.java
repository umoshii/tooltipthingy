package me.owdding.iconographic.mixins;

import me.owdding.iconographic.config.Config;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI;

import java.util.function.Consumer;

import static tech.thatgravyboat.skyblockapi.utils.extentions.ItemStackExtensionsKt.getSkyBlockId;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Unique
    private boolean iconographic$shouldHideComponent(DataComponentType<?> type) {
        ItemStack self = (ItemStack) (Object) this;

        if (Config.isEnabled() && Config.hideVanillaTooltipData() && (!Config.skyblockOnly() || LocationAPI.INSTANCE.isOnSkyBlock()) && getSkyBlockId(self) != null) {
            return type == DataComponents.ENCHANTMENTS ||
                    type == DataComponents.ATTRIBUTE_MODIFIERS ||
                    type == DataComponents.UNBREAKABLE ||
                    type == DataComponents.FIREWORKS ||
                    type == DataComponents.FIREWORK_EXPLOSION ||
                    type == DataComponents.TRIM ||
                    type == DataComponents.POTION_CONTENTS ||
                    type == DataComponents.JUKEBOX_PLAYABLE ||
                    type == DataComponents.STORED_ENCHANTMENTS;
        }
        return false;
    }

    @Inject(
            method = "addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hideStandardComponents(DataComponentType<?> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag, CallbackInfo ci) {
        if (this.iconographic$shouldHideComponent(type)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "addUnitComponentToTooltip",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hideUnitComponents(DataComponentType<?> type, Component component, TooltipDisplay display, Consumer<Component> consumer, CallbackInfo ci) {
        if (this.iconographic$shouldHideComponent(type)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "addAttributeTooltips",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hideAttributeComponents(Consumer<Component> consumer, TooltipDisplay display, Player player, CallbackInfo ci) {
        if (this.iconographic$shouldHideComponent(DataComponents.ATTRIBUTE_MODIFIERS)) {
            ci.cancel();
        }
    }
}