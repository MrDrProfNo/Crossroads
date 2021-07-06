package com.Da_Technomancer.crossroads.API.witchcraft;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityTemplate implements INBTSerializable<CompoundNBT>{

	public static final String RESPAWNING_KEY = "cr_respawning";
	public static final String LOYAL_KEY = "cr_loyal";

	private ResourceLocation entityName;
	private boolean loyal;
	private boolean respawning;
	private ArrayList<EffectInstance> effects;//Durations of these effects are ignored when applying
	private CompoundNBT data;
	private int degradation;

	//Cache generated based on entity name
	private EntityType<?> entityType;

	public EntityTemplate(){

	}

	public EntityTemplate(EntityTemplate template){
		this.entityName = template.entityName;
		this.entityType = null;
		this.loyal = template.loyal;
		this.respawning = template.respawning;
		this.effects = template.effects;
		this.data = template.data;
		this.degradation = template.degradation;
	}

	public EntityTemplate(ResourceLocation entityName, boolean loyal, boolean respawning, ArrayList<EffectInstance> effects, CompoundNBT data, int degradation){
		this.entityName = entityName;
		this.loyal = loyal;
		this.respawning = respawning;
		this.effects = effects;
		this.data = data;
		this.degradation = degradation;
	}

	public ResourceLocation getEntityName(){
		return entityName;
	}

	public void setEntityName(ResourceLocation entityName){
		this.entityName = entityName;
		entityType = null;
	}

	@Nullable
	public EntityType<?> getEntityType(){
		if(entityType == null){
			//Generate a cache based on entityName
			entityType = entityName == null ? null : ForgeRegistries.ENTITIES.getValue(entityName);
		}
		return entityType;
	}

	public boolean isLoyal(){
		return loyal;
	}

	public void setLoyal(boolean loyal){
		this.loyal = loyal;
	}

	public boolean isRespawning(){
		return respawning;
	}

	public void setRespawning(boolean respawning){
		this.respawning = respawning;
	}

	@Nonnull
	public ArrayList<EffectInstance> getEffects(){
		if(effects == null){
			effects = new ArrayList<>(0);
		}
		return effects;
	}

	public void setEffects(ArrayList<EffectInstance> effects){
		this.effects = effects;
	}

	public CompoundNBT getData(){
		return data;
	}

	public void setData(CompoundNBT data){
		this.data = data;
	}

	public int getDegradation(){
		return degradation;
	}

	public void setDegradation(int degradation){
		this.degradation = degradation;
	}

	@Override
	public CompoundNBT serializeNBT(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("entity_name", entityName.toString());
		nbt.putBoolean("loyal", loyal);
		nbt.putBoolean("respawning", respawning);
		ListNBT potions = new ListNBT();
		if(effects != null){
			for(EffectInstance instance : effects){
				potions.add(instance.save(new CompoundNBT()));
			}
		}
		nbt.put("potions", potions);
		nbt.put("data", data);
		nbt.putInt("degradation", degradation);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt){
		entityName = new ResourceLocation(nbt.getString("entity_name"));
		entityType = null;
		loyal = nbt.getBoolean("loyal");
		respawning = nbt.getBoolean("respawning");
		ListNBT potions = nbt.getList("potions", 10);//ID 10 is CompoundNBT
		effects = new ArrayList<>();
		for(int i = 0; i < potions.size(); i++){
			effects.add(EffectInstance.load(potions.getCompound(i)));
		}
		data = nbt.getCompound("data");
		degradation = nbt.getInt("degradation");
	}

	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		EntityTemplate that = (EntityTemplate) o;
		return loyal == that.loyal &&
				respawning == that.respawning &&
				degradation == that.degradation &&
				Objects.equals(entityName, that.entityName) &&
				Objects.equals(effects, that.effects) &&
				Objects.equals(data, that.data);
	}

	@Override
	public int hashCode(){
		return Objects.hash(entityName, loyal, respawning, effects, data, degradation);
	}

	/**
	 * Adds a tooltip based on the information in this template
	 * @param tooltips The tooltip to append to
	 * @param maxLines The maximum lines to append. This value will be ignored for essential information
	 */
	public void addTooltip(List<ITextComponent> tooltips, int maxLines){
		getEntityType();//Builds the cache
		if(entityName == null){
			tooltips.add(new TranslationTextComponent("tt.crossroads.boilerplate.entity_template.type.missing"));
			//Error message, nothing else
		}else{
			tooltips.add(new TranslationTextComponent("tt.crossroads.boilerplate.entity_template.type").append(entityType == null ? new StringTextComponent(entityName.toString()) : entityType.getDescription()));
			TextComponent detailsCompon = new TranslationTextComponent("tt.crossroads.boilerplate.entity_template.degradation", degradation);
			if(loyal){
				detailsCompon.append(new TranslationTextComponent("tt.crossroads.boilerplate.entity_template.loyal"));
			}
			if(respawning){
				detailsCompon.append(new TranslationTextComponent("tt.crossroads.boilerplate.entity_template.respawning"));
			}
			tooltips.add(detailsCompon);

			int lines = 2;
			int effectCount = effects.size();
			int needExtension = Math.max(0, effectCount - (maxLines - lines));
			for(EffectInstance effect : effects){
				if(lines < maxLines || needExtension > 0 && lines < maxLines - 1){
					tooltips.add(new TranslationTextComponent("tt.crossroads.boilerplate.entity_template.potion").append(effect.getEffect().getDisplayName()).append(new TranslationTextComponent("enchantment.level." + (effect.getAmplifier() + 1))));
					lines++;
				}else{
					break;
				}
			}
			if(needExtension > 0){
				tooltips.add(new TranslationTextComponent("tt.crossroads.boilerplate.entity_template.potion.additional", needExtension));
			}
		}
	}
}
