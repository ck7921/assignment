package assignment.utils;

import assignment.utils.csv.Product;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Mapping maps to DTO's
 * Dont use this in production! Normally a mapping framework e.g. Jackson
 * would be used. The Spring Framework also comes out of the box with such
 * features if really required. In this application it's just a small utility
 * to more easily de-serialize objects from CSV files.
 *
 * @param <TYPE> target type
 */
public final class SimpleValueMapper<TYPE> {

    private Supplier<TYPE> dtoFactory;

    public SimpleValueMapper(final Supplier<TYPE> factory) {
        if(factory == null) { // HINT: project lombok can do that
            throw new IllegalArgumentException("dto factory must not be null");
        }
        this.dtoFactory = factory;
    }

    public TYPE map(final Map<String, String> valueMap) {
        // HINT: spring framework can do that (and even better)
        final TYPE dto = dtoFactory.get();
        final Class<?> aClass = dto.getClass();
        final Method[] methods = aClass.getDeclaredMethods();
        for (final Method method : methods) {
            if (!method.getName().startsWith("set")) continue;
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length < 1) continue;
            final String parameterTypeName = parameterTypes[0].getName();
            final String propertyName = parsePropertyName(method.getName());
            if (!valueMap.containsKey(propertyName)) continue;
            final String propertyRawValue = valueMap.get(propertyName);
            try {
                switch (parameterTypeName) {
                    case "java.lang.String" -> method.invoke(dto, asString(propertyRawValue));
                    case "java.lang.Long", "long" -> method.invoke(dto, asLong(propertyRawValue));
                    case "java.lang.Integer", "int" -> method.invoke(dto, asInteger(propertyRawValue));
                    case "java.math.BigDecimal" -> method.invoke(dto, asBigDecimal(propertyRawValue));
                    default -> System.out.println("type not supported: " + parameterTypeName);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("failure to map values to dto", e);
            }
        }
        return dto;
    }

    private String asString(final String s) {
        return s;
    }

    private BigDecimal asBigDecimal(final String s) {
        return s != null && s.length() > 0 ? new BigDecimal(s.trim()) : BigDecimal.ZERO;
    }

    private Long asLong(final String s) {
        return s != null ? Long.parseLong(s.trim()) : 0;
    }

    private Integer asInteger(final String s) {
        return s != null ? Integer.parseInt(s.trim(), 10) : 0;
    }

    public static String parsePropertyName(final String methodName) {
        if (methodName == null || methodName.length() == 3) {
            return methodName;
        }

        char[] charSequence = methodName.substring(3).toCharArray();
        charSequence[0] = Character.toLowerCase(charSequence[0]);

        return new String(charSequence);
    }

    public static void main(String[] args) {

        SimpleValueMapper<Product> mapper = new SimpleValueMapper<Product>(Product::new);

        Map<String, String> map = new HashMap<>();
        map.put("key", "key123");
        map.put("bigValue", "10.45");
        map.put("id", "123");
        map.put("nativeInt", "42");
        map.put("boxedLong", "4442");

        Product p = mapper.map(map);
        System.out.println("done");
    }
}
