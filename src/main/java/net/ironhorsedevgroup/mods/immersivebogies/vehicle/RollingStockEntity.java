package net.ironhorsedevgroup.mods.immersivebogies.vehicle;

import cam72cam.immersiverailroading.entity.physics.SimulationState;

import mcinterface1122.WrapperWorld;

import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.entities.instances.PartEngine;
import minecrafttransportsimulator.jsondefs.JSONPart;
import minecrafttransportsimulator.mcinterface.AWrapperWorld;
import minecrafttransportsimulator.mcinterface.IWrapperNBT;
import minecrafttransportsimulator.mcinterface.IWrapperPlayer;
import minecrafttransportsimulator.mcinterface.InterfaceManager;
import minecrafttransportsimulator.systems.ConfigSystem;
import net.ironhorsedevgroup.mods.immersivebogies.ImmersiveBogies;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import trackapi.lib.ITrack;
import trackapi.lib.ITrackBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RollingStockEntity extends EntityVehicleF_Physics {
    private double velocity = 1;
    public List<SimulationState> states = new ArrayList();
    private String defID;

    public RollingStockEntity(AWrapperWorld world, IWrapperPlayer placingPlayer, RollingStockItem item, IWrapperNBT data) {
        super(world, placingPlayer, item, data);
    }

    @Override
    public void update() {
        super.update();
            world.beginProfiling("RollingStockEntity", true);
            Point3D point = getRailCollisions(false);
            if (point.equals(this.position)) {
                point = getRailCollisions(true);
            }
            moveToPoint(point);
            world.endProfiling();
    }

    private void moveToPoint(Point3D position) {
        if (Objects.nonNull(position)) {
            this.position.set(position.x, this.position.y, position.z);
        } else {
            ImmersiveBogies.logger.warn("No position gathered from rail collisions!");
        }
    }

    private Point3D getRailCollisions(boolean front) {
        Point3D position = this.groundDeviceCollective.getContactPoint(front);
        if (Objects.nonNull(position)) {
            position.rotate(this.orientation).add(this.position);
            if (this.world instanceof WrapperWorld) {
                World gameWorld = ((WrapperWorld) this.world).getWrappedWorld();
                BlockPos pos = new BlockPos((int)position.x, (int)position.y, (int)position.z);
                IBlockState state = gameWorld.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof ITrackBlock) {
                    ITrackBlock trackBlock = (ITrackBlock) block;
                    ImmersiveBogies.logger.info("Track block gauge is {}", trackBlock.getTrackGauge(gameWorld, pos));
                    return getRailMovement(gameWorld, trackBlock, position);
                } else if (block.hasTileEntity(state)) {
                    TileEntity tile = gameWorld.getTileEntity(pos);
                    if (tile instanceof ITrack) {
                        ITrack track = (ITrack) tile;
                        ImmersiveBogies.logger.info("Track gauge is {}", track.getTrackGauge());
                        return getRailMovement(track);
                    }
                }
            }
        }
        return this.position;
    }

    private Point3D getRailMovement(World world, ITrackBlock track, Point3D position) {
        Vec3d pos = (track.getNextPosition(world, new BlockPos(position.x, position.y, position.z), new Vec3d(this.position.x, this.position.y, this.position.z), new Vec3d(-Math.sin(Math.toRadians(this.rotation.angles.y)) * (velocity / 20), 0, Math.cos(Math.toRadians(this.rotation.angles.y)) * (velocity / 20))));
        return new Point3D(pos.x, pos.y, pos.z);
    }

    private Point3D getRailMovement(ITrack track) {
        Vec3d pos = (track.getNextPosition(new Vec3d(this.position.x, this.position.y, this.position.z), new Vec3d(-Math.sin(Math.toRadians(this.rotation.angles.y)) * (velocity / 20), 0, Math.cos(Math.toRadians(this.rotation.angles.y)) * (velocity / 20))));
        return new Point3D(pos.x, pos.y, pos.z);
    }


    @Override
    public void addPartsPostAddition(IWrapperPlayer placingPlayer, IWrapperNBT data) {
        super.addPartsPostAddition(placingPlayer, data);
        //If we have a default fuel, add it now as we SHOULD have an engine to tell
        //us what fuel type we will need to add.
        if (data == null && definition.motorized.defaultFuelQty > 0) {
            for (APart part : allParts) {
                if (part instanceof PartEngine) {
                    String mostPotentFluid = "";
                    //If the engine is electric, just use the electric fuel type.
                    if (part.definition.engine.type == JSONPart.EngineType.ELECTRIC) {
                        mostPotentFluid = PartEngine.ELECTRICITY_FUEL;
                    } else {
                        //Get the most potent fuel for the vehicle from the fuel configs.
                        for (String fluidName : ConfigSystem.settings.fuel.fuels.get(part.definition.engine.fuelType).keySet()) {
                            if (InterfaceManager.coreInterface.isFluidValid(fluidName)) {
                                if (mostPotentFluid.isEmpty() || ConfigSystem.settings.fuel.fuels.get(part.definition.engine.fuelType).get(mostPotentFluid) < ConfigSystem.settings.fuel.fuels.get(part.definition.engine.fuelType).get(fluidName)) {
                                    mostPotentFluid = fluidName;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
