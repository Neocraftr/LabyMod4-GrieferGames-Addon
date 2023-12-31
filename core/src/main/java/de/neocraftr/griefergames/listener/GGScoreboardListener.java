package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGSubServerChangeEvent;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamUpdateEvent;

public class GGScoreboardListener {
  private GrieferGames griefergames;

  public GGScoreboardListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onScoreboardTeams(ScoreboardTeamUpdateEvent event) {
    if(!griefergames.isOnGrieferGames()) return;

    if(event.team().getTeamName().equals("server_value")) {
      String subServerName = griefergames.helper().componentToPlainText(event.team().getPrefix()).toLowerCase();
      if(subServerName.isBlank() || subServerName.contains("lade")) return;

      griefergames.setSubServerType(SubServerType.REGULAR);
      if(!griefergames.getSubServer().equals(subServerName)) {
        griefergames.setSubServer(subServerName);
        GGSubServerChangeEvent changeEvent = new GGSubServerChangeEvent(subServerName);
        Laby.labyAPI().eventBus().fire(changeEvent);
      }
    }
  }
}
