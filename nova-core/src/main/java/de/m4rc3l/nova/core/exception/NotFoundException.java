package de.m4rc3l.nova.core.exception;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class NotFoundException extends HttpException {

  private static final String TYPE = "COMPONENT_NOT_FOUND";
  private static final String MESSAGE_FORMAT = "Component \"%s\" could not be found.";

  private final String component;

  public NotFoundException(final String component, final Throwable cause) {
    super(cause);
    this.component = component;
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public Map<String, String> getAdditionalProperties() {
    return Map.of("component", this.component);
  }

  @Override
  public String getMessage() {
    return String.format(MESSAGE_FORMAT, this.component);
  }
}
