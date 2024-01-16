package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.I18n;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated(since = "1.1.1", forRemoval = true)
public class GlobalMessage extends ChatModule {
  // Not in use, already implemented by GrieferGames

  private final GrieferGames griefergames;
  private final Pattern globalChatRegex = Pattern.compile("([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) [\\u00BB:]");

  public GlobalMessage(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.isCancelled()) return;
    if(!griefergames.isSubServerType(SubServerType.REGULAR)) return;
    if (!griefergames.configuration().chatConfig().isHideVoteMessages()) return;

    Component message = null;
    String playername = null;

    for (Component component : event.getMessage().component().getChildren()) {
      Matcher globalChatMatcher = globalChatRegex.matcher(this.getPlainText(component));
      if (globalChatMatcher.find()) {
        message = component;
        playername = globalChatMatcher.group(2);
        break;
      }
    }

    if (message == null) return;
    if (playername.startsWith("~")) playername = playername.replaceFirst("~", "");

    if (!playername.equals(Laby.labyAPI().getName())) {
      String command = "/msg " + playername + " ";
      Component hoverText = Component.text(I18n.translate(griefergames.namespace() + ".messages.globalMessageHoverText"), NamedTextColor.GREEN);

      for (Component component : message.getChildren()) {
        String plain = this.getPlainText(component);
        if (plain.equals("» ") || plain.equals(": ")) break;
        component.style().hoverEvent(HoverEvent.showText(hoverText)).clickEvent(ClickEvent.suggestCommand(command));
      }
    }
  }
}
