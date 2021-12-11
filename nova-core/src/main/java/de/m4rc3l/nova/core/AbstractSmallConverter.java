package de.m4rc3l.nova.core;

public abstract class AbstractSmallConverter<M, D> implements Converter<M, D> {

  @Override
  public M toModel(final D dto) {
    throw new UnsupportedOperationException();
  }

  @Override
  public abstract D toDto(M model);

  @Override
  public void override(final M model, final D dto) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void merge(final M model, final D dto) {
    throw new UnsupportedOperationException();
  }
}
