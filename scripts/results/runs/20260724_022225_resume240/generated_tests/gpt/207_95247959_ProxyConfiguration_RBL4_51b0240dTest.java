package software.amazon.awssdk.http.nio.netty;

import org.testng.Assert;
import org.testng.annotations.Test;
import software.amazon.awssdk.http.nio.netty.ProxyConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProxyConfiguration_RBL4_51b0240dTest {

    @Test
    public void testBuilderWithAllParameters() {
        Set<String> nonProxyHosts = new HashSet<>();
        nonProxyHosts.add("localhost");
        nonProxyHosts.add("127.0.0.1");

        ProxyConfiguration proxyConfig = ProxyConfiguration.builder()
                .scheme("https")
                .host("proxy.example.com")
                .port(8080)
                .username("user")
                .password("pass")
                .nonProxyHosts(nonProxyHosts)
                .useSystemPropertyValues(false)
                .useEnvironmentVariableValues(false)
                .build();

        Assert.assertEquals(proxyConfig.scheme(), "https");
        Assert.assertEquals(proxyConfig.host(), "proxy.example.com");
        Assert.assertEquals(proxyConfig.port(), 8080);
        Assert.assertEquals(proxyConfig.username(), "user");
        Assert.assertEquals(proxyConfig.password(), "pass");
        Assert.assertEquals(proxyConfig.nonProxyHosts(), Collections.unmodifiableSet(nonProxyHosts));
    }

    @Test
    public void testBuilderWithDefaultValues() {
        ProxyConfiguration proxyConfig = ProxyConfiguration.builder()
                .build();

        Assert.assertEquals(proxyConfig.scheme(), "http");
        Assert.assertNull(proxyConfig.host());
        Assert.assertEquals(proxyConfig.port(), 0);
        Assert.assertNull(proxyConfig.username());
        Assert.assertNull(proxyConfig.password());
        Assert.assertEquals(proxyConfig.nonProxyHosts(), Collections.emptySet());
    }

    @Test
    public void testEqualsAndHashCode() {
        Set<String> nonProxyHosts1 = new HashSet<>();
        nonProxyHosts1.add("localhost");

        Set<String> nonProxyHosts2 = new HashSet<>();
        nonProxyHosts2.add("localhost");

        ProxyConfiguration proxyConfig1 = ProxyConfiguration.builder()
                .scheme("http")
                .host("proxy.example.com")
                .port(8080)
                .username("user")
                .password("pass")
                .nonProxyHosts(nonProxyHosts1)
                .build();

        ProxyConfiguration proxyConfig2 = ProxyConfiguration.builder()
                .scheme("http")
                .host("proxy.example.com")
                .port(8080)
                .username("user")
                .password("pass")
                .nonProxyHosts(nonProxyHosts2)
                .build();

        Assert.assertEquals(proxyConfig1, proxyConfig2);
        Assert.assertEquals(proxyConfig1.hashCode(), proxyConfig2.hashCode());
    }

    @Test
    public void testNonProxyHostsWithNull() {
        ProxyConfiguration proxyConfig = ProxyConfiguration.builder()
                .nonProxyHosts(null)
                .build();

        Assert.assertEquals(proxyConfig.nonProxyHosts(), Collections.emptySet());
    }

    @Test
    public void testUseSystemPropertyValues() {
        ProxyConfiguration proxyConfig = ProxyConfiguration.builder()
                .useSystemPropertyValues(true)
                .build();

        Assert.assertTrue(proxyConfig.nonProxyHosts().isEmpty());
    }

    @Test
    public void testUseEnvironmentVariableValues() {
        ProxyConfiguration proxyConfig = ProxyConfiguration.builder()
                .useEnvironmentVariableValues(true)
                .build();

        Assert.assertTrue(proxyConfig.nonProxyHosts().isEmpty());
    }
}
