package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import net.labymod.api.event.Subscribe;

public class Blanks extends ChatModule {
  private final GrieferGames griefergames;

  public Blanks(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(griefergames.configuration().chatConfig().hideBlankLines().get() && event.getMessage().getPlainText().isBlank()) {
      event.setCancelled(true);
    }

    if(griefergames.configuration().chatConfig().hideSupremeBlankLines().get() && event.getMessage().getPlainText().trim().equals("\u00BB")) {
      event.setCancelled(true);
    }
  }
}
