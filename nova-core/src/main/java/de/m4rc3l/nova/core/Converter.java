package de.m4rc3l.nova.core;

public interface Converter<M, D> {

  M toModel(D dto);

  D toDto(M model);

  void override(M model, D dto);

  void merge(M model, D dto);
}
