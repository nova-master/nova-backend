package net.getnova.framework.core.service;

import net.getnova.framework.core.Converter;
import net.getnova.framework.core.exception.NotFoundException;
import net.getnova.framework.core.utils.ValidationUtils;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractCommonIdCrudService<D, I, M> extends AbstractCrudService<D, I, M, I> {

  protected final String name;

  public AbstractCommonIdCrudService(
    final String name,
    final CrudRepository<M, I> repository,
    final Converter<M, D> converter
  ) {
    super(repository, converter);
    this.name = name;
  }

  @Override
  public D findById(final I id) {
    return this.converter.toDto(
      this.repository.findById(id)
        .orElseThrow(() -> new NotFoundException(this.name))
    );
  }

  @Override
  public boolean exist(final I id) {
    return this.repository.existsById(id);
  }

  @Override
  public D save(final I id, final D dto) {
    ValidationUtils.validate(dto);

    final M model = this.repository.findById(id)
      .orElseThrow(() -> new NotFoundException(this.name));

    this.converter.override(model, dto);

    return this.converter.toDto(model);
  }

  @Override
  public D merge(I id, D dto) {
ValidationUtils.validateProperties(dto);

    final M model = this.repository.findById(id)
      .orElseThrow(() -> new NotFoundException(this.name));

    this.converter.merge(model, dto);

    return this.converter.toDto(model);
  }

  @Override
  public void delete(final I id) {
    if (!this.repository.existsById(id)) {
      throw new NotFoundException(this.name);
    }

    this.repository.deleteById(id);
  }
}
