package com.Da_Technomancer.crossroads.items.itemSets;

import com.Da_Technomancer.crossroads.items.CRItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public abstract class GearMatItem extends Item{

	protected static final String KEY = "material";
	protected static final Properties itemProp = new Properties().group(CRItems.TAB_GEAR);

	protected GearMatItem(){
		super(itemProp);
	}

	public ItemStack withMaterial(GearFactory.GearMaterial mat, int count){
		if(mat == null){
			mat = GearFactory.getDefaultMaterial();
		}
		ItemStack out = new ItemStack(this, count, new CompoundNBT());
		out.getTag().putString(KEY, mat.getId());
		return out;
	}

	/**
	 * @param stack A stack with a GearMatItem
	 * @return The configured GearMaterial. Null if it has a non-existent material, iron if not configured (default)
	 */
	@Nullable
	public static GearFactory.GearMaterial getMaterial(ItemStack stack){
		String matKey;
		if(!stack.hasTag()){
			return GearFactory.getDefaultMaterial();
		}else{
			matKey = stack.getTag().getString(KEY);
		}
		return GearFactory.findMaterial(matKey);
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack){
		GearFactory.GearMaterial mat = getMaterial(stack);
		return new TranslationTextComponent(getTranslationKey(stack), mat == null ? "INVALID" : mat.getName());
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(isInGroup(group)){
			//Add every material variant of this item
			for(GearFactory.GearMaterial mat : GearFactory.getMaterials()){
				items.add(withMaterial(mat, 1));
			}
		}
	}

	protected abstract double shapeFactor();

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced){
		GearFactory.GearMaterial mat = getMaterial(stack);
		if(mat != null){
			tooltip.add(new TranslationTextComponent("tt.crossroads.boilerplate.inertia", + mat.getDensity() * shapeFactor()));
		}
	}
}