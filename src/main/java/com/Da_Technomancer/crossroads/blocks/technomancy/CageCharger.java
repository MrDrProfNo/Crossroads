package com.Da_Technomancer.crossroads.blocks.technomancy;

import com.Da_Technomancer.crossroads.API.CRProperties;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.items.CRItems;
import com.Da_Technomancer.crossroads.items.technomancy.BeamCage;
import com.Da_Technomancer.crossroads.tileentities.technomancy.CageChargerTileEntity;
import com.Da_Technomancer.essentials.blocks.redstone.IReadable;
import com.Da_Technomancer.essentials.blocks.redstone.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CageCharger extends ContainerBlock implements IReadable{

	private static final VoxelShape SHAPE = VoxelShapes.or(box(0, 0, 0, 16, 4, 16), box(4, 4, 4, 12, 8, 12));

	public CageCharger(){
		super(CRBlocks.getMetalProperty());
		String name = "cage_charger";
		setRegistryName(name);
		CRBlocks.toRegister.add(this);
		CRBlocks.blockAddQue(this);
		registerDefaultState(defaultBlockState().setValue(CRProperties.ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
		builder.add(CRProperties.ACTIVE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return SHAPE;
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn){
		return new CageChargerTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state){
		return BlockRenderType.MODEL;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		tooltip.add(new TranslationTextComponent("tt.crossroads.cage_charger.desc"));
		tooltip.add(new TranslationTextComponent("tt.crossroads.cage_charger.redstone"));
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit){
		TileEntity te;
		if(!worldIn.isClientSide && (te = worldIn.getBlockEntity(pos)) != null){
			if(state.getValue(CRProperties.ACTIVE)){
				playerIn.inventory.add(((CageChargerTileEntity) te).getCage());
				((CageChargerTileEntity) te).setCage(ItemStack.EMPTY);
				worldIn.setBlockAndUpdate(pos, defaultBlockState().setValue(CRProperties.ACTIVE, false));
			}else if(!playerIn.getItemInHand(hand).isEmpty() && playerIn.getItemInHand(hand).getItem() == CRItems.beamCage){
				((CageChargerTileEntity) te).setCage(playerIn.getItemInHand(hand));
				playerIn.setItemInHand(hand, ItemStack.EMPTY);
				worldIn.setBlockAndUpdate(pos, defaultBlockState().setValue(CRProperties.ACTIVE, true));
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving){
		if(!isMoving && state.getValue(CRProperties.ACTIVE) && newState.getBlock() != this){
			InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((CageChargerTileEntity) world.getBlockEntity(pos)).getCage());
		}
		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state){
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos){
		return RedstoneUtil.clampToVanilla(read(worldIn, pos, blockState));
	}

	@Override
	public float read(World world, BlockPos pos, BlockState state){
		if(state.getValue(CRProperties.ACTIVE)){
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof CageChargerTileEntity){
				ItemStack cage = ((CageChargerTileEntity) te).getCage();
				return BeamCage.getStored(cage).getPower();
			}
		}
		return 0;
	}
}
