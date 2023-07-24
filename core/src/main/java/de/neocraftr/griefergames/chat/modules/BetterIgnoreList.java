package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.I18n;
import java.util.ArrayList;
import java.util.List;

public class BetterIgnoreList extends ChatModule {
  private final GrieferGames griefergames;

  public BetterIgnoreList(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.isCancelled()) return;
    if(!griefergames.configuration().chatConfig().betterIgnoreList().get()) return;

    if(event.getMessage().getPlainText().startsWith("Ignoriert: ")) {
      List<Component> children = new ArrayList<>(event.getMessage().component().getChildren());
      if(children.size() == 2) {
        children.remove(1);

        String[] names = event.getMessage().getPlainText().replace("Ignoriert: ", "").split(" ");
        for(String name : names) {
          children.add(Component.empty().append(Component.text("\n- "+name, Style.builder()
              .clickEvent(ClickEvent.runCommand("/unignore "+name))
              .hoverEvent(HoverEvent.showText(Component.text(I18n.translate(griefergames.namespace()+".messages.hoverIgnoreListEntry"))))
              .build())));
        }

        event.getMessage().component().setChildren(children);
      }
    }
  }
}
