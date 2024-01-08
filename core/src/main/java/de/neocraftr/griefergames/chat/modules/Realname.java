package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.RealnamePosition;
import de.neocraftr.griefergames.enums.SubServerType;
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
    if(!griefergames.isSubServerType(SubServerType.REGULAR)) return;
    RealnamePosition position = griefergames.configuration().chatConfig().getRealnamePosition();
    if (position == RealnamePosition.DEFAULT) return;
    if (event.getMessage().getPlainText().isBlank()) return;

    Matcher matcher = realnameRegex.matcher(event.getMessage().getPlainText());
    if (matcher.find()) {
      if (position == RealnamePosition.SECONDCHAT) {
        event.setSecondChat(true);
      } else if (position == RealnamePosition.BOTH) {
        event.setSecondChat(true, true);
      }
    }
  }
}
