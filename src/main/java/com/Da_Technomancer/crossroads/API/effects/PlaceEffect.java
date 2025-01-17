package com.Da_Technomancer.crossroads.API.effects;

import com.Da_Technomancer.crossroads.API.MiscUtil;
import com.Da_Technomancer.crossroads.API.beams.EnumBeamAlignments;
import com.Da_Technomancer.crossroads.CRConfig;
import com.Da_Technomancer.crossroads.Crossroads;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.util.List;

public class PlaceEffect extends BeamEffect{

	public static FakePlayer getBlockFakePlayer(ServerWorld world){
		GameProfile fakePlayerProfile = new GameProfile(null, Crossroads.MODID + "-block-fake-player-" + MiscUtil.getDimensionName(world));
		return FakePlayerFactory.get(world, fakePlayerProfile);
	}

	@Override
	public void doBeamEffect(EnumBeamAlignments align, boolean voi, int power, World worldIn, BlockPos pos, @Nullable Direction dir){
		if(!performTransmute(align, voi, power, worldIn, pos)){
			if(voi){
				if(!CRConfig.isProtected(worldIn, pos, worldIn.getBlockState(pos))){
					worldIn.destroyBlock(pos, true);
				}
			}else{
				double range = Math.sqrt(power) / 2D;
				List<ItemEntity> items = worldIn.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(Vector3d.atCenterOf(pos).add(-range, -range, -range), Vector3d.atCenterOf(pos).add(range, range, range)), EntityPredicates.ENTITY_STILL_ALIVE);
				if(items.size() != 0){
					FakePlayer placer = getBlockFakePlayer((ServerWorld) worldIn);
					for(ItemEntity ent : items){
						ItemStack stack = ent.getItem();
						if(!stack.isEmpty() && stack.getItem() instanceof BlockItem){
							BlockItemUseContext context = new BlockItemUseContext(new ItemUseContext(placer, Hand.MAIN_HAND, new BlockRayTraceResult(new Vector3d(ent.getX(), ent.getY(), ent.getZ()), Direction.DOWN, ent.blockPosition(), false)));
							BlockState state = ((BlockItem) stack.getItem()).getBlock().getStateForPlacement(context);
							BlockState worldState = worldIn.getBlockState(ent.blockPosition());
							if(worldState.canBeReplaced(context) && state.canSurvive(worldIn, ent.blockPosition())){
								worldIn.setBlockAndUpdate(ent.blockPosition(), state);
								state.getBlock().setPlacedBy(worldIn, ent.blockPosition(), worldIn.getBlockState(ent.blockPosition()), placer, stack);
								SoundType soundtype = state.getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, placer);
								worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
								stack.shrink(1);
								if(stack.getCount() <= 0){
									ent.remove();
								}
							}
						}
					}
				}
			}
		}
	}
}