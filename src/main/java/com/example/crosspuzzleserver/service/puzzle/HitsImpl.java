package com.example.crosspuzzleserver.service.puzzle;

import com.example.crosspuzzleserver.service.puzzle.spi.Hits;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitsImpl implements Hits {

    JedisPooled jedis = new JedisPooled("redis", 6379);

    private static final int EXPIRE_TIME = 60 * 60 * 24; //하루

    @Override
    public boolean isHit(String cookieValue, String boardId) {
        String key = cookieValue + boardId;
        if (jedis.get(key) == null) {
            jedis.set(key, boardId);
            jedis.expire(key, EXPIRE_TIME);
            return false;
        }
        return true;
    }

}
