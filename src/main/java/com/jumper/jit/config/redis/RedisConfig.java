package com.jumper.jit.config.redis;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RedisConfig {
    @Resource
    private RedisConsumer redisConsumer;

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);

        t.setKeySerializer(RedisSerializer.string());
        t.setValueSerializer(RedisSerializer.string());

        t.setHashKeySerializer(RedisSerializer.string());
        t.setHashValueSerializer(RedisSerializer.string());

        t.setEnableTransactionSupport(false);
        t.afterPropertiesSet();

        return t;
    }


    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(redisConsumer);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        List<PatternTopic> l = Arrays.stream(RedisTopics.values()).map(redisTopics -> new PatternTopic(redisTopics.toString())).toList();
        container.addMessageListener(listenerAdapter, l);
        return container;
    }

}
