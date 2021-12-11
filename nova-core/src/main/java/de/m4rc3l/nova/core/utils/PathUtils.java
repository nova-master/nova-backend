package de.m4rc3l.nova.core.utils;

public final class PathUtils {

  private static final String EMPTY_PATH = "/";
  private static final char SLASH = '/';

  private PathUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Strips one or multiple path components from the beginning of a path.
   * <pre>
   * stripComponent("/hello", "/hello/world") -&gt; "/world"
   * stripComponent("/hello/world", "/hello/world/123") -&gt; "/123"
   * </pre>
   *
   * @param component the component to strip from the path (must start with a {@code /}, multiple components separated
   *                  with a {@code /})
   * @param path      the path, where the components should be stripped
   * @return the path without the specified components
   * @throws IllegalArgumentException if the components does not start with a {@code /}
   */
  public static String stripComponents(final String component, final String path) {
    if (component.isEmpty() || (component.length() == 1 && component.charAt(0) == SLASH)) {
      return normalizePath(path);
    }

    if (component.charAt(0) != SLASH) {
      throw new IllegalArgumentException("component should start with \"/\"");
    }

    final int length = path.charAt(0) == SLASH ? component.length() : component.length() - 1;

    return normalizePath(path.substring(length));
  }

  /**
   * Removes leading and trailing {@code /} from a path.
   * <pre>
   * normalizePath("/hello/") -&gt; "hello"
   * normalizePath("/hello/world") -&gt; "hello/world"
   * normalizePath("") -&gt; "/"
   * </pre>
   *
   * @param fullPath the path witch should be normalized
   * @return the normalized path
   */
  public static String normalizePath(final String fullPath) {
    if (fullPath.isEmpty()) {
      return EMPTY_PATH;
    }

    boolean startsWithSlash = fullPath.charAt(0) == SLASH;
    if (fullPath.length() == 1 && startsWithSlash) {
      return fullPath;
    }

    String path = fullPath;
    if (!startsWithSlash) {
      path = SLASH + path;
    }

    final int end = path.length() - 1;
    if (path.charAt(end) == SLASH) {
      path = path.substring(0, end);
    }

    return path;
  }
}
