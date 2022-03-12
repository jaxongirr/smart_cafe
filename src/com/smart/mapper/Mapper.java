package com.smart.mapper;

public interface Mapper<F, T> {

    T map(F from);
}
