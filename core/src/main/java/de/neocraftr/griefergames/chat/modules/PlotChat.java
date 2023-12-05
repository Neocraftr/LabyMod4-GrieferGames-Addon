package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.event.Subscribe;

public class PlotChat extends ChatModule {
  private final GrieferGames griefergames;

  public PlotChat(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.isCancelled()) return;
    if (griefergames.getSubServerType() == SubServerType.REGULAR) {
      if (!griefergames.configuration().chatConfig().isPlotChatRight()) return;

      if (event.getMessage().getPlainText().startsWith("[Plot-Chat]")) {
        event.setSecondChat(true);
      }
    }
  }
}
