package ru.zychkov.core.custom;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PropertyLoader {

    protected static Map<String, Object> propertiesMap;
    protected static Map<String, Object> defaultPropertiesMap;

    protected void load(String path, Object object) {
        assert (object != null);

        compileAllProperties(path, object);
        Class<?> clazz = object.getClass();

        while (clazz != Object.class) {
            Map<Field, PropertyInfo> propertyInfoMap = resolve(clazz.getDeclaredFields());

            for (Field field : propertyInfoMap.keySet()) {
                PropertyInfo info = propertyInfoMap.get(field);
                setValueToField(field, object, info.getValue());
            }

            clazz = clazz.getSuperclass();
        }
    }

    private void compileAllProperties(String path, Object object) {
        String defaultPropertiesPath =
                path.substring(0, path.lastIndexOf("-")) + path.substring(path.lastIndexOf("."));
        defaultPropertiesMap = compileProperties(defaultPropertiesPath, object);

        if (path.endsWith("-null.yaml")) {
            return;
        }

        propertiesMap = compileProperties(path, object);
    }

    private Map<String, Object> compileProperties(String path, Object object) {
        Map<String, Object> result = new HashMap<>();

        Yaml yaml = new Yaml();
        InputStream inputStream = object.getClass()
                .getClassLoader()
                .getResourceAsStream(path);

        if (inputStream != null) {
            result = yaml.load(inputStream);
        }

        return result;
    }

    private void setValueToField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Can not set object <%s> field <%s> value", object, field), e
            );
        }
    }

    private Map<Field, PropertyInfo> resolve(Field[] declaredFields) {
        Map<Field, PropertyInfo> result = new HashMap<>();

        for (Field field : declaredFields) {
            result.putAll(resolveProperty(field));
        }

        return result;
    }

    private Map<Field, PropertyInfo> resolveProperty(Field field) {
        Map<Field, PropertyInfo> result = new HashMap<>();

        if (!shouldDecorate(field)) {
            return result;
        }

        String key = getKey(field);
        Class<?> type = field.getType();

        Object property = getProperty(key, type);
        result.put(field, new PropertyInfo(key, property));

        return result;
    }

    private boolean shouldDecorate(AnnotatedElement element) {
        return element.isAnnotationPresent(Property.class);
    }

    private String getKey(AnnotatedElement element) {
        return element.getAnnotation(Property.class).value();
    }

    @SneakyThrows
    private <T> T getProperty(String path, Class<T> tClass) {
        String[] split = path.split("\\.");

        T result = getPropertyFromMap(tClass, split, propertiesMap);

        if (result == null) {
            result = getPropertyFromMap(tClass, split, defaultPropertiesMap);
        }

        return Optional.ofNullable(result).orElseThrow(() ->
                new ClassNotFoundException(String.format("Property %s not found", path)));
    }

    private <T> T getPropertyFromMap(Class<T> tClass, String[] split, Map<String, Object> incomingMap) {
        Map<String, Object> currentMap = incomingMap;
        T result = null;

        for (String s : split) {
            Object o = currentMap.get(s);

            if (o instanceof Map) {
                currentMap = (Map<String, Object>) o;
            } else {
                if (tClass.isPrimitive()) {
                    result = (T) getWrapperType(tClass).cast(o);
                } else {
                    result = tClass.cast(o);
                }

                break;
            }
        }

        return result;
    }

    private <T> Class<?> getWrapperType(Class<T> primitive) {
        Class<?> result;

        switch (primitive.getTypeName()) {
            case "int":
                result = Integer.class;
                break;
            case "boolean":
                result = Boolean.class;
                break;
            case "double":
                result = Double.class;
                break;
            default:
                result = String.class;
        }

        return result;
    }
}
