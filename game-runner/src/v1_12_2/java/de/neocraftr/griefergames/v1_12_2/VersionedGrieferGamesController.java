package de.neocraftr.griefergames.v1_12_2;

import de.neocraftr.griefergames.utils.GrieferGamesController;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import javax.inject.Singleton;

@Singleton
@Implements(GrieferGamesController.class)
public class VersionedGrieferGamesController extends GrieferGamesController {

  @Override
  public boolean playerAllowedFlying() {
    return Minecraft.getMinecraft().player.capabilities.allowFlying;
  }

  @Override
  public boolean hideBoosterMenu() {
    if(Minecraft.getMinecraft().player == null) return false;
    Container cont = Minecraft.getMinecraft().player.openContainer;
    if(cont instanceof ContainerChest) {
      ContainerChest chest = (ContainerChest) cont;
      IInventory inv = chest.getLowerChestInventory();
      if(inv.getName().equals("§6Booster - Übersicht")) {
        Minecraft.getMinecraft().player.closeScreen();
        return true;
      }
    }
    return false;
  }
}
