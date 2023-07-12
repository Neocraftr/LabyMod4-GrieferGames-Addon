package de.neocraftr.griefergames.chat.modules;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.I18n;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Payment extends ChatModule {
  private final GrieferGames griefergames;
  private final Pattern receiveMoneyRegex = Pattern.compile("^([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) hat dir \\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) gegeben\\.$");
  private final Pattern payMoneyRegex = Pattern.compile("^Du hast ([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) \\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) gegeben\\.$");
  private final Pattern earnMoneyRegex = Pattern.compile("\\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) wurde zu deinem Konto hinzugefügt\\.$");
  private final DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);

  public Payment(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void messageProcessEvent(GGChatProcessEvent event) {
    if(event.getMessage().getPlainText().isBlank()) return;

    Matcher receiveMoneyMatcher = receiveMoneyRegex.matcher(event.getMessage().getPlainText());
    if (receiveMoneyMatcher.find()) {
      String rank = receiveMoneyMatcher.group(1);
      String name = receiveMoneyMatcher.group(2);
      double amount = getAmount(receiveMoneyMatcher.group(3));

      if(!event.getMessage().getFormattedText().contains("§f §ahat dir $")) {
        griefergames.addIncome(amount);
        // TODO: Log transaction

        if(griefergames.configuration().payment().payChatRight().get()) {
          event.setSecondChat(true);
        }

        if(griefergames.configuration().payment().payAchievement().get()) {
          sendPaymentNotification("receive", rank, name, amount);
        }

        if(griefergames.configuration().payment().payHighlight().get()) {
          event.getMessage().component().append(Component.text(" \u2714", Style.builder()
              .color(NamedTextColor.GREEN)
              .hoverEvent(HoverEvent.showText(Component.text(I18n.translate("griefergames.messages.verifiedPayment"), NamedTextColor.GREEN)))
              .build()));
        }
      } else {
        if(griefergames.configuration().payment().fakeMoneyWarning().get()) {
          String warningMessage = "§e§l"+I18n.translate("griefergames.messages.warning")+" §c"
              +I18n.translate("griefergames.messages.fakeMoney")
              .replace("{player}", "§e"+rank+" ┃ "+name+"§c")
              .replace("{amount}", "§e$"+receiveMoneyMatcher.group(3)+"§c");

          griefergames.displayMessage("\n\n");
          griefergames.displayAddonMessage(warningMessage);
        }
      }
    }

    Matcher payMoneyMatcher = payMoneyRegex.matcher(event.getMessage().getPlainText());
    if(payMoneyMatcher.find()) {
      String rank = payMoneyMatcher.group(1);
      String name = payMoneyMatcher.group(2);
      double amount = getAmount(payMoneyMatcher.group(3));

      sendPaymentNotification("pay", rank, name, amount);
      griefergames.addIncome(amount * -1);
    }

    Matcher earnMoneyMatcher = earnMoneyRegex.matcher(event.getMessage().getPlainText());
    if(earnMoneyMatcher.find()) {
      double amount = getAmount(earnMoneyMatcher.group(1));

      sendPaymentNotification("earn", amount);
      griefergames.addIncome(amount);
    }
  }

  public double getAmount(String message) {
    message = message.replaceAll(",", "");
    try {
      return Double.parseDouble(message);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public void sendPaymentNotification(String event, String rank, String name, double amount) {
    String message = I18n.translate("griefergames.notifications.payment."+event);
    message = message.replace("{amount}", "$"+moneyFormat.format(amount)).replace("{player}", rank+" | "+name);

    Laby.labyAPI().notificationController().push(Notification.builder()
        .title(Component.text(I18n.translate("griefergames.notifications.payment.title"), NamedTextColor.GREEN))
        .text(Component.text(message))
        .icon(Icon.head(name)).build());
  }

  public void sendPaymentNotification(String event, double amount) {
    String message = I18n.translate("griefergames.notifications.payment."+event);
    message = message.replace("{amount}", "$"+moneyFormat.format(amount));

    Laby.labyAPI().notificationController().push(Notification.builder()
        .title(Component.text(I18n.translate("griefergames.notifications.payment.title"), NamedTextColor.GREEN))
        .text(Component.text(message)).build());
  }
}
