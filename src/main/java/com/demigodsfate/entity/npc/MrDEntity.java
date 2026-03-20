package com.demigodsfate.entity.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

/**
 * Mr. D (Dionysus) — camp director of Camp Half-Blood.
 * Grumpy, calls everyone by the wrong name, wants to be anywhere else.
 */
public class MrDEntity extends CampNpcEntity {
    public MrDEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, "Mr. D",
                "Oh, another one. What was your name again? Pedro? Perry?",
                "I don't care about your quest. I'm missing my wine.",
                "Zeus punished me by sending me here. Can you imagine?",
                "If you die on your quest, try not to make a mess.",
                "The strawberries are coming in nicely this year. That's MY doing.",
                "Go bother Chiron. I'm busy losing at pinochle.",
                "No, I will NOT turn you into a dolphin. Probably.",
                "Back in my day, heroes had STYLE. You lot are so... mortal.");
    }
}
