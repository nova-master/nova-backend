package net.getnova.framework.core.service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.core.Converter;
import net.getnova.framework.core.utils.ValidationUtils;
import org.springframework.data.repository.CrudRepository;

@RequiredArgsConstructor
public abstract class AbstractSmallCrudService<D, S, I, M, SM, P> implements SmallCrudService<D, S, I> {

  protected final CrudRepository<M, P> repository;
  protected final CrudRepository<SM, P> smallRepository;
  protected final Converter<M, D> converter;
  protected final Converter<SM, S> smallConverter;

  @Override
  public Set<S> findAll() {
    return StreamSupport.stream(this.smallRepository.findAll().spliterator(), false)
      .map(this.smallConverter::toDto)
      .collect(Collectors.toSet());
  }

  @Override
  public abstract D findById(I id);

  @Override
  public abstract boolean exist(I id);

  @Override
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
  public abstract D merge(I id, D dto);

  @Override
  public abstract void delete(I id);
}
