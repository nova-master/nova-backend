package net.getnova.backend.api.parser;

import net.getnova.backend.api.annotations.ApiEndpoint;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiParameterData;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

final class ApiEndpointParser {

    private ApiEndpointParser() {
        throw new UnsupportedOperationException();
    }

    static Map<String, ApiEndpointData> parseEndpoints(final Object object, final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .map(method -> parseEndpoint(object, clazz, method))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableMap(ApiEndpointData::getName, Function.identity()));
    }

    private static ApiEndpointData parseEndpoint(final Object instance, final Class<?> clazz, final Method method) {
        final boolean hasAccess = method.canAccess(instance);
        if (!hasAccess) method.setAccessible(true);

        if (!method.isAnnotationPresent(ApiEndpoint.class)) {
            if (!hasAccess) method.setAccessible(false);
            return null;
        }

        final ApiEndpoint endpointAnnotation = method.getAnnotation(ApiEndpoint.class);
        final Set<ApiParameterData> parameters = ApiParameterParser.parseParameters(clazz, method);

        return new ApiEndpointData(endpointAnnotation.name(),
                String.join("\n", endpointAnnotation.description()),
                parameters == null ? Collections.emptySet() : parameters,
                parameters != null,
                instance, clazz, method);
    }
}