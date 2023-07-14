package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.settings.GrieferGamesConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatTime extends ChatModule {
  private final GrieferGames griefergames;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  public ChatTime(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.isCancelled()) return;
    if (!griefergames.configuration().chatConfig().showChatTime().get()) return;
    if (event.getMessage().getPlainText().isBlank()) return;

    String[] time = LocalDateTime.now().format(formatter).split(":");
    String timeMsg = griefergames.configuration().chatConfig().chatTimeFormat().get();
    if(timeMsg.isBlank()) {
      timeMsg = GrieferGamesConfig.DEFAULT_CHATTIME_FORMAT;
    }
    timeMsg = timeMsg.replace("&", "§");
    timeMsg = timeMsg.replace("{h}", time[0]);
    timeMsg = timeMsg.replace("{m}", time[1]);
    timeMsg = timeMsg.replace("{s}", time[2]);

    if(griefergames.configuration().chatConfig().chatTimeAfterMessage().get()) {
      event.getMessage().component().append(Component.text("§r "+timeMsg));
    } else {
      // TODO: Properly prepend time to existing message
      event.getMessage().edit(Component.empty().append(Component.text(timeMsg+" ")).append(event.getMessage().component()));
    }
  }
}
