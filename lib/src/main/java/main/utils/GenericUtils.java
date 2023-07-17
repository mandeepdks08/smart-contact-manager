package main.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import main.customExceptions.CustomGeneralException;
import main.datamodels.BaseFilter;

public class GenericUtils {

	public static <T> List<T> filter(List<T> entities, BaseFilter filter) {
		if (CollectionUtils.isEmpty(entities)) {
			return entities;
		}
		List<Field> filterFields = ReflectionUtils.getNonNullFields(filter);
		Map<String, Field> entityFields = getFieldNameAgainstFieldMap(entities.get(0));
		return entities.stream().filter(entity -> filterFields.stream().allMatch(filterField -> {
			try {
				filterField.setAccessible(true);
				Field entityField = entityFields.get(filterField.getName());
				if (entityField == null) {
					return true;
				}
				entityField.setAccessible(true);
				return filterField.get(filter).equals(entityField.get(entity));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
		})).collect(Collectors.toList());
	}

	public static <T, V> T update(T baseModel, V updates) throws CustomGeneralException {
		List<Field> updatesFields = ReflectionUtils.getNonNullFields(updates);
		Map<String, Field> baseModelFields = getFieldNameAgainstFieldMap(baseModel);
		for (Field updatesField : updatesFields) {
			try {
				updatesField.setAccessible(true);
				Field baseModelField = baseModelFields.get(updatesField.getName());
				baseModelField.setAccessible(true);
				baseModelField.set(baseModel, updatesField.get(updates));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new CustomGeneralException("Could not update");
			}
		}
		return baseModel;
	}

	public static <T> Map<String, Field> getFieldNameAgainstFieldMap(T obj) {
		if (obj == null)
			return null;
		return Arrays.stream(obj.getClass().getDeclaredFields())
				.collect(Collectors.toMap(Field::getName, field -> field));
	}
}
