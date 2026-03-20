package com.demigodsfate.command;

import com.demigodsfate.DemigodsFate;
import com.demigodsfate.quest.QuestManager;
import com.demigodsfate.quest.QuestRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Quest-related commands.
 * /quest start <id> — Start a quest
 * /quest advance — Advance to next stage (debug)
 * /quest status — Show current quest
 */
@EventBusSubscriber(modid = DemigodsFate.MODID)
public class QuestCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("quest")
                .then(Commands.literal("start")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    QuestRegistry.getAllQuests().forEach(q -> builder.suggest(q.getId()));
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    if (!(context.getSource().getEntity() instanceof Player player)) return 0;
                                    String questId = StringArgumentType.getString(context, "id");
                                    QuestManager.startQuest(player, questId);
                                    return 1;
                                })))
                .then(Commands.literal("advance")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof Player player)) return 0;
                            QuestManager.advanceStage(player);
                            return 1;
                        }))
                .then(Commands.literal("status")
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof Player player)) return 0;
                            QuestManager.showStatus(player);
                            return 1;
                        })));
    }
}
