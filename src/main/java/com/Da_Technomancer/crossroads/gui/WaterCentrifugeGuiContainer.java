package com.Da_Technomancer.crossroads.gui;

import com.Da_Technomancer.crossroads.API.templates.IGuiObject;
import com.Da_Technomancer.crossroads.API.templates.MachineGUI;
import com.Da_Technomancer.crossroads.Crossroads;
import com.Da_Technomancer.crossroads.gui.container.WaterCentrifugeContainer;
import com.Da_Technomancer.crossroads.tileentities.fluid.WaterCentrifugeTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class WaterCentrifugeGuiContainer extends MachineGUI{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Crossroads.MODID, "textures/gui/container/steam_boiler_gui.png");

	public WaterCentrifugeGuiContainer(IInventory playerInv, WaterCentrifugeTileEntity te){
		super(new WaterCentrifugeContainer(playerInv, te));
	}

	@Override
	public void initGui(){
		super.initGui();
		guiObjects = new IGuiObject[2];
		guiObjects[0] = new FluidGuiObject(this, 0, 1,10_000, (width - xSize) / 2, (height - ySize) / 2, 10, 70);
		guiObjects[1] = new FluidGuiObject(this, 2, 3,10_000, (width - xSize) / 2, (height - ySize) / 2, 70, 70);
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(TEXTURE);

		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		blit(i, j, 0, 0, xSize, ySize);


		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}
}