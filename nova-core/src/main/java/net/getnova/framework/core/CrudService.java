package net.getnova.framework.core;

import java.util.Set;

public interface CrudService<D, I> {

  Set<D> findAll();

  boolean exist(I id);

  D save(D dto);

  D save(I id, D dto);

  void delete(I id);
}