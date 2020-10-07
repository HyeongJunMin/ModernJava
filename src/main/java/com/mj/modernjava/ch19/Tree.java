package com.mj.modernjava.ch19;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class Tree {
  private String key;
  private int value;
  private Tree left, right;
//  public static void update(String key, int newValue, Tree tree) {
//    if (tree == null) {
//      // add new node
//    } else if (key.equals(tree.getKey())) {
//      tree.setValue(newValue);
//    } else {
//      update(key, newValue, key.compareTo(tree.getKey()) < 0 ? tree.getLeft() : tree.getRight());
//    }
//  }
  public static Tree update(String key, int newValue, Tree tree) {
    if (tree == null) {
      tree = new Tree(key, newValue, null, null);
    } else if (key.equals(tree.getKey())) {
      tree.setValue(newValue);
    } else if (key.compareTo(tree.getKey()) < 0) {
      tree.setLeft(update(key, newValue, tree));
    } else {
      tree.setRight(update(key, newValue, tree));
    }
    return tree;
  }
}
