package io.github.hadron13.gearbox.blocks.irradiator;


import io.github.hadron13.gearbox.blocks.laser.Laser;
import net.createmod.catnip.theme.Color;
import net.minecraft.util.Mth;

public interface LaserRecipe {
    public int getColor();
    public float getPower();

    public static boolean matchLaser(LaserRecipe recipe, Laser laser){
        if(recipe.getPower() > laser.getPower())
            return false;

        int required_color = recipe.getColor();
        int provided_color = laser.getColor();

        if(Mth.abs((required_color >> 16 & 0xff) - (provided_color >> 16 & 0xff)) > 5)
            return false;
        if(Mth.abs( (required_color >> 8 & 0xff) - (provided_color >> 8 & 0xff)) > 5)
            return false;
        if(Mth.abs( (required_color & 0xff) - (provided_color & 0xff)) > 5)
            return false;

        return true;
    }
}
