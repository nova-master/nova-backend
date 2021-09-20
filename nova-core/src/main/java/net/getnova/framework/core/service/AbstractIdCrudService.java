package net.getnova.framework.core.service;

import net.getnova.framework.core.Converter;
import net.getnova.framework.core.exception.NotFoundException;
import net.getnova.framework.core.utils.ValidationUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractIdCrudService<D, I, M, P> extends AbstractCrudService<D, I, M, P> {

  protected final String name;
  protected final Converter<P, I> idConverter;

  public AbstractIdCrudService(
    final String name,
    final CrudRepository<M, P> repository,
    final Converter<M, D> converter,
    final Converter<P, I> idConverter
  ) {
    super(repository, converter);
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
  public void delete(final I id) {
    final P pId = this.idConverter.toModel(id);

    if (!this.repository.existsById(pId)) {
      throw new NotFoundException(this.name);
    }

    this.repository.deleteById(pId);
  }
}
