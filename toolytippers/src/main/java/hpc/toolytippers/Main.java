package hpc.toolytippers;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "toolytippers", name = "Tooly Tippers", version = "0.1", dependencies = "")
public class Main {
	@Instance("toolytippers")
	public Main instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
 		MinecraftForge.EVENT_BUS.register(new TooltipEvent()); 
	}

}
