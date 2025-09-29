package net.redstone233.atm.materials;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;
import net.redstone233.atm.AnnouncementTestMod;

public class ModToolMaterials {
    public static final ToolMaterial SPECIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 3000, 17.0F, 15.0F, 17, ItemTags.NETHERITE_TOOL_MATERIALS
    );

    public static void init() {
        AnnouncementTestMod.LOGGER.info("工具材料注册完毕！");
    }
}
