package com.Da_Technomancer.crossroads.gui;

import com.Da_Technomancer.crossroads.API.templates.FluidGuiObject;
import com.Da_Technomancer.crossroads.API.templates.IGuiObject;
import com.Da_Technomancer.crossroads.API.templates.InventoryTE;
import com.Da_Technomancer.crossroads.API.templates.MachineGUI;
import com.Da_Technomancer.crossroads.Main;
import com.Da_Technomancer.crossroads.gui.container.SteamBoilerContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class SaltReactorGuiContainer extends MachineGUI{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/container/salt_reactor_gui.png");

	public SaltReactorGuiContainer(IInventory playerInv, InventoryTE te){
		super(new SteamBoilerContainer(playerInv, te));
	}

	@Override
	public void initGui(){
		super.initGui();
		guiObjects = new IGuiObject[2];
		guiObjects[0] = new FluidGuiObject(this, 0, 1,3_200, (width - xSize) / 2, (height - ySize) / 2, 10, 70);
		guiObjects[1] = new FluidGuiObject(this, 2, 3,3_200, (width - xSize) / 2, (height - ySize) / 2, 70, 70);
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(TEXTURE);

		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(i, j, 0, 0, xSize, ySize);


		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}
}
