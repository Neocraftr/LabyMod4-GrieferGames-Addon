package de.neocraftr.griefergames.v1_8_9;

import de.neocraftr.griefergames.utils.GrieferGamesController;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implements(GrieferGamesController.class)
public class VersionedGrieferGamesController extends GrieferGamesController {
  @Inject
  public VersionedGrieferGamesController() {}

  @Override
  public boolean playerAllowedFlying() {
    return Minecraft.getMinecraft().thePlayer.capabilities.allowFlying;
  }
}
