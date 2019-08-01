package com.fantasticsource.unstuck;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static net.minecraft.util.text.TextFormatting.AQUA;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class Commands extends CommandBase
{
    private String name;

    public Commands(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return AQUA + "/" + name + WHITE + " - Helps get you unstuck...hopefully";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (!(sender instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (!Loader.isModLoaded("waystones"))
        {
            notifyCommandListener(sender, this, "Waystones is not installed on the server!");
            return;
        }
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("waystones:warp_stone"));
        if (item == null)
        {
            notifyCommandListener(sender, this, "Waystones is installed, but the warp stone was not found!?");
            return;
        }
        ItemStack stack = new ItemStack(item);
        if (player.inventory.hasItemStack(stack))
        {
            notifyCommandListener(sender, this, "You already have a warp stone!");
            return;
        }
        if (player.inventory.addItemStackToInventory(stack))
        {
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryContainer.detectAndSendChanges();
            notifyCommandListener(sender, this, "Use this warp stone!");
        }
        else notifyCommandListener(sender, this, "Your inventory is full!");
    }
}
