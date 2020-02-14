package com.Da_Technomancer.crossroads;

import com.Da_Technomancer.crossroads.API.Capabilities;
import com.Da_Technomancer.crossroads.API.packets.CRPackets;
import com.Da_Technomancer.crossroads.blocks.CRBlocks;
import com.Da_Technomancer.crossroads.command.CRCommands;
import com.Da_Technomancer.crossroads.entity.*;
import com.Da_Technomancer.crossroads.fluids.CRFluids;
import com.Da_Technomancer.crossroads.gui.*;
import com.Da_Technomancer.crossroads.gui.container.*;
import com.Da_Technomancer.crossroads.items.CRItems;
import com.Da_Technomancer.crossroads.items.crafting.recipes.*;
import com.Da_Technomancer.crossroads.items.itemSets.ItemSets;
import com.Da_Technomancer.crossroads.particles.CRParticles;
import com.Da_Technomancer.crossroads.particles.ColorParticleType;
import com.Da_Technomancer.crossroads.render.TESR.AAModTESR;
import com.Da_Technomancer.crossroads.tileentities.CRTileEntity;
import com.Da_Technomancer.crossroads.world.ModWorldGen;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.Da_Technomancer.crossroads.Crossroads.MODID;

@Mod(MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Crossroads{

	public static final String MODID = "crossroads";
	public static final String MODNAME = "Crossroads";
	public static final Logger logger = LogManager.getLogger(MODNAME);

	public Crossroads(){
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::commonInit);
		bus.addListener(this::clientInit);

		CRConfig.init();

		MinecraftForge.EVENT_BUS.register(this);

		CRConfig.load();
	}

	private void commonInit(@SuppressWarnings("unused") FMLCommonSetupEvent e){
		//Pre
		CRPackets.preInit();
		Capabilities.register();
//		CrossroadsTileEntity.init();
		CRPackets.preInit();
//		ModDimensions.init();
		//Main
		MinecraftForge.EVENT_BUS.register(new EventHandlerCommon());
		//NetworkRegistry.INSTANCE.registerGuiHandler(Crossroads.instance, new GuiHandler());
//		ModIntegration.init();

//		CrossroadsConfig.config.save();

//		ModIntegration.preInit();
	}

	private void clientInit(@SuppressWarnings("unused") FMLClientSetupEvent e){
//		TESRRegistry.init();
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
//		ModelLoaderRegistry.registerLoader(new BakedModelLoader());
		ModEntities.clientInit();
		CRItems.clientInit();
		AAModTESR.registerBlockRenderer();
		Keys.init();
		CRParticles.clientInit();
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e){
		IForgeRegistry<Block> registry = e.getRegistry();
		CRBlocks.init();
		CRFluids.init();
		ItemSets.init();
		for(Block block : CRBlocks.toRegister){
			registry.register(block);
		}
		CRBlocks.toRegister.clear();
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e){
		IForgeRegistry<Item> registry = e.getRegistry();
		CRItems.init();
		for(Item item : CRItems.toRegister){
			registry.register(item);
		}
		CRItems.toRegister.clear();
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> e){
		IForgeRegistry<IRecipeSerializer<?>> reg = e.getRegistry();
		reg.register(new SingleIngrRecipe.SingleRecipeSerializer<>(StampMillRec::new).setRegistryName("stamp_mill"));
		reg.register(new MillRec.Serializer().setRegistryName("mill"));
		reg.register(new SingleIngrRecipe.SingleRecipeSerializer<>(OreCleanserRec::new).setRegistryName("ore_cleanser"));
		reg.register(new BeamExtractRec.Serializer().setRegistryName("beam_extract"));
		reg.register(new IceboxRec.Serializer().setRegistryName("cooling"));
		reg.register(new CentrifugeRec.Serializer().setRegistryName("centrifuge"));
		reg.register(new AlchemyRec.Serializer().setRegistryName("alchemy"));
		reg.register(new BlastFurnaceRec.Serializer().setRegistryName("cr_blast_furnace"));
		reg.register(new FluidCoolingRec.Serializer().setRegistryName("fluid_cooling"));
		reg.register(new CrucibleRec.Serializer().setRegistryName("crucible"));
		reg.register(new DetailedCrafterRec.Serializer().setRegistryName("detailed_crafter"));
		reg.register(new BeamTransmuteRec.Serializer().setRegistryName("beam_transmute"));
		reg.register(new BoboRec.Serializer().setRegistryName("bobo"));
		reg.register(new CopshowiumRec.Serializer().setRegistryName("copshowium"));
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerEnts(RegistryEvent.Register<EntityType<?>> e){
		IForgeRegistry<EntityType<?>> registry = e.getRegistry();

		registry.register(EntityType.Builder.create(EntityFlameCore::new, EntityClassification.MISC).immuneToFire().disableSummoning().setShouldReceiveVelocityUpdates(false).size(1, 1).build("flame_core"));
		registry.register(EntityType.Builder.<EntityBullet>create(EntityBullet::new, EntityClassification.MISC).immuneToFire().setTrackingRange(64).setUpdateInterval(5).size(0.25F, 0.25F).build("bullet"));
		registry.register(EntityType.Builder.<EntityShell>create(EntityShell::new, EntityClassification.MISC).immuneToFire().setTrackingRange(64).setUpdateInterval(5).size(.25F, .25F).build("shell"));
		registry.register(EntityType.Builder.<EntityNitro>create(EntityNitro::new, EntityClassification.MISC).setTrackingRange(64).setUpdateInterval(5).build("nitro"));
		registry.register(EntityType.Builder.<EntityGhostMarker>create(EntityGhostMarker::new, EntityClassification.MISC).setTrackingRange(64).setUpdateInterval(20).immuneToFire().setShouldReceiveVelocityUpdates(false).build("ghost_marker"));
//		registry.register(EntityType.Builder.create(EntityArmRidable::new, EntityClassification.MISC).immuneToFire().setUpdateInterval(1).disableSummoning().setTrackingRange(64).size(0.01F, 0.01F).build("arm_ridable"));
		registry.register(EntityType.Builder.create(EntityFlyingMachine::new, EntityClassification.MISC).size(1F, 1.3F).setTrackingRange(64).setUpdateInterval(1).build("flying_machine"));

//		EntityRegistry.registerModEntity(new ResourceLocation(Crossroads.MODID, "bullet"), EntityBullet.class, "bullet", id++, Crossroads.instance, 64, 5, true);
//		EntityRegistry.registerModEntity(new ResourceLocation(Crossroads.MODID, "arm_ridable"), EntityArmRidable.class, "arm_ridable", id++, Crossroads.instance, 64, 1, false);
//		EntityRegistry.registerModEntity(new ResourceLocation(Crossroads.MODID, "shell"), EntityShell.class, "shell", id++, Crossroads.instance, 64, 5, true);
//		EntityRegistry.registerModEntity(new ResourceLocation(Crossroads.MODID, "nitro"), EntityNitro.class, "nitro", id++, Crossroads.instance, 64, 5, true);
//		EntityRegistry.registerModEntity(new ResourceLocation(Crossroads.MODID, "flying_machine"), EntityFlyingMachine.class, "flying_machine", id++, Crossroads.instance, 64, 1, true);
//		EntityRegistry.registerModEntity(new ResourceLocation(Crossroads.MODID, "flame_core"), EntityFlameCore.class, "flame_core", id++, Crossroads.instance, 64, 20, true);
//		EntityRegistry.registerModEntity(new ResourceLocation(Crossroads.MODID, "ghost_marker"), EntityGhostMarker.class, "ghost_marker", id++, Crossroads.instance, 64, 20, true);
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerParticles(RegistryEvent.Register<ParticleType<?>> e){
		IForgeRegistry<ParticleType<?>> registry = e.getRegistry();
		registry.register(new ColorParticleType("color_flame", false));
		registry.register(new ColorParticleType("color_gas", false));
		registry.register(new ColorParticleType("color_liquid", false));
		registry.register(new ColorParticleType("color_solid", false));
		registry.register(new ColorParticleType("color_splash", false));
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> e){
		IForgeRegistry<TileEntityType<?>> reg = e.getRegistry();
		CRTileEntity.init(reg);
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	@OnlyIn(Dist.CLIENT)
	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> e){
		registerCon(FireboxContainer::new, FireboxScreen::new, "firebox", e);
		registerCon(IceboxContainer::new, IceboxScreen::new, "icebox", e);
		registerCon(FluidCoolerContainer::new, FluidCoolerScreen::new, "fluid_cooler", e);
		registerCon(CrucibleContainer::new, CrucibleScreen::new, "crucible", e);
		registerCon(SaltReactorContainer::new, SaltReactorScreen::new, "salt_reactor", e);
		registerCon(SmelterContainer::new, SmelterScreen::new, "smelter", e);
		registerCon(BlastFurnaceContainer::new, BlastFurnaceScreen::new, "ind_blast_furnace", e);
		registerCon(MillstoneContainer::new, MillstoneScreen::new, "millstone", e);
		registerCon(StampMillContainer::new, StampMillScreen::new, "stamp_mill", e);
		registerCon(FatCollectorContainer::new, FatCollectorScreen::new, "fat_collector", e);
		registerCon(FatCongealerContainer::new, FatCongealerScreen::new, "fat_congealer", e);
		registerCon(FatFeederContainer::new, FatFeederScreen::new, "fat_feeder", e);
		registerCon(FluidTankContainer::new, FluidTankScreen::new, "fluid_tank", e);
		registerCon(OreCleanserContainer::new, OreCleanserScreen::new, "ore_cleanser", e);
		registerCon(RadiatorContainer::new, RadiatorScreen::new, "radiator", e);
		registerCon(SteamBoilerContainer::new, SteamBoilerScreen::new, "steam_boiler", e);
		registerCon(WaterCentrifugeContainer::new, WaterCentrifugeScreen::new, "water_centrifuge", e);
		registerCon(ColorChartContainer::new, ColorChartScreen::new, "color_chart", e);
		registerCon(BeamExtractorContainer::new, BeamExtractorScreen::new, "beam_extractor", e);
		registerCon(HeatLimiterContainer::new, HeatLimiterScreen::new, "heat_limiter", e);
		registerCon(RotaryPumpContainer::new, RotaryPumpScreen::new, "rotary_pump", e);
		registerCon(DetailedCrafterContainer::new, DetailedCrafterScreen::new, "detailed_crafter", e);
		registerCon(ReagentFilterContainer::new, ReagentFilterScreen::new, "reagent_filter", e);
		registerCon(CopshowiumMakerContainer::new, CopshowiumMakerScreen::new, "copshowium_maker", e);
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> e){
		registerConType(FireboxContainer::new, "firebox", e);
		registerConType(IceboxContainer::new, "icebox", e);
		registerConType(FluidCoolerContainer::new, "fluid_cooler", e);
		registerConType(CrucibleContainer::new, "crucible", e);
		registerConType(SaltReactorContainer::new, "salt_reactor", e);
		registerConType(SmelterContainer::new, "smelter", e);
		registerConType(BlastFurnaceContainer::new, "ind_blast_furnace", e);
		registerConType(MillstoneContainer::new, "millstone", e);
		registerConType(StampMillContainer::new, "stamp_mill", e);
		registerConType(FatCollectorContainer::new, "fat_collector", e);
		registerConType(FatCongealerContainer::new, "fat_congealer", e);
		registerConType(FatFeederContainer::new, "fat_feeder", e);
		registerConType(FluidTankContainer::new, "fluid_tank", e);
		registerConType(OreCleanserContainer::new, "ore_cleanser", e);
		registerConType(RadiatorContainer::new, "radiator", e);
		registerConType(SteamBoilerContainer::new, "steam_boiler", e);
		registerConType(WaterCentrifugeContainer::new, "water_centrifuge", e);
		registerConType(ColorChartContainer::new, "color_chart", e);
		registerConType(BeamExtractorContainer::new, "beam_extractor", e);
		registerConType(HeatLimiterContainer::new, "heat_limiter", e);
		registerConType(RotaryPumpContainer::new, "rotary_pump", e);
		registerConType(DetailedCrafterContainer::new, "detailed_crafter", e);
		registerConType(ReagentFilterContainer::new, "reagent_filter", e);
		registerConType(CopshowiumMakerContainer::new, "copshowium_maker", e);
	}

	/**
	 * Creates and registers a container type
	 * @param cons Container factory
	 * @param id The ID to use
	 * @param reg Registry event
	 * @param <T> Container subclass
	 * @return The newly created type
	 */
	private static <T extends Container> ContainerType<T> registerConType(IContainerFactory<T> cons, String id, RegistryEvent.Register<ContainerType<?>> reg){
		ContainerType<T> contType = new ContainerType<>(cons);
		contType.setRegistryName(new ResourceLocation(MODID, id));
		reg.getRegistry().register(contType);
		return contType;
	}

	/**
	 * Creates and registers both a container type and a screen factory. Not usable on the physical server due to screen factory.
	 * @param cons Container factory
	 * @param screenFactory The screen factory to be linked to the type
	 * @param id The ID to use
	 * @param reg Registery event
	 * @param <T> Container subclass
	 */
	@OnlyIn(Dist.CLIENT)
	private static <T extends Container> void registerCon(IContainerFactory<T> cons, ScreenManager.IScreenFactory<T, ContainerScreen<T>> screenFactory, String id, RegistryEvent.Register<ContainerType<?>> reg){
		ContainerType<T> contType = registerConType(cons, id, reg);
		ScreenManager.registerFactory(contType, screenFactory);
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void registerFluids(RegistryEvent.Register<Fluid> e){
		IForgeRegistry<Fluid> registry = e.getRegistry();
		for(Fluid f : CRFluids.toRegister){
			registry.register(f);
		}
		CRFluids.toRegister.clear();
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void registerWorldgen(RegistryEvent.Register<Feature<?>> e){
		ModWorldGen.register(e.getRegistry());
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void serverLoading(FMLServerStartingEvent e){
		CRCommands.init(e.getCommandDispatcher());
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void serverStarted(FMLServerStartedEvent e){
		MinecraftForge.EVENT_BUS.register(new EventHandlerServer());
	}
}