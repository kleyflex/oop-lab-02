package annotations;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Arrays;
import java.util.Comparator;

public class AnnotationInvoker {
    // Значение параметров простых типов
    private static final Map<Class<?>, Object> VALUES = Map.of(
        int.class, 3,
        long.class, 5L,
        double.class, 2.5,
        boolean.class, true,
        char.class, 'A',
        String.class, "Java"
    );

    // Вызов protected и private методов с @Repeat столько сколько в аннотации
    public static void invokeAnnotated(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("Объект для вызова не задан");
        }

        for(Method method : target.getClass().getDeclaredMethods()) {
            Repeat repeat = method.getAnnotation(Repeat.class);

            if(repeat != null && isProtectedOrPrivate(method)) {
                invoke(target, method, repeat.value());
            }
        }
    }

    private static boolean isProtectedOrPrivate(Method method) {
        int modifiers = method.getModifiers();
        return Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers);
    }

    private static void invoke(Object target, Method method, int times) {
        if (times < 0) {
            throw new IllegalStateException("Отрицательное число вызовов у метода: " + method.getName());
        }

        Object[] args = createArgs(method.getParameterTypes());

        method.setAccessible(true);
        System.out.println("Метод: " + method.getName() + ", вызовов: " + times);

        try {
            for(int i = 0; i < times; i++) {
                method.invoke(target, args);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Нет доступа к методу " + method.getName(), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Метод " + method.getName() + " завершился с ошибкой", e.getCause());
        }
    }

    // Создаем значения для списка параметров метода/конструктора
    private static Object[] createArgs(Class<?>[] types) {
        return Arrays.stream(types).map(AnnotationInvoker::createValue).toArray();
    }

    // Создаем значение параметра по типу
    private static Object createValue(Class<?> type) {
        if(VALUES.containsKey(type)) {
            return VALUES.get(type);
        }

        if(type.isArray()) {
            Object array = Array.newInstance(type.getComponentType(), 1);
            Array.set(array, 0, createValue(type.getComponentType()));
            return array;
        }

        return createObject(type);
    }

    // Создаём объект через конструктор
    // начало с меньшего кол-ва параметров
    private static Object createObject(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

        for(Constructor<?> constructor : constructors) {
            try {
                constructor.setAccessible(true);
                return constructor.newInstance(createArgs(constructor.getParameterTypes()));
            } catch (ReflectiveOperationException | RuntimeException e) {
                // пропускаем
            }
        }

        throw new IllegalArgumentException("Не удалось создать значение для типа " + type.getName());
    }



}
