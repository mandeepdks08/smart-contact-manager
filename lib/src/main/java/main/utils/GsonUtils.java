package main.utils;

import java.util.Objects;

import com.google.gson.Gson;

public class GsonUtils {
	private static Gson gson;
	
	public static Gson getGson() {
		if(Objects.isNull(gson)) {
			gson = new Gson();
		}
		return gson;
	}
	
	public static <T, V> V convert(T input, Class<V> outputClass) {
		String inputJson = GsonUtils.getGson().toJson(input);
		return GsonUtils.getGson().fromJson(inputJson, outputClass);
	}
}
