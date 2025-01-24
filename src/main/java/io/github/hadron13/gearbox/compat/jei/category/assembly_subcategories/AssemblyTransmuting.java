package io.github.hadron13.gearbox.compat.jei.category.assembly_subcategories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedIrradiator;

public class AssemblyTransmuting extends SequencedAssemblySubCategory {

    AnimatedIrradiator irradiator;

    public AssemblyTransmuting() {
        super(25);
        irradiator = new AnimatedIrradiator(false);
    }

    @Override
    public void draw(SequencedRecipe<?> sequencedRecipe, PoseStack ms, double v, double v1, int index) {

        irradiator.offset = index;
        ms.pushPose();
        ms.translate(-5, 50, 0);
        ms.scale(.6f, .6f, .6f);
        irradiator.draw(ms, getWidth() / 2, 0);
        ms.popPose();
    }
}
