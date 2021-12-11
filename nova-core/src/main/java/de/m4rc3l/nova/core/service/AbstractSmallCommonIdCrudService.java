package de.m4rc3l.nova.core.service;

import de.m4rc3l.nova.core.Converter;
import de.m4rc3l.nova.core.exception.NotFoundException;
import de.m4rc3l.nova.core.utils.ValidationUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractSmallCommonIdCrudService<D, S, I, M, SM>
  extends AbstractSmallCrudService<D, S, I, M, SM, I> {

  protected final String name;

  public AbstractSmallCommonIdCrudService(
    final String name,
    final CrudRepository<M, I> repository,
    final CrudRepository<SM, I> smallRepository,
    final Converter<M, D> converter,
    final Converter<SM, S> smallConverter
  ) {
    super(repository, smallRepository, converter, smallConverter);
    this.name = name;
  }

  @Override
  @Transactional(readOnly = true)
  public D findById(final I id) {
    return this.converter.toDto(
      this.repository.findById(id)
        .orElseThrow(() -> new NotFoundException(this.name))
    );
  }

  @Override
  @Transactional(readOnly = true)
  public boolean exist(final I id) {
    return this.repository.existsById(id);
  }

  @Override
  @Transactional
  public D save(final I id, final D dto) {
    ValidationUtils.validate(dto);

    final M model = this.repository.findById(id)
      .orElseThrow(() -> new NotFoundException(this.name));

    this.converter.override(model, dto);

    return this.converter.toDto(model);
  }

  @Override
  @Transactional
  public D merge(final I id, final D dto) {
    ValidationUtils.validateProperties(dto);

    final M model = this.repository.findById(id)
      .orElseThrow(() -> new NotFoundException(this.name));

    this.converter.merge(model, dto);

    return this.converter.toDto(model);
  }

  @Override
  @Transactional
  public void delete(final I id) {
    if (!this.repository.existsById(id)) {
      throw new NotFoundException(this.name);
    }

    this.repository.deleteById(id);
  }
}
