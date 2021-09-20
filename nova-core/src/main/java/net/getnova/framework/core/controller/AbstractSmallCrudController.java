package net.getnova.framework.core.controller;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.core.service.SmallCrudService;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AbstractSmallCrudController<D, S, I> implements SmallCrudController<D, S, I> {

  protected final SmallCrudService<D, S, I> service;

  @Override
  @Transactional(readOnly = true)
  public Set<S> findAll() {
    return this.service.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public D get(final I id) {
    return this.service.findById(id);
  }

  @Override
  @Transactional
  public D post(final D dto) {
    return this.service.save(dto);
  }

  @Override
  @Transactional
  public D put(final I id, final D dto) {
    return this.service.save(id, dto);
  }

  @Override
  @Transactional
  public D patch(final I id, final D dto) {
    return this.service.merge(id, dto);
  }

  @Override
  @Transactional
  public void delete(final I id) {
    this.service.delete(id);
  }
}
