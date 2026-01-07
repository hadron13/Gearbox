package io.github.hadron13.gearbox.compat.kubejs.helpers;

import com.simibubi.create.content.fluids.FlowSource;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import dev.latvian.mods.kubejs.core.FluidStackKJS;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.function.BiConsumer;

public class FluidIngredientHelper {
    public static SizedFluidIngredient toFluidIngredient(FluidStackKJS fluidStack) {
        return SizedFluidIngredient.of(fluidStack.kjs$self());
    }

    // Commented out because it is unused and needs reformatting

    //public static FlowSource.FluidHandler createEffectHandler(FluidIngredient fluidIngredient, BiConsumer<OpenEndedPipe, FluidStackJS> handler) {
    //    return new OpenEndedPipe.FluidHandler() {
    //        @Override
    //        public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
    //            return fluidIngredient.test(fluid);
    //        }
//
    //        @Override
    //        public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
    //            handler.accept(pipe, FluidStackJS.of(fluid));
    //        }
    //    };
    //}

//    public static BlockSpoutingBehaviour createSpoutingHandler(BlockStatePredicate block, SpecialSpoutHandlerEvent.SpoutHandler handler) {
//        return new BlockSpoutingBehaviour() {
//            @Override
//            public int fillBlock(Level world, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
//                if (!block.test(world.getBlockState(pos))) {
//                    return 0;
//                }
//                return (int) handler.fillBlock(new BlockContainerJS(world, pos), FluidStackJS.of(FluidStackHooksForge.fromForge(availableFluid)), simulate);
//            }
//        };
//    }
}