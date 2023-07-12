package de.neocraftr.griefergames;

import de.neocraftr.griefergames.chat.modules.AntiMagicClanTag;
import de.neocraftr.griefergames.chat.modules.AntiMagicPrefix;
import de.neocraftr.griefergames.chat.modules.Bank;
import de.neocraftr.griefergames.chat.modules.BetterIgnoreList;
import de.neocraftr.griefergames.chat.modules.Blanks;
import de.neocraftr.griefergames.chat.modules.ChatTime;
import de.neocraftr.griefergames.chat.modules.ItemRemover;
import de.neocraftr.griefergames.chat.modules.Mention;
import de.neocraftr.griefergames.chat.modules.MobRemover;
import de.neocraftr.griefergames.chat.modules.News;
import de.neocraftr.griefergames.chat.modules.Nickname;
import de.neocraftr.griefergames.chat.modules.Payment;
import de.neocraftr.griefergames.chat.modules.PlotChat;
import de.neocraftr.griefergames.chat.modules.PrivateMessage;
import de.neocraftr.griefergames.chat.modules.Realname;
import de.neocraftr.griefergames.chat.modules.Teleport;
import de.neocraftr.griefergames.chat.modules.Vote;
import de.neocraftr.griefergames.commands.GGMessageCommand;
import de.neocraftr.griefergames.listener.GGKeyListener;
import de.neocraftr.griefergames.listener.GGMessageReceiveListener;
import de.neocraftr.griefergames.listener.GGServerJoinListener;
import de.neocraftr.griefergames.listener.GGServerQuitListener;
import de.neocraftr.griefergames.settings.GrieferGamesConfig;
import de.neocraftr.griefergames.utils.Helper;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.options.ChatVisibility;
import net.labymod.api.configuration.labymod.chat.AdvancedChatMessage;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.core.configuration.labymod.chat.IngameChatTab;

@AddonMain
public class GrieferGames extends LabyAddon<GrieferGamesConfig> {

  public static final Component PREFIX = Component.empty()
      .append(Component.text("[", NamedTextColor.DARK_GRAY))
      .append(Component.text("GrieferGames-Addon", NamedTextColor.GOLD))
      .append(Component.text("] ", NamedTextColor.DARK_GRAY));

  private static GrieferGames griefergames;
  private Helper helper;

  private boolean onGrieferGames = false;
  private IngameChatTab secondChat = null;
  private String nickname = null;
  private double income = 0;

  @Override
  protected void enable() {
    griefergames = this;
    helper = new Helper(this);

    this.registerSettingCategory();
    this.registerListener(new GGServerJoinListener(this));
    this.registerListener(new GGServerQuitListener(this));
    this.registerListener(new GGMessageReceiveListener(this));
    this.registerListener(new GGKeyListener(this));

    // Chat modules
    this.registerListener(new Blanks(this));
    this.registerListener(new PrivateMessage(this));
    this.registerListener(new Payment(this));
    this.registerListener(new Bank(this));
    this.registerListener(new AntiMagicClanTag(this));
    this.registerListener(new AntiMagicPrefix(this));
    this.registerListener(new News(this));
    this.registerListener(new PlotChat(this));
    this.registerListener(new Vote(this));
    this.registerListener(new Realname(this));
    this.registerListener(new ItemRemover(this));
    this.registerListener(new MobRemover(this));
    this.registerListener(new BetterIgnoreList(this));
    this.registerListener(new Mention(this));
    this.registerListener(new Nickname(this));
    this.registerListener(new Teleport(this));
    this.registerListener(new ChatTime(this));

    if(this.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      this.registerCommand(new GGMessageCommand(this));
    }
  }

  public void sendToSecondChat(String msg) {
    AdvancedChatMessage chatMessage = AdvancedChatMessage.chat(ChatMessage.builder()
        .component(Component.text(msg))
        .visibility(ChatVisibility.SHOWN)
        .build());
    secondChat.handleInput(chatMessage);
  }

  public void displayAddonMessage(String message) {
    displayAddonMessage(Component.text(message));
  }
  public void displayAddonMessage(Component message) {
    this.displayMessage(Component.empty().append(PREFIX).append(message));
  }

  @Override
  protected Class configurationClass() {
    return GrieferGamesConfig.class;
  }

  public static GrieferGames get() {
    return griefergames;
  }

  public Helper helper() {
    return helper;
  }

  public boolean isOnGrieferGames() {
    return onGrieferGames;
  }
  public void setOnGrieferGames(boolean onGrieferGames) {
    this.onGrieferGames = onGrieferGames;
  }

  public IngameChatTab getSecondChat() {
    return secondChat;
  }
  public void setSecondChat(IngameChatTab secondChat) {
    this.secondChat = secondChat;
  }

  public String getNickname() {
    return nickname;
  }
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public double getIncome() {
    return income;
  }
  public void setIncome(double income) {
    this.income = income;
  }
  public void addIncome(double income) {
    this.income += income;
  }
}
