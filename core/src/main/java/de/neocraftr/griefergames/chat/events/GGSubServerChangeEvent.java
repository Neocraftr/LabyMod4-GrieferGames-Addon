package de.neocraftr.griefergames.chat.events;

import net.labymod.api.event.Event;

public class GGSubServerChangeEvent implements Event {
  private String subServerName;

  public GGSubServerChangeEvent(String subServerName) {
    this.subServerName = subServerName;
  }

  public String subServerName() {
    return subServerName;
  }
}
