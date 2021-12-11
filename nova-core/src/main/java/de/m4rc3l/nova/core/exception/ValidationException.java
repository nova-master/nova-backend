package de.m4rc3l.nova.core.exception;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ValidationException extends HttpException {

  private static final String TYPE = "BAD_INPUT";
  private static final String MESSAGE_FORMAT = "Expected property \"%s\" to be in state \"%s\".";

  private final String property;
  private final String expected;

  public ValidationException(final String property, final String expected, final Throwable cause) {
    super(cause);
    this.property = property;
    this.expected = expected;
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public Map<String, String> getAdditionalProperties() {
    return Map.of(
      "property", this.property,
      "expected", this.expected
    );
  }

  @Override
  public String getMessage() {
    return String.format(MESSAGE_FORMAT, this.property, this.expected);
  }
}
