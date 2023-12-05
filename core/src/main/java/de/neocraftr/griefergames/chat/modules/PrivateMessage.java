package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.Sounds;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.I18n;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivateMessage extends ChatModule {
  private final GrieferGames griefergames;
  private final Pattern privateMessageRegex = Pattern.compile("\\[([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) -> mir\\] (.*)$");
  private final Pattern privateMessageRegexCloud = Pattern.compile("\\[([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) -> (mir|me)\\] (.*)$");
  private final Pattern privateMessageSentRegex = Pattern.compile("\\[mir -> ([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16})\\] (.*)$");
  private long lastAfkMessage = 0;

  public PrivateMessage(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if (event.isCancelled()) return;
    if (griefergames.getSubServerType() == SubServerType.REGULAR || griefergames.getSubServerType() == SubServerType.CLOUD) {
      Matcher privateMessage = privateMessageRegexCloud.matcher(event.getMessage().getPlainText());

      if (privateMessage.find()) {
        String playerName = privateMessage.group(2);

        if (griefergames.configuration().automations().afkMsgReply().get() && griefergames.isAfk() && lastAfkMessage + 1000 <= System.currentTimeMillis()) {
          String message = griefergames.configuration().automations().afkMsgText().get();
          if (!message.isBlank()) {
            griefergames.sendMessage("/msg " + playerName + " " + message);
            lastAfkMessage = System.currentTimeMillis();
          }
        }
      }
    }
    if (griefergames.getSubServerType() == SubServerType.REGULAR) {
      Matcher privateMessage = privateMessageRegex.matcher(event.getMessage().getPlainText());
      Matcher privateMessageSent = privateMessageSentRegex.matcher(event.getMessage().getPlainText());

      if (privateMessage.find()) {
        String playerName = privateMessage.group(2);

        if (griefergames.configuration().chatConfig().isClickToReply() && griefergames.isSubServerType(SubServerType.REGULAR)) {
          addReplyAction(event.getMessage().component(), "§6[", "§6 -> ", playerName);
        }

        if (griefergames.configuration().chatConfig().isPrivateChatRight()) {
          event.setSecondChat(true);
        }

        if (griefergames.configuration().chatConfig().getPrivateChatSound() != Sounds.NONE) {
          ResourceLocation resource = ResourceLocation.create("minecraft", griefergames.configuration().chatConfig().getPrivateChatSound().path());
          Laby.labyAPI().minecraft().sounds().playSound(resource, 1f, 1f);
        }
      }

      if (privateMessageSent.find()) {
        if (griefergames.configuration().chatConfig().isClickToReply() && griefergames.isSubServerType(SubServerType.REGULAR)) {
          addReplyAction(event.getMessage().component(), "§6 -> ", "§6] ", privateMessageSent.group(2));
        }

        if (griefergames.configuration().chatConfig().isPrivateChatRight()) {
          event.setSecondChat(true);
        }
      }
    }
  }

  public void addReplyAction(Component message, String startText, String endText, String name) {
    int startIndex = -1, endIndex = -1;
    for (int i = 0; i < message.getChildren().size(); i++) {
      Component component = message.getChildren().get(i);
      String formatted = this.getFormattedText(component);

      if (startIndex == -1 && formatted.equals(startText)) {
        startIndex = i + 1;
      }

      if (endIndex == -1 && formatted.equals(endText)) {
        endIndex = i - 1;
      }
    }

    for (int i = startIndex; i <= endIndex; i++) {
      Component component = message.getChildren().get(i);
      component.style(component.style()
        .hoverEvent(HoverEvent.showText(Component.text(I18n.translate(griefergames.namespace() + ".messages.clickToReply"), NamedTextColor.GREEN)))
        .clickEvent(ClickEvent.suggestCommand("/msg " + name + " ")));
    }
  }
}
