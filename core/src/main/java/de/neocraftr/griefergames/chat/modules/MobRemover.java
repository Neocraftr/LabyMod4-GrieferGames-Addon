package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.I18n;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobRemover extends ChatModule {
  private final GrieferGames griefergames;
  private final Pattern mobRemoverMessageRegex = Pattern.compile("^\\[MobRemover\\] Achtung! In ([0-9]+) Minuten? werden alle Tiere gelöscht\\.$");
  private final Pattern nmoboverDoneMessageRegex = Pattern.compile("^\\[MobRemover\\] Es wurden ([0-9]+) Tiere entfernt\\.$");
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  public MobRemover(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.isCancelled()) return;
    String plain = event.getMessage().getPlainText();
    if(plain.isBlank()) return;

    Matcher mobRemoverMessage = mobRemoverMessageRegex.matcher(plain);
    Matcher mobRemoverDoneMessage = nmoboverDoneMessageRegex.matcher(plain);

    boolean done;
    if((done = mobRemoverDoneMessage.find()) || mobRemoverMessage.find()) {
      if(griefergames.configuration().chatConfig().mobRemoverLastTimeHover().get() && done) {
        String dateNowStr = LocalDateTime.now().format(formatter);
        Component hoverText = Component.text(dateNowStr);
        event.getMessage().component().style(event.getMessage().component().style().hoverEvent(
            HoverEvent.showText(hoverText)));
      }

      if(griefergames.configuration().chatConfig().mobRemoverChatRight().get()) {
        event.setSecondChat(true);
      }

      if(griefergames.configuration().chatConfig().mobRemoverNotification().get() && !done) {
        Laby.labyAPI().notificationController().push(Notification.builder()
            .title(Component.text("MobRemover", NamedTextColor.RED))
            .text(Component.text(I18n.translate("griefergames.notifications.mobRemover").replace("{time}", mobRemoverMessage.group(1))))
            .icon(Icon.texture(ResourceLocation.create("griefergames", "textures/mobremover.png")))
            .build());
      }
    }
  }
}
