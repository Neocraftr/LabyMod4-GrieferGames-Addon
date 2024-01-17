package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.event.Subscribe;

public class Blanks extends ChatModule {
  private final GrieferGames griefergames;

  public Blanks(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if (!griefergames.configuration().chatConfig().isHideBlankLines()) return;
    if (event.getMessage().getPlainText().isBlank()) {
      event.setCancelled(true);
    }
    if (event.getMessage().getPlainText().trim().equals("\u00BB")) {
      event.setCancelled(true);
    }
    if (event.getMessage().getFormattedText().equals("&7")) {
      event.setCancelled(true);
    }
  }
}