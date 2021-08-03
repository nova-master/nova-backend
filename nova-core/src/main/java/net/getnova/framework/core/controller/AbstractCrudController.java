package net.getnova.framework.core.controller;

import net.getnova.framework.core.service.SmallCrudService;

public class AbstractCrudController<D, I> extends AbstractSmallCrudController<D, D, I> implements CrudController<D, I> {

  public AbstractCrudController(final SmallCrudService<D, D, I> service) {
    super(service);
  }
}
