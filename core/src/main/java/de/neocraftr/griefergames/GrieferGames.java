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
import de.neocraftr.griefergames.hud.DelayModule;
import de.neocraftr.griefergames.hud.IncomeHudWidget;
import de.neocraftr.griefergames.hud.NicknameHudWidget;
import de.neocraftr.griefergames.hud.RedstoneHudWidget;
import de.neocraftr.griefergames.listener.GGKeyListener;
import de.neocraftr.griefergames.listener.GGMessageReceiveListener;
import de.neocraftr.griefergames.listener.GGScoreboardListener;
import de.neocraftr.griefergames.listener.GGServerJoinListener;
import de.neocraftr.griefergames.listener.GGServerMessageListener;
import de.neocraftr.griefergames.listener.GGServerQuitListener;
import de.neocraftr.griefergames.listener.GGSubServerChangeListener;
import de.neocraftr.griefergames.listener.GGTickListener;
import de.neocraftr.griefergames.settings.GrieferGamesConfig;
import de.neocraftr.griefergames.utils.FileManager;
import de.neocraftr.griefergames.utils.Helper;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
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
  private FileManager fileManager;

  private boolean onGrieferGames = false;
  private IngameChatTab secondChat = null;
  private HudWidgetCategory hudWidgetCategory = null;
  private String nickname = null;
  private double income = 0;
  private boolean redstoneActive = false;
  private long waitTime = 0;
  private boolean citybuildDelay = false;
  private String subServer = "";
  private long lastActivety = 0;
  private boolean afk = false;

  @Override
  protected void enable() {
    griefergames = this;
    helper = new Helper(this);
    fileManager = new FileManager(this);

    registerSettingCategory();
    registerListener(new GGServerJoinListener(this));
    registerListener(new GGServerQuitListener(this));
    registerListener(new GGMessageReceiveListener(this));
    registerListener(new GGKeyListener(this));
    registerListener(new GGServerMessageListener(this));
    registerListener(new GGScoreboardListener(this));
    registerListener(new GGSubServerChangeListener(this));
    registerListener(new GGTickListener(this));

    // Chat modules
    registerListener(new Blanks(this));
    registerListener(new PrivateMessage(this));
    registerListener(new Payment(this));
    registerListener(new Bank(this));
    registerListener(new AntiMagicClanTag(this));
    registerListener(new AntiMagicPrefix(this));
    registerListener(new News(this));
    registerListener(new PlotChat(this));
    registerListener(new Vote(this));
    registerListener(new Realname(this));
    registerListener(new ItemRemover(this));
    registerListener(new MobRemover(this));
    registerListener(new BetterIgnoreList(this));
    registerListener(new Mention(this));
    registerListener(new Nickname(this));
    registerListener(new Teleport(this));
    registerListener(new ChatTime(this));

    // Hud widgets
    hudWidgetCategory = new HudWidgetCategory(this, "griefergames");
    labyAPI().hudWidgetRegistry().categoryRegistry().register(hudWidgetCategory);
    labyAPI().hudWidgetRegistry().register(new IncomeHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new NicknameHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new RedstoneHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new DelayModule(this));
    //labyAPI().hudWidgetRegistry().register(new FlyHudModule(this));

    if(labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      registerCommand(new GGMessageCommand(this));
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
    displayMessage(Component.empty().append(PREFIX).append(message));
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

  public FileManager fileManager() {
    return fileManager;
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

  public HudWidgetCategory getHudWidgetCategory() {
    return hudWidgetCategory;
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

  public boolean isRedstoneActive() {
    return redstoneActive;
  }
  public void setRedstoneActive(boolean redstoneActive) {
    this.redstoneActive = redstoneActive;
  }

  public long getWaitTime() {
    return waitTime;
  }
  public void setWaitTime(long waitTime) {
    this.waitTime = waitTime;
  }

  public boolean isCitybuildDelay() {
    return citybuildDelay;
  }
  public void setCitybuildDelay(boolean citybuildDelay) {
    this.citybuildDelay = citybuildDelay;
  }

  public String getSubServer() {
    return subServer;
  }
  public void setSubServer(String subServer) {
    this.subServer = subServer;
  }

  public long getLastActivety() {
    return lastActivety;
  }
  public void setLastActivety(long lastActivety) {
    this.lastActivety = lastActivety;
  }

  public boolean isAfk() {
    return afk;
  }
  public void setAfk(boolean afk) {
    this.afk = afk;
    helper.performAfkActions(afk);
  }
}
