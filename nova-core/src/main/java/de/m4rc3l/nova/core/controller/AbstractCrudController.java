package de.m4rc3l.nova.core.controller;

import de.m4rc3l.nova.core.service.SmallCrudService;

public class AbstractCrudController<D, I> extends AbstractSmallCrudController<D, D, I> implements CrudController<D, I> {

  public AbstractCrudController(final SmallCrudService<D, D, I> service) {
    super(service);
  }
}
