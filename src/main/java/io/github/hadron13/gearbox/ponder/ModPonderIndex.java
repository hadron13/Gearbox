package io.github.hadron13.gearbox.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.ponder.ModPonderScenes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModPonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(Gearbox.MODID);

    public static void register() {
        HELPER.forComponents(ModBlocks.KILN).addStoryBoard("kiln", ModPonderScenes::kiln);
        HELPER.forComponents(ModBlocks.SAPPER).addStoryBoard("sapper", ModPonderScenes::sapper);
        HELPER.forComponents(ModBlocks.LASER).addStoryBoard("laser", ModPonderScenes::laser);
        HELPER.forComponents(ModBlocks.PUMPJACK_ARM, ModBlocks.PUMPJACK_CRANK, ModBlocks.PUMPJACK_WELL).addStoryBoard("pumpjack", ModPonderScenes::pumpjack);


    }
}
