package com.mj.modernjava.ch19;

import java.util.function.Function;
import java.util.function.Supplier;

public class PatternMatching {

  public static void main(String[] args) {
    simplify();

    Expr e = new BinOp("+", new Number(5), new BinOp("*", new Number(3), new Number(4)));
    Integer result = evaluate(e);
    System.out.println(e + " = " + result);
  }

  public static void simplify() {
    TriFunction<String, Expr, Expr, Expr> binopcase = (opname, left, right) -> {
      if ("+".equals(opname)) {
        if (left instanceof Number && ((Number) left).val == 0) {
          return right;
        }
        if (right instanceof Number && ((Number) right).val == 0) {
          return left;
        }
      }
      if ("*".equals(opname)) {
        if (left instanceof Number && ((Number) left).val == 1) {
          return right;
        }
        if (right instanceof Number && ((Number) right).val == 1) {
          return left;
        }
      }
      return new BinOp(opname, left, right);
    };
    Function<Integer, Expr> numcase = val -> new Number(val);
    Supplier<Expr> defaultcase = () -> new Number(0);

    Expr e = new BinOp("+", new Number(5), new Number(0));
    Expr match = patternMatchExpr(e, binopcase, numcase, defaultcase);
    if (match instanceof Number) {
      System.out.println("Number: " + match);
    }
    else if (match instanceof BinOp) {
      System.out.println("BinOp: " + match);
    }
  }

  public static Expr simplify(Expr e) {
    TriFunction<String, Expr, Expr, Expr> binOperCase =
        (operator, left, right) -> {
          if ("+".equals(operator)) {
            if (left instanceof Number && ((Number)left).val == 0) {
              return right;
            }
            if (right instanceof Number && ((Number)right).val == 0) {
              return right;
            }
          }
          if ("*".equals(operator)) {
            if (left instanceof Number && ((Number)left).val == 0) {
              return right;
            }
            if (right instanceof Number && ((Number)right).val == 0) {
              return right;
            }
          }
          return new BinOp(operator, left, right);
        };
    Function<Integer, Expr> numCase = val -> new Number(val); // 숫자 처리
    Supplier defaultCase = () -> new Number(0); // 수식을 인식할 수 없을 때 기본처리
    return patternMatchExpr(e, binOperCase, numCase, defaultCase);
  }

  private static Integer evaluate(Expr e) {
    Function<Integer, Integer> numcase = val -> val;
    Supplier<Integer> defaultcase = () -> 0;
    TriFunction<String, Expr, Expr, Integer> binopcase = (opname, left, right) -> {
      if ("+".equals(opname)) {
        if (left instanceof Number && right instanceof Number) {
          return ((Number) left).val + ((Number) right).val;
        }
        if (right instanceof Number && left instanceof BinOp) {
          return ((Number) right).val + evaluate(left);
        }
        if (left instanceof Number && right instanceof BinOp) {
          return ((Number) left).val + evaluate(right);
        }
        if (left instanceof BinOp && right instanceof BinOp) {
          return evaluate(left) + evaluate(right);
        }
      }
      if ("*".equals(opname)) {
        if (left instanceof Number && right instanceof Number) {
          return ((Number) left).val * ((Number) right).val;
        }
        if (right instanceof Number && left instanceof BinOp) {
          return ((Number) right).val * evaluate(left);
        }
        if (left instanceof Number && right instanceof BinOp) {
          return ((Number) left).val * evaluate(right);
        }
        if (left instanceof BinOp && right instanceof BinOp) {
          return evaluate(left) * evaluate(right);
        }
      }
      return defaultcase.get();
    };

    return patternMatchExpr(e, binopcase, numcase, defaultcase);
  }

  static class Expr {}

  static class Number extends Expr {

    int val;

    public Number(int val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return "" + val;
    }

  }

  static class BinOp extends Expr {

    String opname;
    Expr left, right;

    public BinOp(String opname, Expr left, Expr right) {
      this.opname = opname;
      this.left = left;
      this.right = right;
    }

    @Override
    public String toString() {
      return "(" + left + " " + opname + " " + right + ")";
    }

  }
  public static <T> T myIf(boolean b, Supplier<T> truecase, Supplier<T> falsecase) {
    return b ? truecase.get() : falsecase.get();
  }

  static interface TriFunction<S, T, U, R> {
    R apply(S s, T t, U u);
  }

  static <T> T patternMatchExpr(Expr e, TriFunction<String, Expr, Expr, T> binopcase, Function<Integer, T> numcase, Supplier<T> defaultcase) {
    if (e instanceof BinOp) {
      return binopcase.apply(((BinOp) e).opname, ((BinOp) e).left, ((BinOp) e).right);
    }
    else if (e instanceof Number) {
      return numcase.apply(((Number) e).val);
    }
    else {
      return defaultcase.get();
    }
  }

}
