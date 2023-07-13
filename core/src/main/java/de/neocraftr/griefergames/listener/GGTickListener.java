package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
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
    if(event.phase() == Phase.POST) {
      long currentTime = System.currentTimeMillis();

      /*if(currentTime > nextNameColorize) {
        nextNameColorize = currentTime + 20000L;
        griefergames.helper().colorizePlayerNames();
      }*/

      if(!griefergames.isAfk() && griefergames.getLastActivety() + (griefergames.configuration().automations().afkTime().get() * 1000) < System.currentTimeMillis()) {
        griefergames.setAfk(true);
      }
    }
  }
}
