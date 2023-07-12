package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.EnumRealnamePosition;
import net.labymod.api.event.Subscribe;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Realname extends ChatModule {
  private final GrieferGames griefergames;
  private final Pattern realnameRegex = Pattern.compile("^(?:\\[[^\\]]+\\])?[A-Za-z\\-]+\\+? \\u2503 \\u007E?\\!?\\w{1,16} ist (\\!?\\w{1,16})$");

  public Realname(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.isCancelled()) return;
    EnumRealnamePosition position = griefergames.configuration().chatConfig().realnamePosition().get();
    if(position == EnumRealnamePosition.DEFAULT) return;
    if(event.getMessage().getPlainText().isBlank()) return;

    Matcher matcher = realnameRegex.matcher(event.getMessage().getPlainText());
    if(matcher.find()) {
      if(position == EnumRealnamePosition.SECONDCHAT) {
        event.setSecondChat(true);
      } else if(position == EnumRealnamePosition.BOTH) {
        event.setSecondChat(true, true);
      }
    }
  }
}
