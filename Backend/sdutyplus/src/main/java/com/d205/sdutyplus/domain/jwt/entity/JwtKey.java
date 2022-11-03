package com.d205.sdutyplus.domain.jwt.entity;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.jsonwebtoken.security.Keys;

/**
 * JWT Key를 제공하고 조회합니다.
 */
public class JwtKey {
    /**
     * Kid-Key List 외부로 절대 유출되어서는 안됩니다.(AES암호화 예정)
     */
    private static final Map<String, String> SECRET_KEY_SET = new HashMap<String, String>() {
        {
            put("key1",
                    "SpringSecurityJWTPracticeProjectIsSoGoodAndThisProjectIsSoFunSpringSecurityJWTPracticeProjectIsSoGoodAndThisProjectIsSoFun");
            put("key2",
                    "GoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurity");
            put("key3",
                    "HelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurity");
        }
    };

    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static Random randomIndex = new Random();

    /**
     * SECRET_KEY_SET 에서 랜덤한 KEY 가져오기
     *
     * @return keyId와 key Pair
     */
    public static Pair<String, Key> getRandomKey() {
        String keyId = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(keyId);
        Pair<String, Key> pair = new Pair<String, Key>(keyId,
                Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
        //주어진 키를 암호화에 적절하게 길이를 조절하여 반환해준다.
        return pair;
    }

    /**
     * kid로 Key찾기
     *
     * @param keyId keyId
     * @return Key
     */
    public static Key getKey(String keyId) {
        String key = SECRET_KEY_SET.getOrDefault(keyId, null);
        if (key == null)
            return null;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
