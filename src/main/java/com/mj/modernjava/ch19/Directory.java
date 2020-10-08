package com.mj.modernjava.ch19;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public class Directory extends Entry {
  private ArrayList<Entry> directory = new ArrayList();
  public Directory(String name) {
    super(name);
  }
  public void add(Entry entry) {
    directory.add(entry);
  }
  public void accept(Visitor v) {
    v.visit(this);
  }
}