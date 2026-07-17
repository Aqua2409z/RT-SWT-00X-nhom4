
package com.amadeus.session.repository.redis;

import com.amadeus.session.SessionConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestRedisConfiguration {

    private SessionConfiguration mockConfig;
    private RedisConfiguration redisConfig;

    @Before
    public void setUp() {
        mockConfig = new SessionConfiguration();
        redisConfig = new RedisConfiguration(mockConfig);
    }

    @Test
    public void testDefaultValues() {
        assertEquals("localhost", redisConfig.server);
        assertEquals("6379", redisConfig.port);
        assertEquals("100", redisConfig.poolSize);
        assertEquals(2000, (int) redisConfig.timeout);
        assertEquals(RedisConfiguration.DEFAULT_REDIS_MASTER_NAME, redisConfig.masterName);
        assertEquals("SINGLE", redisConfig.clusterMode);
        assertTrue(redisConfig.supportIpV4);
        assertFalse(redisConfig.supportIpV6);
        assertEquals(ExpirationStrategy.ZRANGE, redisConfig.getStrategy());
    }

    @Test
    public void testCustomConfiguration() {
        mockConfig.setAttribute(RedisConfiguration.REDIS_HOST, "127.0.0.1");
        mockConfig.setAttribute(RedisConfiguration.REDIS_PORT, "6380");
        mockConfig.setAttribute(RedisConfiguration.REDIS_POOL_SIZE, "50");
        mockConfig.setAttribute(RedisConfiguration.REDIS_TIMEOUT, "3000");
        mockConfig.setAttribute(RedisConfiguration.REDIS_MASTER_NAME, "myMaster");
        mockConfig.setAttribute(RedisConfiguration.REDIS_CLUSTER_MODE, "SENTINEL");
        mockConfig.setAttribute(RedisConfiguration.REDIS_USE_IPV6, "true");
        mockConfig.setAttribute(RedisConfiguration.REDIS_USE_IPV4, "false");
        mockConfig.setAttribute(RedisConfiguration.REDIS_EXPIRATION_STRATEGY, "NOTIF");

        redisConfig = new RedisConfiguration(mockConfig);

        assertEquals("127.0.0.1", redisConfig.server);
        assertEquals("6380", redisConfig.port);
        assertEquals("50", redisConfig.poolSize);
        assertEquals(3000, (int) redisConfig.timeout);
        assertEquals("myMaster", redisConfig.masterName);
        assertEquals("SENTINEL", redisConfig.clusterMode);
        assertTrue(redisConfig.supportIpV6);
        assertFalse(redisConfig.supportIpV4);
        assertEquals(ExpirationStrategy.NOTIF, redisConfig.getStrategy());
    }

    @Test
    public void testHostsAndPorts() throws Exception {
        mockConfig.setAttribute(RedisConfiguration.REDIS_HOST, "localhost:6379,127.0.0.1:6380");
        redisConfig = new RedisConfiguration(mockConfig);
        List<RedisConfiguration.HostAndPort> hostsAndPorts = redisConfig.hostsAndPorts();

        assertEquals(2, hostsAndPorts.size());
        assertEquals("localhost", hostsAndPorts.get(0).getHost());
        assertEquals(6379, hostsAndPorts.get(0).getPort());
        assertEquals("127.0.0.1", hostsAndPorts.get(1).getHost());
        assertEquals(6380, hostsAndPorts.get(1).getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHost() throws Exception {
        mockConfig.setAttribute(RedisConfiguration.REDIS_HOST, "invalid_host");
        redisConfig = new RedisConfiguration(mockConfig);
        redisConfig.hostsAndPorts();
    }

    @Test
    public void testSentinels() {
        mockConfig.setAttribute(RedisConfiguration.REDIS_HOST, "localhost:6379;127.0.0.1:6380");
        redisConfig = new RedisConfiguration(mockConfig);
        assertEquals(2, redisConfig.sentinels().size());
        assertTrue(redisConfig.sentinels().contains("localhost:6379"));
        assertTrue(redisConfig.sentinels().contains("127.0.0.1:6380"));
    }

    @Test
    public void testToString() {
        String expectedString = "RedisConfiguration [clusterMode=SINGLE, masterName=com.amadeus.session, server=localhost, port=6379, poolSize=100, strategy=ZRANGE, supportIpV6=false, supportIpV4=true, timeout=2000]";
        assertEquals(expectedString, redisConfig.toString());
    }
}
