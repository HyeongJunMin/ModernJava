package com.mj.modernjava.ch19;

public abstract class Entry implements Element{
  String name;
  public Entry(String name) {
    this.name = name;
  }
  public abstract void add(Entry entry);
}
