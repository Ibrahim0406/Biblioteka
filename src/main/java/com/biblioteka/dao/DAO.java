package com.biblioteka.dao;

import java.util.List;

public interface DAO<T> {
    void create(T entity) throws Exception;
    T read(int id) throws Exception;
    List<T> readAll() throws Exception;
    void update(T entity) throws Exception;
    void delete(int id) throws Exception;
}