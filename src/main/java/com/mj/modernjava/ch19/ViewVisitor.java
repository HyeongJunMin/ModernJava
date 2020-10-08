package com.mj.modernjava.ch19;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ViewVisitor extends Visitor {
  private String path = "";
  public void visit(File file) {
    log.info("{}/{}", path, file.name);
  }
  public void visit(Directory dir) {
    path = path + "/" + dir.name;
    log.info("{}", path);
    for (int i = 0; i < dir.getDirectory().size(); i++) {
      dir.getDirectory().get(i).accept(this);
    }
  }
}
