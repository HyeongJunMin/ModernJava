package com.mj.modernjava.ch15;

public interface Processor<T,R> extends Subscriber<T>, Publisher<R> {
}
