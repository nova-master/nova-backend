package de.m4rc3l.nova.core.service;

import de.m4rc3l.nova.core.Converter;
import de.m4rc3l.nova.core.exception.NotFoundException;
import de.m4rc3l.nova.core.utils.ValidationUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractSmallIdCrudService<D, S, I, M, SM, P>
  extends AbstractSmallCrudService<D, S, I, M, SM, P> {

  protected final String name;
  protected final Converter<P, I> idConverter;

  public AbstractSmallIdCrudService(
    final String name,
    final CrudRepository<M, P> repository,
    final CrudRepository<SM, P> smallRepository,
    final Converter<M, D> converter,
    final Converter<SM, S> smallConverter,
    final Converter<P, I> idConverter
  ) {
    super(repository, smallRepository, converter, smallConverter);
    this.name = name;
    this.idConverter = idConverter;
  }

  @Override
  @Transactional(readOnly = true)
  public D findById(final I id) {
    final P pId = this.idConverter.toModel(id);

    return this.converter.toDto(
      this.repository.findById(pId)
        .orElseThrow(() -> new NotFoundException(this.name))
    );
  }

  @Override
  @Transactional(readOnly = true)
  public boolean exist(final I id) {
    final P pId = this.idConverter.toModel(id);

    return this.repository.existsById(pId);
  }

  @Override
  @Transactional
  public D save(final I id, final D dto) {
    ValidationUtils.validate(dto);

    final P pId = this.idConverter.toModel(id);

    final M model = this.repository.findById(pId)
      .orElseThrow(() -> new NotFoundException(this.name));

    this.converter.override(model, dto);

    return this.converter.toDto(model);
  }

  @Override
  @Transactional
  public D merge(final I id, final D dto) {
    ValidationUtils.validateProperties(dto);

    final P pId = this.idConverter.toModel(id);

    final M model = this.repository.findById(pId)
      .orElseThrow(() -> new NotFoundException(this.name));

    this.converter.merge(model, dto);

    return this.converter.toDto(model);
  }

  @Override
  @Transactional
  public void delete(final I id) {
    final P pId = this.idConverter.toModel(id);

    if (!this.repository.existsById(pId)) {
      throw new NotFoundException(this.name);
    }

    this.repository.deleteById(pId);
  }
}
