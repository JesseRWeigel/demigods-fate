package com.demigodsfate.client;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.ability.AbilityManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side keybinds for abilities.
 * R = Ability 1, V = Ability 2, G = Ability 3
 */
@EventBusSubscriber(modid = DemigodsFate.MODID, value = net.neoforged.api.distmarker.Dist.CLIENT)
public class AbilityKeyBinds {
    private static final String CATEGORY = "key.categories.demigodsfate";

    public static KeyMapping ABILITY_1;
    public static KeyMapping ABILITY_2;
    public static KeyMapping ABILITY_3;

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ABILITY_1 = new KeyMapping(
                "key.demigodsfate.ability_1",
                InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R,
                CATEGORY);
        ABILITY_2 = new KeyMapping(
                "key.demigodsfate.ability_2",
                InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,
                CATEGORY);
        ABILITY_3 = new KeyMapping(
                "key.demigodsfate.ability_3",
                InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G,
                CATEGORY);

        event.register(ABILITY_1);
        event.register(ABILITY_2);
        event.register(ABILITY_3);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        // Check keybinds and trigger abilities server-side via command
        if (ABILITY_1 != null && ABILITY_1.consumeClick()) {
            AbilityManager.tryUseAbility(mc.player, 0);
        }
        if (ABILITY_2 != null && ABILITY_2.consumeClick()) {
            AbilityManager.tryUseAbility(mc.player, 1);
        }
        if (ABILITY_3 != null && ABILITY_3.consumeClick()) {
            AbilityManager.tryUseAbility(mc.player, 2);
        }
    }
}
