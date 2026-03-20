package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.entity.ModEntities;
import com.demigodsfate.entity.npc.ChironEntity;
import com.demigodsfate.entity.npc.MrDEntity;
import com.demigodsfate.world.CampSafeZone;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Commands for managing camps.
 * /camp create <name> <radius> — Create a safe zone at current position
 * /camp list — List all safe zones
 * /camp spawn chiron — Spawn Chiron NPC at current position
 * /camp spawn mrd — Spawn Mr. D NPC at current position
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class CampCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("camp")
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("radius", IntegerArgumentType.integer(10, 200))
                                        .executes(context -> {
                                            if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;

                                            String name = StringArgumentType.getString(context, "name");
                                            int radius = IntegerArgumentType.getInteger(context, "radius");
                                            BlockPos pos = player.blockPosition();

                                            CampSafeZone.addSafeZone(pos, radius, name);

                                            player.sendSystemMessage(Component.literal("Camp safe zone '" + name + "' created!")
                                                    .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
                                            player.sendSystemMessage(Component.literal("Center: " + pos.toShortString() + ", Radius: " + radius)
                                                    .withStyle(ChatFormatting.GRAY));
                                            player.sendSystemMessage(Component.literal("Hostile mobs cannot spawn within this area.")
                                                    .withStyle(ChatFormatting.YELLOW));
                                            return 1;
                                        }))))
                .then(Commands.literal("list")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;

                            var zones = CampSafeZone.getSafeZones();
                            if (zones.isEmpty()) {
                                player.sendSystemMessage(Component.literal("No camp safe zones defined.")
                                        .withStyle(ChatFormatting.GRAY));
                                return 0;
                            }

                            player.sendSystemMessage(Component.literal("=== Camp Safe Zones ===")
                                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                            for (var zone : zones) {
                                player.sendSystemMessage(Component.literal("  " + zone.name() + " — " +
                                        zone.center().toShortString() + " (r=" + zone.radius() + ")")
                                        .withStyle(ChatFormatting.WHITE));
                            }
                            return 1;
                        }))
                .then(Commands.literal("spawn")
                        .then(Commands.literal("chiron")
                                .executes(context -> {
                                    if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;
                                    ServerLevel level = player.serverLevel();

                                    ChironEntity chiron = ModEntities.CHIRON.get().create(level);
                                    if (chiron != null) {
                                        chiron.moveTo(player.getX() + 2, player.getY(), player.getZ());
                                        chiron.setCustomName(Component.literal("Chiron").withStyle(ChatFormatting.GOLD));
                                        chiron.setCustomNameVisible(true);
                                        level.addFreshEntity(chiron);
                                        player.sendSystemMessage(Component.literal("Chiron has been summoned!")
                                                .withStyle(ChatFormatting.GOLD));
                                    }
                                    return 1;
                                }))
                        .then(Commands.literal("mrd")
                                .executes(context -> {
                                    if (!(context.getSource().getEntity() instanceof ServerPlayer player)) return 0;
                                    ServerLevel level = player.serverLevel();

                                    MrDEntity mrD = ModEntities.MR_D.get().create(level);
                                    if (mrD != null) {
                                        mrD.moveTo(player.getX() + 2, player.getY(), player.getZ());
                                        mrD.setCustomName(Component.literal("Mr. D").withStyle(ChatFormatting.DARK_PURPLE));
                                        mrD.setCustomNameVisible(true);
                                        level.addFreshEntity(mrD);
                                        player.sendSystemMessage(Component.literal("Mr. D has appeared. He looks annoyed.")
                                                .withStyle(ChatFormatting.DARK_PURPLE));
                                    }
                                    return 1;
                                }))));
    }
}
