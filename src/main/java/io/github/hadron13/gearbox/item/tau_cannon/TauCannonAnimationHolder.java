package io.github.hadron13.gearbox.item.tau_cannon;


import net.createmod.catnip.animation.LerpedFloat;

public class TauCannonAnimationHolder {

    public static LerpedFloat speed = LerpedFloat.linear();
    public static LerpedFloat recoil = LerpedFloat.linear();

    public TauCannonAnimationHolder(){

    }
    public static void init(){
        speed.chase(1, 0.1f, LerpedFloat.Chaser.EXP);

        recoil.chase(0, 0.1f, LerpedFloat.Chaser.LINEAR);
    }

    public static void tick(){
        speed.tickChaser();
        recoil.tickChaser();
    }
}
