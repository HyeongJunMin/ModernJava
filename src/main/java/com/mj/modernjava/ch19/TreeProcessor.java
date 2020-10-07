package com.mj.modernjava.ch19;

public class TreeProcessor {
  public static int lookup(String key, int defaultValue, Tree tree) {
    if (tree == null) return defaultValue;
    if (key.equals(tree.getKey())) return tree.getValue();
    return lookup(key, defaultValue, key.compareTo(tree.getKey()) < 0 ? tree.getLeft() : tree.getRight());
  }
}
