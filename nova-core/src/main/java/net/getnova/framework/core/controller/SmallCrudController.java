package net.getnova.framework.core.controller;

import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface SmallCrudController<D, S, I> {

  @GetMapping
  Set<S> findAll();

  @GetMapping("{id}")
  D get(@PathVariable I id);

  @PostMapping
  D post(@RequestBody D dto);

  @PutMapping("{id}")
  D put(@PathVariable I id, @RequestBody D dto);

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable I id);
}
