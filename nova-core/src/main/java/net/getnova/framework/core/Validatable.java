package net.getnova.framework.core;

import net.getnova.framework.core.exception.ValidationException;

public interface Validatable {

  /**
   * Validates the contents of properties and also {@link net.getnova.framework.core.utils.ValidationUtils#notNull(String,
   * Object)}.
   *
   * @throws ValidationException if the validation fails
   */
  default void validate() throws ValidationException {
    this.validateProperties();
  }

  /**
   * Validates the contents of properties, but not {@link net.getnova.framework.core.utils.ValidationUtils#notNull(String,
   * Object)}.
   *
   * @throws ValidationException if the validation fails
   */
  default void validateProperties() throws ValidationException {
    // noop
  }
}
