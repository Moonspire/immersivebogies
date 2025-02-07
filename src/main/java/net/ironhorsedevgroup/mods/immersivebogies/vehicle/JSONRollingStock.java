package net.ironhorsedevgroup.mods.immersivebogies.vehicle;

import minecrafttransportsimulator.jsondefs.JSONSubDefinition;
import minecrafttransportsimulator.jsondefs.JSONVehicle;
import minecrafttransportsimulator.packloading.JSONParser;
import minecrafttransportsimulator.packloading.PackResourceLoader;
import net.ironhorsedevgroup.mods.immersivebogies.ImmersiveBogies;

@JSONParser.JSONDescription("Immersive Railroading compatible Transport Simulator vehicle definitions can be done here")
public class JSONRollingStock extends JSONVehicle {
    public static final RollingStockClassification CLASSIFICATION = new RollingStockClassification();

    @JSONParser.JSONRequired
    @JSONParser.JSONDescription("This section specifies the unique information relevant to specifically rail vehicles")
    public VehicleRail rail;

    public JSONRollingStock() {}

    public static class VehicleRail {
        @JSONParser.JSONDescription("The track gauge of the car")
        public float defaultGauge;
    }

    @Override
    public String getModelLocation(JSONSubDefinition subDefinition) {
        switch (this.rendering.modelType) {
            case OBJ:
                return PackResourceLoader.getPackResource(this, PackResourceLoader.ResourceType.OBJ_MODEL, subDefinition.modelName != null ? subDefinition.modelName : this.systemName);
            case LITTLETILES:
                return PackResourceLoader.getPackResource(this, PackResourceLoader.ResourceType.LT_MODEL, subDefinition.modelName != null ? subDefinition.modelName : this.systemName);
            default:
                return null;
        }
    }

    @Override
    public String getTextureLocation(JSONSubDefinition subDefinition) {
        switch (this.rendering.modelType) {
            case OBJ:
                return PackResourceLoader.getPackResource(this, PackResourceLoader.ResourceType.PNG, subDefinition.textureName != null ? subDefinition.textureName : this.systemName + subDefinition.subName);
            case LITTLETILES:
                return "GLOBAL";
            default:
                return null;
        }
    }
}
