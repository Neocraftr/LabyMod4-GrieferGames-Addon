package de.neocraftr.griefergames.v1_20_1;

import de.neocraftr.griefergames.utils.GrieferGamesController;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implements(GrieferGamesController.class)
public class VersionedGrieferGamesController extends GrieferGamesController {

  @Inject
  public VersionedGrieferGamesController() {}

  @Override
  public boolean playerAllowedFlying() {
    return Minecraft.getInstance().player.getAbilities().mayfly;
  }

  @Override
  public boolean hideBoosterMenu() {
    if(Minecraft.getInstance().player == null) return false;

    Screen screen = Minecraft.getInstance().screen;
    if(screen instanceof ContainerScreen) {
      if(screen.getTitle().getString().equals("§6Booster - Übersicht")) {
        Minecraft.getInstance().player.closeContainer();
        return true;
      }
    }
    return false;
  }
}