package main.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/misc")
public class MiscController {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@RequestMapping(value = "/topsecret", method = RequestMethod.GET)
	protected String showTopSecret() {
		return "I pretend to be poor but I own half of my kidney.";
	}

	@RequestMapping(value = "/save-redis", method = RequestMethod.POST)
	protected void saveRedisTest(@RequestBody Map<String, Object> mp) {
		mp.entrySet().forEach(entry -> redisTemplate.opsForValue().set(entry.getKey(), entry.getValue()));
	}
	
	@RequestMapping(value = "/get-redis", method = RequestMethod.GET)
	protected Map<String, Object> getRedisTest(@RequestBody Map<String, List<String>> keysMap) {
		Map<String, Object> result = new HashMap<>();
		keysMap.get("keys").forEach(key -> result.put(key, redisTemplate.opsForValue().get(key)));
		return result;
	}
}
