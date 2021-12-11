package de.m4rc3l.nova.core.service;

import de.m4rc3l.nova.core.Converter;
import de.m4rc3l.nova.core.utils.ValidationUtils;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AbstractCrudService<D, I, M, P> implements CrudService<D, I> {

  protected final CrudRepository<M, P> repository;
  protected final Converter<M, D> converter;

  @Override
  @Transactional(readOnly = true)
  public Set<D> findAll() {
    return StreamSupport.stream(this.repository.findAll().spliterator(), false)
      .map(this.converter::toDto)
      .collect(Collectors.toSet());
  }

  @Override
  @Transactional(readOnly = true)
  public abstract D findById(I id);

  @Override
  @Transactional(readOnly = true)
  public abstract boolean exist(I id);

  @Override
  @Transactional
  public D save(final D dto) {
    ValidationUtils.validate(dto);

    return this.converter.toDto(
      this.repository.save(
        this.converter.toModel(dto)
      )
    );
  }

  @Override
  public abstract D save(I id, D dto);

  @Override
  public abstract void delete(I id);
}
