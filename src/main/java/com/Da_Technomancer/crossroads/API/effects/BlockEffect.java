package com.Da_Technomancer.crossroads.API.effects;

import com.Da_Technomancer.crossroads.CRConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEffect implements IEffect{

	private final BlockState block;

	public BlockEffect(BlockState block){
		this.block = block;
	}

	@Override
	public void doEffect(World worldIn, BlockPos pos){
		BlockState prev = worldIn.getBlockState(pos);
		if(prev == block){
			return;
		}

		if(CRConfig.isProtected(worldIn, pos, worldIn.getBlockState(pos))){
			return;
		}
		worldIn.setBlock(pos, block, 3);
		SoundType soundtype = block.getBlock().getSoundType(block, worldIn, pos, null);
		worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
	}
}
