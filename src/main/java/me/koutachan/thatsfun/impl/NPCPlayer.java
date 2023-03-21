package me.koutachan.thatsfun.impl;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.sun.jmx.remote.protocol.rmi.ServerProvider;
import me.koutachan.thatsfun.impl.connection.FakePlayerConnection;
import me.koutachan.thatsfun.impl.controller.*;
import me.koutachan.thatsfun.impl.pathfinder.*;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class NPCPlayer extends EntityPlayer {
    //private int jumpTicks;
    public NPCPathfinderGoalSelector goalSelector;
    public NPCPathfinderGoalSelector targetSelector;
    public NPCControllerMove moveController;
    public NPCControllerLook lookController;
    public NPCControllerJump controllerJump;
    //Changes: bo -> entitySenses
    public NPCEntitySenses entitySenses;
    public NPCNavigationAbstract navigation;
    private final Map<PathType, Float> bt = Maps.newEnumMap(PathType.class);
    private EntityLiving goalTarget;
    private int jumpTicks;
    private boolean aware = true;
    //Changes (float) bA -> cD;
    //Changes bz -> de
    //Changes bA -> ba
    private float cD;
    private BlockPosition de;
    private float ba = -1.0F;

    public NPCPlayer(Location location, MinecraftServer minecraftserver, WorldServer worldserver) {
        super(minecraftserver, worldserver, new GameProfile(UUID.randomUUID(), "テストマン"), new PlayerInteractManager(worldserver));
        this.initAttribute();
        this.goalSelector = new NPCPathfinderGoalSelector(world.getMethodProfilerSupplier());
        this.targetSelector = new NPCPathfinderGoalSelector(world.getMethodProfilerSupplier());
        this.moveController = new NPCControllerMove(this);
        this.controllerJump = new NPCControllerJump(this);
        this.lookController = new NPCControllerLook(this);
        this.entitySenses = new NPCEntitySenses(this);
        this.navigation = this.c(worldserver);
        this.playerConnection = new FakePlayerConnection(minecraftserver, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), this);
        worldserver.addPlayerJoin(this);
        getMinecraftServer().getPlayerList().getPlayers().forEach(player -> {
            player.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
            player.playerConnection.sendPacket(this.P());
        });
        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.sentListPacket = false;
    }


    @Override
    public void tick() {
        super.tick();
        this.movementTick();
        this.entityBaseTick();
        if (!this.world.isClientSide) {
            //eA are Unleash Check
            //this.eA();
            if (this.ticksLived % 5 == 0) {
                this.D();
            }
        }
        this.sentPackets();
    }

    //Changes -> rename H -> D
    protected void D() {
        boolean flag = !(this.getRidingPassenger() instanceof EntityInsentient);
        boolean flag1 = !(this.getVehicle() instanceof EntityBoat);
        this.goalSelector.plus(PathfinderGoal.Type.MOVE, flag);
        this.goalSelector.plus(PathfinderGoal.Type.JUMP, flag && flag1);
        this.goalSelector.plus(PathfinderGoal.Type.LOOK, flag);
    }

    @Override
    protected final void doTick() {
        ++this.ticksFarFromPlayer;
        if (this.aware) {
            this.world.getMethodProfiler().enter("sensing");
            this.entitySenses.a();
            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().enter("targetSelector");
            this.targetSelector.doTick();
            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().enter("goalSelector");
            this.goalSelector.doTick();
            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().enter("navigation");
            this.navigation.c();
            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().enter("mob tick");
            //何もない、NMSを使っているならここを使う必要すらない。。。
            //this.mobTick();
            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().enter("controls");
            this.world.getMethodProfiler().enter("move");
            this.moveController.a();
            this.world.getMethodProfiler().exitEnter("look");
            this.lookController.a();
            this.world.getMethodProfiler().exitEnter("jump");
            this.controllerJump.b();
            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().exit();
            //this.M();
        }
        Bukkit.broadcastMessage("mot: " + getMot());
    }

    @Override
    public void movementTick() {

        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }
        if (this.cs()) {
            this.aU = 0;
            this.c(this.locX(), this.locY(), this.locZ());
        }

        if (this.aU > 0) {
            double d0 = this.locX() + (this.aV - this.locX()) / (double) this.aU;
            double d1 = this.locY() + (this.aW - this.locY()) / (double) this.aU;
            double d2 = this.locZ() + (this.aX - this.locZ()) / (double) this.aU;
            double d3 = MathHelper.g(this.aY - (double) this.yaw);
            this.yaw = (float) ((double) this.yaw + d3 / (double) this.aU);
            this.pitch = (float) ((double) this.pitch + (this.aZ - (double) this.pitch) / (double) this.aU);
            --this.aU;
            this.setPosition(d0, d1, d2);
            this.setYawPitch(this.yaw, this.pitch);
        } else if (!this.doAITick()) {
            this.setMot(this.getMot().a(0.98D));
        }

        if (this.bb > 0) {
            this.aC = (float) ((double) this.aC + MathHelper.g(this.ba - (double) this.aC) / (double) this.bb);
            --this.bb;
        }

        Vec3D vec3d = this.getMot();
        double d4 = vec3d.x;
        double d5 = vec3d.y;
        double d6 = vec3d.z;
        if (Math.abs(vec3d.x) < 0.003D) {
            d4 = 0.0D;
        }

        if (Math.abs(vec3d.y) < 0.003D) {
            d5 = 0.0D;
        }

        if (Math.abs(vec3d.z) < 0.003D) {
            d6 = 0.0D;
        }

        this.setMot(d4, d5, d6);
        this.world.getMethodProfiler().enter("ai");
        if (this.isFrozen()) {
            this.jumping = false;
            this.aR = 0.0F;
            this.aT = 0.0F;
        }
        if (this.doAITick()) {
            this.world.getMethodProfiler().enter("newAi");
            this.doTick();
            this.world.getMethodProfiler().exit();
        }
        this.yaw = MathHelper.g(yaw);
        this.pitch = MathHelper.g(pitch);

        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("jump");
        if (this.jumping && this.cT()) {
            double d7;
            if (this.aQ()) {
                d7 = this.b(TagsFluid.LAVA);
            } else {
                d7 = this.b(TagsFluid.WATER);
            }
            boolean flag = this.isInWater() && d7 > 0.0D;
            double d8 = this.cx();
            if (!flag || this.onGround && !(d7 > d8)) {
                if (!this.aQ() || this.onGround && !(d7 > d8)) {
                    if ((this.onGround || flag && d7 <= d8) && this.jumpTicks == 0) {
                        this.jump();
                        this.jumpTicks = 10;
                    }
                } else {
                    this.c(TagsFluid.LAVA);
                }
            } else {
                this.c(TagsFluid.WATER);
            }
        } else {
            this.jumpTicks = 0;
        }

        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("travel");
        this.aR *= 0.98F;
        this.aT *= 0.98F;
        this.elytraCheck();
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        this.g(new Vec3D(this.aR, this.aS, this.aT));
        this.world.getMethodProfiler().exit();
        this.world.getMethodProfiler().enter("push");
        if (this.bf > 0) {
            --this.bf;
            this.a(axisalignedbb, this.getBoundingBox());
        }
        this.collideNearby();
        this.world.getMethodProfiler().exit();
        if (!this.world.isClientSide && this.dO() && this.aG()) {
            this.damageEntity(DamageSource.DROWN, 1.0F);
        }
    }

    public void elytraCheck() {
        boolean flag = this.getFlag(7);
        if (flag && !this.onGround && !this.isPassenger() && !this.hasEffect(MobEffects.LEVITATION)) {
            ItemStack itemstack = this.getEquipment(EnumItemSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.d(itemstack)) {
                flag = true;
                if (!this.world.isClientSide && (this.be + 1) % 20 == 0) {
                    itemstack.damage(1, this, (entityliving) -> {
                        entityliving.broadcastItemBreak(EnumItemSlot.CHEST);
                    });
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }

        if (!this.world.isClientSide && flag != this.getFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, flag).isCancelled()) {
            this.setFlag(7, flag);
        }
    }


    /*public float a(PathType pathtype) {
        //TODO: Support Vehicle?
        if (this.getVehicle() instanceof EntityInsentient) {
            return ((EntityInsentient) this.getVehicle()).a(pathtype);
        }
        return pathtype.a();
    }*/

    public float a(PathType type) {
        return type.a();
    }

    public NPCControllerLook getControllerLook() {
        return this.lookController;
    }

    public NPCControllerMove getControllerMove() {
        return this.moveController;
        //TODO: Support Vehicle?
        /*if (this.isPassenger() && this.getVehicle() instanceof EntityInsentient) {
            EntityInsentient entityinsentient = (EntityInsentient)this.getVehicle();
            //return entityinsentient.getControllerMove();
        } else {
            return this.moveController;
        }*/
    }

    public NPCControllerJump getControllerJump() {
        return controllerJump;
    }

    public NPCPathfinderGoalSelector getGoalSelector() {
        return goalSelector;
    }

    public NPCPathfinderGoalSelector getTargetSelector() {
        return targetSelector;
    }

    public NPCEntitySenses getEntitySenses() {
        return entitySenses;
    }

    @Nullable
    public EntityLiving getGoalTarget() {
        return this.goalTarget;
    }

    public void setGoalTarget(@Nullable EntityLiving entityliving) {
        this.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    public boolean setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (this.getGoalTarget() == entityliving) {
            return false;
        } else {
            if (fireEvent) {
                if (reason == EntityTargetEvent.TargetReason.UNKNOWN && this.getGoalTarget() != null && entityliving == null) {
                    reason = this.getGoalTarget().isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
                }

                if (reason == EntityTargetEvent.TargetReason.UNKNOWN) {
                    this.world.getServer().getLogger().log(Level.WARNING, "Unknown target reason, please report on the issue tracker", new Exception());
                }

                CraftLivingEntity ctarget = null;
                if (entityliving != null) {
                    ctarget = (CraftLivingEntity) entityliving.getBukkitEntity();
                }

                EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(this.getBukkitEntity(), ctarget, reason);
                this.world.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return false;
                }

                if (event.getTarget() != null) {
                    entityliving = ((CraftLivingEntity) event.getTarget()).getHandle();
                } else {
                    entityliving = null;
                }
            }

            this.goalTarget = entityliving;
            return true;
        }
    }

    public NPCNavigationAbstract getNavigation() {
        return navigation;
    }

    public void a(PathType pathtype, float f) {
        this.bt.put(pathtype, f);
    }

    //Delta Z
    public void t(float f) {
        this.aT = f;
    }

    //Delta Y?
    public void u(float f) {
        this.aS = f;
    }

    //Delta X
    public void v(float f) {
        this.aR = f;
    }

    @Override
    public void q(float f) {
        super.q(f);
        this.t(f);
    }

    //Changes: Rename b -> c
    protected NPCNavigationAbstract c(World world) {
        return new NPCNavigation(this, world);
    }

    public boolean b(PathType pathtype) {
        return pathtype != PathType.DANGER_FIRE && pathtype != PathType.DANGER_CACTUS && pathtype != PathType.DANGER_OTHER && pathtype != PathType.WALKABLE_DOOR;
    }

    public boolean isAware() {
        return aware;
    }

    public void setAware(boolean aware) {
        this.aware = aware;
    }

    public void forceSleep(BlockPosition position) {
        if (this.isPassenger()) {
            this.stopRiding();
        }
        this.setPose(EntityPose.SLEEPING);
        this.a(position);
        this.e(position);
        this.setMot(Vec3D.ORIGIN);
        this.impulse = true;
    }

    //O -> d
    public int d() {
        return 40;
    }

    //Q -> g
    public int g() {
        return 75;
    }

    //ep -> ea
    public int ea() {
        return 10;
    }

    public void moveTo(int priority, Vec3D goal) {
        targetSelector.a(4, new NPCPathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        targetSelector.a(3, new NPCPathfinderGoalMeleeAttack(this, 100, true));
        targetSelector.a(2, new NPCPathfinderGoalLookAtPlayer(this, EntityHuman.class, 1));
        targetSelector.a(1, new NPCPathfinderGoalDoorInteract(this));
    }

    //Changes f -> g
    public float g(BlockPosition blockposition) {
        return this.a(blockposition, this.world);
    }

    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return 0.0F;
    }

    public boolean a(BlockPosition blockposition) {
        return this.ba == -1.0F || this.de.j(blockposition) < (double) (this.bA * this.bA);
    }

    public void a(BlockPosition blockposition, int i) {
        this.de = blockposition;
        this.ba = (float) i;
    }

    public BlockPosition ew() {
        return this.de;
    }

    public void initAttribute() {
        try {
            Field field = AttributeMapBase.class.getDeclaredField("d");
            field.setAccessible(true);
            AttributeProvider provider = (AttributeProvider) field.get(getAttributeMap());
            Field map = AttributeProvider.class.getDeclaredField("a");
            map.setAccessible(true);
            Map<AttributeBase, AttributeModifiable> maps = (Map<AttributeBase, AttributeModifiable>) map.get(provider);
            map.set(provider, new HashMap<AttributeBase, AttributeModifiable>(maps) {{
                put(GenericAttributes.FOLLOW_RANGE, new AttributeModifiable(GenericAttributes.FOLLOW_RANGE, d -> d.setValue(100)));
                put(GenericAttributes.ATTACK_SPEED, new AttributeModifiable(GenericAttributes.ATTACK_SPEED, d -> d.setValue(1)));
                put(GenericAttributes.MOVEMENT_SPEED, new AttributeModifiable(GenericAttributes.MOVEMENT_SPEED, d -> d.setValue(0.01)));
                put(GenericAttributes.ATTACK_KNOCKBACK, new AttributeModifiable(GenericAttributes.ATTACK_KNOCKBACK, d -> d.setValue(0)));
            }});
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void sentPackets() {

        playerConnection.a();
        getWorldServer().getMinecraftServer().getPlayerList().getPlayers().forEach(v -> {
            v.playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((yaw * 256.0F) / 360.0F)));
        });
        //getWorldServer().getChunkProvider().playerChunkMap.read().movePlayer(this);
        /*final double x = locX(), y = locY(), z = locZ();
        if (lastYaw != yaw || lastPitch != pitch) {

            PacketPlayInFlying flying = new PacketPlayInFlying();
            flying.hasPos = true;
            flying.x = x;
            flying.y = y;
            flying.z = z;
            flying.hasLook = true;
            flying.yaw = yaw;
            flying.pitch = pitch;*/
/*            getWorldServer().getChunkProvider().c()
            playerConnection

            this
            getWorldServer().getMinecraftServer().getServerPing();

        } else if (lastX != x || lastY != y || lastZ != z) {
            PacketPlayInFlying flying = new PacketPlayInFlying();
            flying.hasPos = true;
            flying.x = x;
            flying.y = y;
            flying.z = z;
            playerConnection.a(flying);
        }
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
        this.lastYaw = yaw;
        this.lastPitch = pitch;*/
    }

    @Override
    public boolean attackEntity(Entity entity) {
        float f = (float) this.b(GenericAttributes.ATTACK_DAMAGE);
        float f1 = (float) this.b(GenericAttributes.ATTACK_KNOCKBACK);
        if (entity instanceof EntityLiving) {
            f += EnchantmentManager.a(this.getItemInMainHand(), ((EntityLiving) entity).getMonsterType());
            f1 += (float) EnchantmentManager.b(this);
        }

        int i = EnchantmentManager.getFireAspectEnchantmentLevel(this);
        if (i > 0) {
            EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), i * 4);
            Bukkit.getPluginManager().callEvent(combustEvent);
            if (!combustEvent.isCancelled()) {
                entity.setOnFire(combustEvent.getDuration(), false);
            }
        }
        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && entity instanceof EntityLiving) {
                ((EntityLiving) entity).a(f1 * 0.5F, (double) MathHelper.sin(this.yaw * 0.017453292F), (double) (-MathHelper.cos(this.yaw * 0.017453292F)));
                this.setMot(this.getMot().d(0.6D, 1.0D, 0.6D));
            }

            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;
                this.a(entityhuman, this.getItemInMainHand(), entityhuman.isHandRaised() ? entityhuman.getActiveItem() : ItemStack.b);
            }
            this.a(this, entity);
            this.z(entity);
        }
        return flag;
    }

    private void a(EntityHuman entityhuman, ItemStack itemstack, ItemStack itemstack1) {
        if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentManager.getDigSpeedEnchantmentLevel(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                entityhuman.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                this.world.broadcastEntityEffect(entityhuman, (byte) 30);
            }
        }
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return !isInvulnerable() && super.damageEntity(damagesource, f);
    }
}