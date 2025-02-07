package net.ironhorsedevgroup.mods.immersivebogies.registry;

import minecrafttransportsimulator.jsondefs.JSONPack;
import minecrafttransportsimulator.mcinterface.InterfaceManager;
import minecrafttransportsimulator.packloading.JSONParser;
import minecrafttransportsimulator.packloading.PackParser;
import net.ironhorsedevgroup.mods.immersivebogies.ImmersiveBogies;
import net.ironhorsedevgroup.mods.immersivebogies.vehicle.RollingStockClassification;
import net.ironhorsedevgroup.mods.immersivebogies.vehicle.JSONRollingStock;
import net.ironhorsedevgroup.mods.immersivebogies.packload.JSONStockList;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MTSObjects {
    public static void addDefaultItems() {
        ImmersiveBogies.logger.info("Adding Immersive Bogies Default MTS Objects");

        JSONPack packDef = new JSONPack();
        String packID = ImmersiveBogies.MODID;
        ImmersiveBogies.logger.info("Pack ID: {}", packID);

        Map<String, RollingStockClassification> defaultItems = new HashMap<>();

        defaultItems.put("default_railcar", JSONRollingStock.CLASSIFICATION);
        loadStock(defaultItems, packID);
    }

    public static void loadStock(Map<String, RollingStockClassification> stock, String packID) {
        String prefixFolders = "/assets/" + packID + "/rail/jsondefs/";
        ImmersiveBogies.logger.info("Path to rollingstock: {}", prefixFolders);
        String systemName;
        for (Map.Entry<String, RollingStockClassification> item : stock.entrySet()) {
            try {
                systemName = item.getKey();
                RollingStockClassification classification = item.getValue();
                String entryPath = prefixFolders + classification.toDirectory() + systemName + ".json";
                ImmersiveBogies.logger.info("Loading Stock: {}", entryPath);
                JSONRollingStock itemDef = (JSONRollingStock) JSONParser.parseStream(InterfaceManager.coreInterface.getPackResource(entryPath), classification.getRepresentingClass(), packID, systemName);
                itemDef.packID = packID;
                itemDef.systemName = systemName;
                itemDef.classification = classification;
                PackParser.registerItem(itemDef);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addPackItems(FMLPreInitializationEvent event) {
        String gameDirectory = event.getModConfigurationDirectory().getParent();
        File directory = new File(gameDirectory, "mods");

        if (directory.exists()) {
            ImmersiveBogies.logger.info("Loading Immersive Bogies MTS Content");
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.getName().endsWith(".jar")) {
                    checkJarForPacks(file);
                }
            }
        }
    }

    private static void checkJarForPacks(File packJar) {
        try {
            ZipFile jarFile = new ZipFile(packJar);
            Enumeration<? extends ZipEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith("/rail/stocklist.json")) {
                    String[] path = entry.getName().split("/");
                    String packID = path[path.length - 3];
                    //New style, use def as-is.
                    JSONStockList stock = JSONParser.parseStream(jarFile.getInputStream(entry), JSONStockList.class, null, null);
                    loadAllPackStock(stock, packID);
                }
            }
            jarFile.close();
        } catch (Exception e) {
            InterfaceManager.coreInterface.logError("A fault was encountered when trying to check file " + packJar.getName() + " for Immersive Bogies pack data.  This pack will not load Immersive Bogies content.");
            e.printStackTrace();
        }
    }

    private static void loadAllPackStock(JSONStockList stock, String packID) {
        Map<String, RollingStockClassification> loadStock = new HashMap<>();
        for (String car : stock.stock) {
            loadStock.put(car, JSONRollingStock.CLASSIFICATION);
        }
        loadStock(loadStock, packID);
    }
}
