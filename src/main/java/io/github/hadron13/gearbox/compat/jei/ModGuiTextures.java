package io.github.hadron13.gearbox.compat.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ModGuiTextures implements ScreenElement {
    JEI_HEAT_BAR_CENTERED("jei/widgets", 0, 16, 169, 19),
    JEI_SHORT_ARROW("jei/widgets", 0, 0, 20, 9);

    public static final int FONT_COLOR = 5726074;
    public final ResourceLocation location;
    public int width;
    public int height;
    public int startX;
    public int startY;

    private ModGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    private ModGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }
    private ModGuiTextures(String location, int startX, int startY, int width, int height) {
        this("gearbox", location, startX, startY, width, height);
    }

    private ModGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @OnlyIn(Dist.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, this.location);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y) {
        this.bind();
        GuiComponent.blit(ms, x, y, 0, (float)this.startX, (float)this.startY, this.width, this.height, 256, 256);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, GuiComponent component) {
        this.bind();
        component.blit(ms, x, y, this.startX, this.startY, this.width, this.height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, Color c) {
        this.bind();
        UIRenderHelper.drawColoredTexture(ms, c, x, y, this.startX, this.startY, this.width, this.height);
    }
}