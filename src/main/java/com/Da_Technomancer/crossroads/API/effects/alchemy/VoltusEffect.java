package com.Da_Technomancer.crossroads.API.effects.alchemy;

import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.API.alchemy.EnumMatterPhase;
import com.Da_Technomancer.crossroads.API.alchemy.ReagentMap;
import com.Da_Technomancer.crossroads.render.CRRenderUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class VoltusEffect implements IAlchEffect{

	private static final Color[] BOLT_COLORS = new Color[] {new Color(255, 255, 0, 220), new Color(255, 228, 34, 220), new Color(255, 194, 62, 220)};

	@Override
	public void doEffect(World world, BlockPos pos, int amount, EnumMatterPhase phase, ReagentMap reags) {
		//The odds of spawning a bolt in flame form is decreased due to the much larger number of total calls to this method there will be
		if(Math.random() > (phase == EnumMatterPhase.FLAME ? 0.92D : phase == EnumMatterPhase.SOLID ? 0 : 0.8D)){
			List<LivingEntity> ents = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos.getX() - 5, pos.getY() - 5, pos.getZ() - 5, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5), EntityPredicates.IS_ALIVE);
			for(LivingEntity ent : ents){
				MiscUtil.attackWithLightning(ent, 10, null);
				CRRenderUtil.addArc(world, (float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F, (float) ent.getPosX(), (float) ent.getPosY(), (float) ent.getPosZ(), 1, 0F, BOLT_COLORS[(int) (world.getGameTime() % 3)].getRGB());
			}
		}
	}

	@Override
	public ITextComponent getName(){
		return new TranslationTextComponent("effect.electric");
	}
}
