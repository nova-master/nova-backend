package de.m4rc3l.nova.core.utils;

import de.m4rc3l.nova.core.Validatable;
import java.util.regex.Pattern;
import de.m4rc3l.nova.core.exception.ValidationException;

public final class ValidationUtils {

  private ValidationUtils() {
    throw new UnsupportedOperationException();
  }

  public static void validate(final Object object) throws ValidationException {
    if (object instanceof Validatable) {
      ((Validatable) object).validate();
    }
  }

  public static void validateProperties(final Object object) throws ValidationException {
    if (object instanceof Validatable) {
      ((Validatable) object).validateProperties();
    }
  }

  public static void validate(final Validatable validatable) throws ValidationException {
    validatable.validate();
  }

  public static void validateProperties(final Validatable validatable) throws ValidationException {
    validatable.validateProperties();
  }

  public static <T> void notNull(final String property, final T value) {
    if (value == null) {
      throw new ValidationException(property, "NOT_NULL");
    }
  }

  public static void notEmpty(final String property, final CharSequence value) {
    if (value != null && value.length() == 0) {
      throw new ValidationException(property, "NOT_EMPTY");
    }
  }

  public static void notBlank(final String property, final String value) {
    if (value != null && value.isBlank()) {
      throw new ValidationException(property, "NO_BLANK");
    }
  }

  public static void minLength(final String property, final CharSequence value, final int min) {
    if (value != null && value.length() < min) {
      throw new ValidationException(property, "MAX_LENGTH_" + min);
    }
  }

  public static void maxLength(final String property, final CharSequence value, final int max) {
    if (value != null && value.length() > max) {
      throw new ValidationException(property, "MAX_LENGTH_" + max);
    }
  }

  public static void pattern(final String property, final CharSequence value, final Pattern pattern) {
    if (value != null && pattern.matcher(value).matches()) {
      throw new ValidationException(property, "MATCH_PAtTERN");
    }
  }
}
