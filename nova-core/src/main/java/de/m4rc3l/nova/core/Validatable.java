package de.m4rc3l.nova.core;

import de.m4rc3l.nova.core.exception.ValidationException;

public interface Validatable {

  /**
   * Validates the contents of properties and also {@link ValidationUtils#notNull(String,
   * Object)}.
   *
   * @throws ValidationException if the validation fails
   */
  default void validate() throws ValidationException {
    this.validateProperties();
  }

  /**
   * Validates the contents of properties, but not {@link ValidationUtils#notNull(String,
   * Object)}.
   *
   * @throws ValidationException if the validation fails
   */
  default void validateProperties() throws ValidationException {
    // noop
  }
}
