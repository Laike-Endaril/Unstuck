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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.AQUA;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class Commands extends CommandBase
{
    @Override
    public String getName()
    {
        return "unstuck";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return AQUA + "/unstuck warpstone" + WHITE + " - Gives you a free warp stone from the waystones mod";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (!(sender instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (args.length > 0)
        {
            switch (args[0])
            {
                case "warpstone":
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
                    break;

                default:
                    notifyCommandListener(sender, this, getUsage(sender));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, "warpstone");

        return new ArrayList<>();
    }
}
