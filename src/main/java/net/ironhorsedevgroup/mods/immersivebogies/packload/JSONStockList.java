package net.ironhorsedevgroup.mods.immersivebogies.packload;

import minecrafttransportsimulator.jsondefs.AJSONBase;
import minecrafttransportsimulator.packloading.JSONParser;

import java.util.List;

@JSONParser.JSONDescription("List all stock and locomotive files")
public class JSONStockList extends AJSONBase {
    @JSONParser.JSONDescription("List all included non locomotive rollingstock")
    public List<String> stock;
    @JSONParser.JSONDescription("List all included wheelsets")
    public List<String> wheels;
}
