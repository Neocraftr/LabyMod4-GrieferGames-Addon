package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;

public class GGTickListener {
  private GrieferGames griefergames;
  private long nextNameColorize = System.currentTimeMillis() + 20000L;

  public GGTickListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onTick(GameTickEvent event) {
    if(!griefergames.isOnGrieferGames()) return;
    if(event.phase() == Phase.POST) {
      if (griefergames.getSubServerType() == SubServerType.REGULAR || griefergames.getSubServerType() == SubServerType.CLOUD) {
        long now = System.currentTimeMillis();
        if(!griefergames.isAfk() && griefergames.getLastActivety() + (griefergames.configuration().automations().afkConfig().afkTime().get() * 60000) < System.currentTimeMillis()
          && griefergames.configuration().automations().afkConfig().isEnabled()) {
          griefergames.setAfk(true);
          griefergames.helper().performAfkActions(true);
        }
      }
      if(griefergames.getSubServerType() == SubServerType.REGULAR) {
        if(griefergames.configuration().automations().boosterConfig().isEnabled()) {
          if(griefergames.configuration().automations().boosterConfig().isHideBoosterMenu() || griefergames.isHideBoosterMenu()) {
            if(griefergames.controller().hideBoosterMenu()) {
              griefergames.setHideBoosterMenu(false);
            }
          }
        }
      }
    }
  }
}
