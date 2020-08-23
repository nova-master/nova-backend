package net.getnova.backend.boot.logging.logback.converter.ansi;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnsiStyle implements AnsiElement {

  RESET("0"),
  BOLD("1"),
  FAINT("2"),
  ITALIC("3"),
  UNDERLINE("4");

  private final String code;

  @Override
  public String toString() {
    return this.code;
  }
}
