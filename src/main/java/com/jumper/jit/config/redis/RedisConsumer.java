package com.jumper.jit.config.redis;

import com.jumper.jit.service.DeployService;
import com.jumper.jit.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RedisConsumer implements MessageListener {
    private IndexService indexService;
    private DeployService deployService;

    @Autowired
    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    @Autowired
    public void setDeployService(DeployService deployService) {
        this.deployService = deployService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("get msg:" + new String(message.getBody()));
        System.out.println("get pattern:" + new String(pattern));
        RedisTopics redisTopics = RedisTopics.valueOf(new String(pattern));
        try {
            process(redisTopics, message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void process(RedisTopics redisTopics, Message message) throws IOException {
        String msg = new String(message.getBody());
        Integer someId = null;
        if (!msg.isEmpty()) {
            someId = Integer.valueOf(msg);
        }
        switch (redisTopics) {
            case SubjectArticle -> deployService.deployCurrentSubjectArticle(someId);
            case AllSubjectArticle -> deployService.deployAllSubjectArticle(someId);
            case SingleArticle -> deployService.deployCurrentSingle(someId);
            case AllSingleArticle -> deployService.deployAllSingleArticle();
            case GenIndexData -> indexService.deployIndexList();
            case IndexPage -> deployService.deployIndex();
            case DelIndex -> indexService.delIndex(someId);
        }

    }
}
