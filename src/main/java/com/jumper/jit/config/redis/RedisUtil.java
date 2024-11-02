package com.jumper.jit.config.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class RedisUtil {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    public boolean existsKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean existsHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public boolean setObject(String key, Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean setObjectForHash(String key, String hashKy, Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForHash().put(key, hashKy, json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public <T> T getT(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    public <T> List<T> getTList(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    public <T> T getHashT(String key, String hashKey, Class<T> clazz) {
        String json = (String) redisTemplate.opsForHash().get(key, hashKey);
        if (json != null) {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    public <T> List<T> getHashTList(String key, String hashKey, Class<T> clazz) {
        String json = (String) redisTemplate.opsForHash().get(key, hashKey);
        if (json != null) {
            try {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    public boolean delByKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public List<String> getJsonListByKeyStartWith(String start) {
        Set<String> keys = redisTemplate.keys(start + "*");
        return keys != null ? redisTemplate.opsForValue().multiGet(keys) : null;
    }

    public void sendMessage(RedisTopics redisTopics, Object message) {
        redisTemplate.convertAndSend(redisTopics.toString(), message);
    }
}
