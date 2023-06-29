package main.validators;

import java.lang.reflect.Field;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;

import main.annotations.NonNullable;
import main.restmodels.UserRegisterRequest;


public class Validator {
	/*
	 * Right now validating only fields that cannot be null using non null annotation
	 * Maybe in future we can use a better annotation that could provide more parameters upon which a
	 * field has to be validated
	 * For example,
	 * @ExampleAnnotation(minLength = 8, maxLength = 20)
	 * private String password;
	 */
	public static <T> void validateNonNullFields(T request) throws Exception {
		Field[] fields = request.getClass().getFields();
		for(Field field: fields) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(NonNullable.class)) {
				if(Objects.isNull(field.get(request))) {
					throw new Exception();
				} else if (BooleanUtils.isFalse(field.getDeclaringClass().isPrimitive() || field.getDeclaringClass().isEnum())) {
					validateNonNullFields(field.get(request));
				}
			}
		}
	}
}
