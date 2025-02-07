package net.ironhorsedevgroup.mods.immersivebogies.vehicle;

import minecrafttransportsimulator.items.components.AItemPack;
import minecrafttransportsimulator.jsondefs.AJSONBase;
import minecrafttransportsimulator.jsondefs.AJSONMultiModelProvider;
import minecrafttransportsimulator.jsondefs.JSONSubDefinition;
import minecrafttransportsimulator.packloading.CustomItemClassification;
import minecrafttransportsimulator.packloading.JSONParser;
import minecrafttransportsimulator.packloading.LegacyCompatSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RollingStockClassification implements CustomItemClassification {
    public RollingStockClassification() {
    }

    @Override
    public String toDirectory() {
        return "rollingstock/";
    }

    @Override
    public Class<? extends AJSONBase> getRepresentingClass() {
        return JSONRollingStock.class;
    }

    @Override
    public AItemPack<?> getItem(AJSONMultiModelProvider mainDefinition, JSONSubDefinition subDefinition, String packID) {
        return new RollingStockItem((JSONRollingStock) mainDefinition, subDefinition, packID);
    }

    @Override
    public AJSONBase loadDefinition(File jsonFile, AJSONBase definition) {
        JSONRollingStock vehicleDefinition = (JSONRollingStock) definition;
        JSONRollingStock loadedVehicleDefinition = null;
        try {
            loadedVehicleDefinition = JSONParser.parseStream(Files.newInputStream(jsonFile.toPath()), JSONRollingStock.class, vehicleDefinition.packID, vehicleDefinition.systemName);
            LegacyCompatSystem.performLegacyCompats(loadedVehicleDefinition);
            JSONParser.validateFields(loadedVehicleDefinition, "/", 1);
            vehicleDefinition.rail = loadedVehicleDefinition.rail;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loadedVehicleDefinition;
    }
}
