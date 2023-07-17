package main.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReflectionUtils {

	public static <T> List<Field> getNonNullFields(T obj) {
		try {
			List<Field> nonNullFields = new ArrayList<>();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(obj) != null) {
					nonNullFields.add(field);
				}
			}
			return nonNullFields;
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
}
