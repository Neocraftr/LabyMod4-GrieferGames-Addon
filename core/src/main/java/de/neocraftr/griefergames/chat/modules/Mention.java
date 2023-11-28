package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.Sounds;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mention extends ChatModule {
  private final GrieferGames griefergames;
  private final Pattern globalChatregex = Pattern.compile("([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) [\\u00BB:] (.+)");

  public Mention(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.isCancelled()) return;
    if (griefergames.getSubServerType() == SubServerType.REGULAR) {
      if (!griefergames.configuration().chatConfig().highlightMentions().get() && griefergames.configuration().chatConfig().mentionSound().get() == Sounds.NONE)
        return;
      if (event.getMessage().getPlainText().isBlank()) return;

      Matcher matcher = globalChatregex.matcher(event.getMessage().getPlainText());
      if (matcher.find() && !matcher.group(2).equalsIgnoreCase(Laby.labyAPI().getName())) {
        if (matcher.group(3).toLowerCase().contains(Laby.labyAPI().getName().toLowerCase())) {
          if (griefergames.configuration().chatConfig().highlightMentions().get()) {
            event.getMessage().metadata().set("gg_custom_background", griefergames.configuration().chatConfig().mentionColor().get().get());
          }

          if (griefergames.configuration().chatConfig().mentionSound().get() != Sounds.NONE) {
            ResourceLocation resource = ResourceLocation.create("minecraft", griefergames.configuration().chatConfig().mentionSound().get().path());
            Laby.labyAPI().minecraft().sounds().playSound(resource, 1f, 1f);
          }
        }
      }
    }
  }
}
