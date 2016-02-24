package hpc.toolytippers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TooltipEvent {
	public final static int efficiencyEnchID = 32;

	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		if (event.itemStack != null) {
			ItemStack itemStack = event.itemStack;
			int maxDurability = itemStack.getMaxDamage();
			if (maxDurability > 1 && !itemStack.getHasSubtypes()) {
				int currentDurability = maxDurability - itemStack.getItemDamage();
				event.toolTip.add("Durability: " + currentDurability + "/" + maxDurability);
			}
			int miningLevel = 0;
			String[] tiers = {"Wood", "Stone", "Iron", "Diamond", "Alumite", "Manyullyn"};
			String toolTier = "";
			if (itemStack.getItem() instanceof ItemTool) {
				ItemTool tool = ((ItemTool) itemStack.getItem());
				miningLevel = tool.func_150913_i().getHarvestLevel();
				if (miningLevel < tiers.length) {
					toolTier = tiers[miningLevel];
				} else {
					toolTier = (miningLevel - tiers.length) + " tiers above " + tiers[tiers.length - 1];
				}
				event.toolTip.add("Tier: " + toolTier);
				float[] efficiencies = {2.0f, 4.0f, 6.0f, 8.0f, 12.0f, 14.0f};
				String[] efficiencyNames = {"Wood", "Stone", "Iron", "Diamond", "Gold", "Faster than Gold"};
				float miningSpeed = tool.func_150913_i().getEfficiencyOnProperMaterial();
				NBTTagList enchantments = itemStack.getEnchantmentTagList();
				int efficiencyLevel = 0;
				// note - enchanted items look like this:
				// <minecraft:golden_sword>.withTag({ench: [{lvl: 3 as short, id: 16 as short}, {lvl: 2 as short, id: 19 as short}, {lvl: 2 as short, id: 20 as short}]})
				for (int i = 0; enchantments != null && i < enchantments.tagCount(); i++) {
					if (enchantments.getCompoundTagAt(i).getInteger("id") == efficiencyEnchID) {
						efficiencyLevel = enchantments.getCompoundTagAt(i).getInteger("lvl");
					}
				}
				if (efficiencyLevel > 0) {
					miningSpeed += (efficiencyLevel * efficiencyLevel) + 1;
				}
				String fasterThan = "Hand";
				for (int i = 0; i < efficiencies.length && miningSpeed >= efficiencies[i]; i++) {
					fasterThan = efficiencyNames[i];
				}
				event.toolTip.add("Mining Speed: " + fasterThan);
			}
		}
	}
}
