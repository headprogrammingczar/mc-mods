package hpc.toolytippers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TooltipEvent {
	public final static int efficiencyEnchID = 32;

	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		EnumChatFormatting color = EnumChatFormatting.BLUE;
		if (event.itemStack != null) {
			ItemStack itemStack = event.itemStack;
			int maxDurability = itemStack.getMaxDamage();
			if (maxDurability > 1 && !itemStack.getHasSubtypes()) {
				int currentDurability = maxDurability - itemStack.getItemDamage();
				event.toolTip.add(color + "Durability: " + currentDurability + "/" + maxDurability);
			}
			if (itemStack.getItem() instanceof ItemArmor) {
				ItemArmor armor = ((ItemArmor) itemStack.getItem());
				int protection = armor.damageReduceAmount;
				if (protection > 0) {
					event.toolTip.add(color + "+" + protection + " Defense");
				}
				if (itemStack.getItem().getItemEnchantability() > 0) {
					int enchantability = itemStack.getItem().getItemEnchantability();
					int[] enchantabilities = {9, 10, 12, 15, 25, 26, 30};
					String[] enchantabilityNames = {"Iron", "Diamond", "Chain", "Leather", "Gold", "Better than Gold", "Better than wolves(tm)"};
					String betterThan = "Worse than Iron";
					for (int i = 0; i < enchantabilities.length && enchantability >= enchantabilities[i]; i++) {
						betterThan = enchantabilityNames[i];
					}
					if (itemStack.getEnchantmentTagList() == null) {
						event.toolTip.add(color + "Enchantability: " + betterThan);
					}
				}
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
				event.toolTip.add(color + "Tier: " + toolTier);
				float[] efficiencies = {2.0f, 4.0f, 6.0f, 8.0f, 12.0f, 14.0f, 20.0f};
				String[] efficiencyNames = {"Wood", "Stone", "Iron", "Diamond", "Gold", "Faster than Gold", "Faster than water!"};
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
				event.toolTip.add(color + "Mining Speed: " + fasterThan);
				if (itemStack.getItem().getItemEnchantability() > 0) {
					int enchantability = itemStack.getItem().getItemEnchantability();
					int[] enchantabilities = {5, 10, 14, 15, 22, 23};
					String[] enchantabilityNames = {"Stone", "Diamond", "Iron", "Wood", "Gold", "Better than Gold", "Better than wolves(tm)"};
					String betterThan = "Worse than Stone";
					for (int i = 0; i < enchantabilities.length && enchantability >= enchantabilities[i]; i++) {
						betterThan = enchantabilityNames[i];
					}
					if (itemStack.getEnchantmentTagList() == null) {
						event.toolTip.add(color + "Enchantability: " + betterThan);
					}
				}
			}
			if (itemStack.getItem() instanceof ItemBlock) {
				Block block = ((ItemBlock) itemStack.getItem()).field_150939_a;
				float power = block.getEnchantPowerBonus(event.entityPlayer.worldObj, 0, 0, 0);
				if (power > 0) {
					event.toolTip.add(color + "Enchanting: " + power + " Bookshelves");
				}
			}
		}
	}
}
