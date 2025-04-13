package io.github.hadron13.gearbox.blocks.laser;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.register.data.ModDamageTypes;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.*;


public class LaserBeamBehavior extends BlockEntityBehaviour {
    public static class LaserBeam{
        public boolean enabled = true;
        public Color color = Color.BLACK;
        public float length = 0;
        public float power = 0;
        public Direction facing = Direction.NORTH;
        public BlockPos origin = BlockPos.ZERO;
        public int breakTimer = 0;
        public ILaserReceiver targetReceiver = null;
        public List<ILaserReader> readers = new ArrayList<>();
        public List<Entity> caughtEntities = new ArrayList<>();
    }

    public Map<Direction, LaserBeam> beams;
    public boolean wrenched = false;

    public static final BehaviourType<LaserBeamBehavior> TYPE = new BehaviourType<>();
    public static final int MAX_LENGTH = 100;

    public LaserBeamBehavior(SmartBlockEntity be) {
        super(be);
        beams = new HashMap<>();
        setLazyTickRate(2);
    }

    public void addLaser(Direction face, BlockPos origin, Color color, float power){
        LaserBeam beam = new LaserBeam();
        beam.facing = face;
        beam.origin = origin;
        beam.color = color;
        beam.power = power;
        beam.enabled = false;

        beams.put(face, beam);
    }

    @Nullable
    public LaserBeam getLaser(Direction face){
        return beams.get(face);
    }
    public LaserBeam getLaser(){
        return beams.get(beams.keySet().iterator().next());
    }

    public void enableLaser(Direction face){
        LaserBeam beam = getLaser(face);
        if(beam != null)beam.enabled = true;
    }

    public void disableLaser(Direction face){
        LaserBeam beam = getLaser(face);
        if(beam != null)beam.enabled = false;
    }

    public void setPower(Direction face, float power){
        LaserBeam beam = getLaser(face);
        if(beam != null)beam.power = power;
    }

    public void setColor(Direction face, Color color){
        LaserBeam beam = getLaser(face);
        if(beam != null)beam.color = color;

    }

    public List<Entity> getAllCaughtEntities(){
        List<Entity> allCaughtEntities = new ArrayList<>();
        for(LaserBeam beam : beams.values())
            allCaughtEntities.addAll(beam.caughtEntities);
        return allCaughtEntities;
    }



    /**
     * for redirecting lasers at sub-tick speeds, made for ILaserReceiver blocks
     * @param face face of the laser
     * @param index recursion index
     */
    public void propagate(Direction face, int index){
        if(index == 0)
            return;
        LaserBeam beam = getLaser(face);
        if(beam != null && beam.targetReceiver != null){
            beam.targetReceiver.propagate(beam.facing.getOpposite(),
                                          beam.color,
                                    beam.power - beam.targetReceiver.getLoss(),
                                    index-1);
        }
    }

    @Override
    public void tick(){
        super.tick();
        if(getWorld()==null )
            return;
        for(LaserBeam beam: beams.values())
            updateBeam(beam);
    }
    @Override
    public void lazyTick(){
        if(getWorld() == null)
            return;

        if(getWorld().isClientSide){
            for(LaserBeam beam: beams.values())
                generateParticles(beam);
        }else{
            for(LaserBeam beam: beams.values())
                updateCaughtEntities(beam);
        }
    }

