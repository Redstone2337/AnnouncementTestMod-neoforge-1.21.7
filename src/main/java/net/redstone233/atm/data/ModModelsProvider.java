package net.redstone233.atm.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.redstone233.atm.AnnouncementTestMod;
import net.redstone233.atm.item.ModItems;
import org.jetbrains.annotations.NotNull;

public class ModModelsProvider extends ModelProvider {

    public ModModelsProvider(PackOutput output) {
        super(output, AnnouncementTestMod.MOD_ID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.BLAZING_FLAME_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICE_FREEZE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.TEST_ITEM.get(), ModelTemplates.FLAT_ITEM);
    }
}
