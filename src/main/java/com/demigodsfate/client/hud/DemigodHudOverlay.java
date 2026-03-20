package com.demigodsfate.client.hud;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.ability.Ability;
import com.demigodsfate.ability.AbilityRegistry;
import com.demigodsfate.godparent.GodParent;
import com.demigodsfate.godparent.GodParentData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.map.RegisterMapDecorationRenderersEvent;

import java.util.List;

/**
 * HUD overlay showing god-parent info, drachma count, and ability cooldowns.
 * Renders in the top-left corner of the screen.
 */
@EventBusSubscriber(modid = DemigodsFate.MODID, value = net.neoforged.api.distmarker.Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD)
public class DemigodHudOverlay {

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(DemigodsFate.MODID, "demigod_hud"),
                DemigodHudOverlay::render
        );
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        var player = mc.player;
        GodParent god = GodParentData.getGodParent(player);

        int x = 5;
        int y = 5;

        if (god == null) {
            // Unclaimed
            graphics.drawString(mc.font, "Unclaimed Demigod", x, y, 0xFF5555);
            return;
        }

        // God parent name with color
        int color = god.getColor().getColor() != null ? god.getColor().getColor() : 0xFFFFFF;
        graphics.drawString(mc.font, "Child of " + god.getDisplayName(), x, y, color);
        y += 12;

        // Drachma count
        int drachmas = GodParentData.getDrachmas(player);
        graphics.drawString(mc.font, "\u2726 " + drachmas + " Drachmas", x, y, 0xFFD700); // ✦ symbol
        y += 14;

        // Ability cooldowns
        List<Ability> abilities = AbilityRegistry.getAbilities(god);
        String[] keys = {"[R]", "[V]", "[G]"};
        long gameTime = player.level().getGameTime();

        for (int i = 0; i < abilities.size() && i < 3; i++) {
            Ability ability = abilities.get(i);
            long cooldownEnd = GodParentData.getAbilityCooldown(player, ability.getId());
            boolean ready = gameTime >= cooldownEnd;

            String label = keys[i] + " " + ability.getDisplayName();
            if (ready) {
                graphics.drawString(mc.font, label, x, y, 0x55FF55); // Green = ready
            } else {
                int remaining = (int) Math.ceil((cooldownEnd - gameTime) / 20.0);
                graphics.drawString(mc.font, label + " (" + remaining + "s)", x, y, 0xFF5555); // Red = cooldown
            }
            y += 10;
        }
    }
}