    public void destroy() {
        for(LaserBeam beam: beams.values()) {
            if (beam.targetReceiver != null) {
                beam.targetReceiver.receiveLaser(beam.facing.getOpposite(), Color.BLACK, 0);
            }
            for(ILaserReader reader : beam.readers){
                if(reader != null)
                    reader.receiveLaser(beam.facing.getOpposite(), Color.BLACK, 0);
            }
        }
    }
    public void updateBeam(LaserBeam beam){
        if(!beam.enabled)
            return;
        BlockState blockState = null;
        Level level = getWorld();

        beam.targetReceiver = null;
        for(beam.length = 1; beam.length <= MAX_LENGTH; beam.length++){
            BlockPos currentPosition = beam.origin.relative(beam.facing, (int)beam.length);

            blockState = level.getBlockState(currentPosition);

            if(blockState.isAir() || !blockState.getFluidState().isEmpty())
                continue;

            boolean translucent = blockState.is(Tags.Blocks.GLASS) || blockState.is(Tags.Blocks.GLASS_PANES);

            if(translucent){
                continue;
            }
            BlockEntity blockEntityAtPos = level.getBlockEntity(currentPosition);
            if(blockEntityAtPos instanceof ILaserReceiver receiver){
                if(receiver.receiveLaser(beam.facing.getOpposite(), beam.color, beam.power - receiver.getLoss())){
                    beam.targetReceiver = receiver;
                }
                beam.breakTimer = 0;
                break;
            }

            if(blockEntityAtPos instanceof ILaserReader reader){
                if(reader.receiveLaser(beam.facing.getOpposite(), beam.color, beam.power)){
                    if(!beam.readers.contains(reader))
                        beam.readers.add(reader);
                    continue;
                }
            }

            boolean catchesFire = blockState.isFlammable(level, currentPosition, beam.facing.getOpposite());

            float hardness = blockState.getDestroySpeed(level, currentPosition);
            boolean canBurn = hardness > -1 && hardness < beam.power;

            if(!canBurn && !catchesFire) {
                beam.breakTimer = 0;
                break;
            }
            if(beam.power <= 0)
                break;
            beam.breakTimer++;

            if((canBurn && beam.breakTimer >= (hardness * 10)/beam.power) || (catchesFire && beam.breakTimer >= 20/beam.power)){
                level.destroyBlock(currentPosition, true);
                beam.breakTimer = 0;
            }
            break;
        }

        for(Entity entity : beam.caughtEntities){
            if(entity instanceof ItemEntity){
//                ItemEntity item = (ItemEntity) entity;
                continue;
            }

            if(!entity.isAlive())
                continue;
            //getWorld().explode(null, entity.getX(), entity.getY(), entity.getZ(), 10f, Explosion.BlockInteraction.NONE);
            entity.hurt(new DamageSource(getWorld().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.laser)), beam.power * 2);

            entity.setSecondsOnFire(3);
        }
    }
    public void updateCaughtEntities(LaserBeam beam){
        if(!beam.enabled)
            return;
        Vec3 min = Vec3.atCenterOf( beam.origin );
        Vec3 max = Vec3.atCenterOf( beam.origin.relative(beam.facing, (int)beam.length) );

        AABB laser_collision = new AABB(min, max);

        beam.caughtEntities = getWorld().getEntities(null, laser_collision);
    }
    public void generateParticles(LaserBeam beam){
        if(beam.breakTimer == 0 || !beam.enabled)
            return;

        Direction oppositeFace = beam.facing.getOpposite();
        Vec3 particlePos = Vec3.atCenterOf(beam.origin.relative(beam.facing, (int)beam.length));

        Vec3 velocity = VecHelper.offsetRandomly(new Vec3(oppositeFace.getStepX(), 0, oppositeFace.getStepZ()), getWorld().random, 1.0f);

        getWorld().addParticle(ParticleTypes.LAVA,
                particlePos.x - beam.facing.getStepX()/2f, particlePos.y + 0.1f, particlePos.z - beam.facing.getStepZ()/2f,
                velocity.x, velocity.y, velocity.z);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        for(LaserBeam beam: beams.values()) {
            String id = beam.facing.toString();
            compound.putBoolean("enable"+id, beam.enabled);
            compound.putFloat("power"+id, beam.power);
            compound.putFloat("length"+id, beam.length);
            compound.putInt("color"+id, beam.color.getRGB());
            compound.putInt("break"+id, beam.breakTimer);
        }
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        for(LaserBeam beam: beams.values()) {
            String id = beam.facing.toString();
            beam.enabled = compound.getBoolean("enable"+id);
            beam.power = compound.getFloat("power"+id);
            beam.length = compound.getFloat("length"+id);
            beam.color = new Color(compound.getInt("color"+id));
            beam.breakTimer = compound.getInt("break"+id);
        }
    }
    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
}
