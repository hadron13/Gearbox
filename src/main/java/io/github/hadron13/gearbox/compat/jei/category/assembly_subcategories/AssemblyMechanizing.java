package io.github.hadron13.gearbox.compat.jei.category.assembly_subcategories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedBrassPress;

public class AssemblyMechanizing extends SequencedAssemblySubCategory {

    AnimatedBrassPress press;

    public AssemblyMechanizing() {
        super(25);
        press = new AnimatedBrassPress();
    }

    @Override
    public void draw(SequencedRecipe<?> recipe, PoseStack ms, double mouseX, double mouseY, int index) {
        press.offset = index;
        ms.pushPose();
        ms.translate(-5, 50, 0);
        ms.scale(.6f, .6f, .6f);
        press.draw(ms, getWidth() / 2, 0);
        ms.popPose();
    }

}
