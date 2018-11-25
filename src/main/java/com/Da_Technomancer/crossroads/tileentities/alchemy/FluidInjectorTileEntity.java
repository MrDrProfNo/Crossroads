package com.Da_Technomancer.crossroads.tileentities.alchemy;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.alchemy.AlchemyCarrierTE;
import com.Da_Technomancer.crossroads.API.alchemy.EnumTransferMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidInjectorTileEntity extends AlchemyCarrierTE{

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock() != newState.getBlock();
	}

	public FluidInjectorTileEntity(){
		super();
	}

	public FluidInjectorTileEntity(boolean glass){
		super(glass);
	}

	@Override
	protected EnumTransferMode[] getModes(){
		return new EnumTransferMode[] {EnumTransferMode.BOTH, EnumTransferMode.BOTH, EnumTransferMode.NONE, EnumTransferMode.NONE, EnumTransferMode.NONE, EnumTransferMode.NONE};
	}

	@Override
	protected void performTransfer(){
		for(int i = 0; i < 2; i++){
			if(amount != 0){
				EnumFacing side = EnumFacing.byIndex(i);
				TileEntity te = world.getTileEntity(pos.offset(side.getOpposite()));
				if(te != null && te.hasCapability(Capabilities.CHEMICAL_HANDLER_CAPABILITY, side)){
					if(te.getCapability(Capabilities.CHEMICAL_HANDLER_CAPABILITY, side).insertReagents(contents, side, handler)){
						correctReag();
						markDirty();
					}
				}
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing side){
		if(cap == Capabilities.CHEMICAL_HANDLER_CAPABILITY && (side == null || side == EnumFacing.DOWN)){
			return true;
		}
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (side == null || side == EnumFacing.UP)){
			return true;
		}
		return super.hasCapability(cap, side);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing side){
		if(cap == Capabilities.CHEMICAL_HANDLER_CAPABILITY && (side == null || side == EnumFacing.DOWN)){
			return (T) handler;
		}
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			if(side == null || side == EnumFacing.UP){
				return (T) falseFluidHandler;
			}
		}
		return super.getCapability(cap, side);
	}
}
