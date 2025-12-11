package io.github.hadron13.gearbox.blocks.core_drill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.model.BakedModelHelper;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperBufferFactory;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlock.HORIZONTAL_FACING;

public class CoreDrillRenderer extends KineticBlockEntityRenderer<CoreDrillBlockEntity> {

    public static Map<Block, SuperByteBuffer> oreCoreModels = new HashMap<>();

    public CoreDrillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(CoreDrillBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(ModPartialModels.SHAFT_DUAL_TINY, state, state.getValue(HORIZONTAL_FACING).getClockWise());
    }

    public static SuperByteBuffer getOreCoreModel(Block ore){
        if(oreCoreModels.containsKey(ore)){
            return oreCoreModels.get(ore);
        }
        BlockState baseBlockState = ore.defaultBlockState();
        BakedModel model = BakedModelHelper.generateModel(ModPartialModels.ORE_CORE.get(), (s) -> getSpriteOnSide(baseBlockState, Direction.NORTH));

        SuperByteBuffer byteBuffer = SuperBufferFactory.getInstance().createForBlock(model, baseBlockState);

        oreCoreModels.put(ore, byteBuffer);

        return byteBuffer;
    }

    private static TextureAtlasSprite getSpriteOnSide(BlockState state, Direction side) {
        BakedModel model = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
        if (model == null)
            return null;
        RandomSource random = RandomSource.create();
        random.setSeed(42L);
        List<BakedQuad> quads = model.getQuads(state, side, random, ModelData.EMPTY, null);
        if (!quads.isEmpty()) {
            return quads.get(0)
                    .getSprite();
        }
        random.setSeed(42L);
        quads = model.getQuads(state, null, random, ModelData.EMPTY, null);
        if (!quads.isEmpty()) {
            for (BakedQuad quad : quads) {
                if (quad.getDirection() == side) {
                    return quad.getSprite();
                }
            }
        }
        return model.getParticleIcon(ModelData.EMPTY);
    }

        @Override
    protected void renderSafe(CoreDrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        VertexConsumer solid = buffer.getBuffer(RenderType.solid());
        Direction facing = be.getBlockState().getValue(HORIZONTAL_FACING);

        SuperByteBuffer pole = CachedBuffers.partialFacing(AllPartialModels.MECHANICAL_MIXER_POLE, be.getBlockState(), facing);
        SuperByteBuffer tube = CachedBuffers.partial(ModPartialModels.CORE_DRILL_TUBE, be.getBlockState());
        SuperByteBuffer core = getOreCoreModel((be.minedBlock == null)?Blocks.AIR: be.minedBlock);


        ms.pushPose();
        TransformStack.of(ms)
            .translate(facing.step().mul(0.5f/16f))
            .rotateCentered(22.5f * Mth.DEG_TO_RAD, facing.getClockWise())
                .translate(0, 0.5/16f, 0)
        ;

        float tube_y_offset = 0;
        float pole_y_offset = be.poleOffset.getValue(partialTicks);

        switch (be.drillState){
            case CoreDrillBlockEntity.IDLE -> {
                tube_y_offset = 0;
            }
            case CoreDrillBlockEntity.PUSHING ->{
                tube_y_offset = be.payloadOffset.getValue(partialTicks) * 20/16f;

                tube.light(light)
                    .translate(0, 4/16f, 0)
                    .translate(0, -tube_y_offset, 0)
                    .renderInto(ms, solid);
            }
            case CoreDrillBlockEntity.PULLING -> {

                core.light(light)
                    .translate(0, 3/16f - be.payloadOffset.getValue(partialTicks) * (20/16f), 0)
                    .renderInto(ms, solid);
            }
            case CoreDrillBlockEntity.STORING -> {

                float offset = Mth.clamp((be.payloadOffset.getValue(partialTicks) * 4)-0.75f, 0.0f, 1.0f);

                core.light(light)
                        .translate(0, 3/16f, 0)
                        .translate(facing.step().mul(offset * -7/16f))
                        .renderInto(ms, solid);
            }
        }

        pole.light(light)
            .translate(0, -pole_y_offset, 0)
            .translate(0, 16/16f, 0)
            .renderInto(ms, solid);


        if (VisualizationManager.supportsVisualization(be.getLevel())){
            ms.popPose();
            return;
        }

        for(int i = 0; i < be.tubes; i++){
            tube.light(light)
                .translate(0, 4/16f, 0)
                .translate(0, (i + 1)*-20/16f -tube_y_offset, 0)
                .renderInto(ms, solid);
        }

        ms.popPose();
    }
}
