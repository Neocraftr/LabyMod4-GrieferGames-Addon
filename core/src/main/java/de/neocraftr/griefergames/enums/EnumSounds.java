package de.neocraftr.griefergames.enums;

public enum EnumSounds {
  NONE(""),
  BASS("note.bass"),
  BASSDRUM("note.bd"),
  HARP("note.harp"),
  HAT("note.hat"),
  PLING("note.pling"),
  SNARE("note.snare"),
  POP("random.pop");

  private String path;

  EnumSounds(String path) {
    this.path = path;
  }

  public String path() {
    return path;
  }
}
