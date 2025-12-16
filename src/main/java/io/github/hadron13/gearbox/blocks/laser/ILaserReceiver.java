package io.github.hadron13.gearbox.blocks.laser;

public interface ILaserReceiver {
    /**
     * @brief method called every frame by every Laser
     * @param laser laser that called this method
     */
    public void receiveLaser(Laser laser);

    /**
     * @brief method called after a laser has either ceased or was pointed elsewhere
     * @param laser laser that called this method
     */
    public void endReceiveLaser(Laser laser);


    public enum LaserInteraction{
        BLOCK,
        PASS
    }

}
