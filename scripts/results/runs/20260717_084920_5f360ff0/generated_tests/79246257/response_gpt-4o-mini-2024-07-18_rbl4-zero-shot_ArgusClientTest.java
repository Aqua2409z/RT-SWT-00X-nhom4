package com.salesforce.argus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.salesforce.argus.model.*;
import com.salesforce.pyplyn.client.UnauthorizedException;
import com.salesforce.pyplyn.configuration.EndpointConnector;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArgusClientTest {

    private ArgusClient argusClient;
    private EndpointConnector connector;

    @Before
    public void setUp() {
        connector = mock(EndpointConnector.class);
        argusClient = new ArgusClient(connector);
    }

    @Test
    public void testIsAuthenticated() {
        assertFalse(argusClient.isAuthenticated());
        argusClient.accessToken = new byte[]{1, 2, 3};
        assertTrue(argusClient.isAuthenticated());
    }

    @Test
    public void testAuthWithValidCredentials() throws UnauthorizedException {
        when(connector.username()).thenReturn("user");
        when(connector.password()).thenReturn(new byte[]{1, 2, 3});
        AuthToken token = mock(AuthToken.class);
        when(token.accessToken()).thenReturn(new byte[]{4, 5, 6});
        when(token.refreshToken()).thenReturn(new byte[]{7, 8, 9});
        when(argusClient.svc().login(any())).thenReturn(token);

        assertTrue(argusClient.auth());
        assertNotNull(argusClient.accessToken);
        assertNotNull(argusClient.refreshToken);
    }

    @Test(expected = UnauthorizedException.class)
    public void testAuthWithInvalidCredentials() throws UnauthorizedException {
        when(connector.username()).thenReturn("user");
        when(connector.password()).thenReturn(new byte[]{1, 2, 3});
        when(argusClient.svc().login(any())).thenReturn(null);

        argusClient.auth();
    }

    @Test
    public void testGetMetrics() throws UnauthorizedException {
        List<String> expressions = Arrays.asList("metric1", "metric2");
        MetricResponse response = mock(MetricResponse.class);
        when(argusClient.svc().getMetrics(anyString(), anyList())).thenReturn(Collections.singletonList(response));

        List<MetricResponse> metrics = argusClient.getMetrics(expressions);
        assertNotNull(metrics);
        assertEquals(1, metrics.size());
        assertEquals(response, metrics.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMetricsWithNullExpressions() throws UnauthorizedException {
        argusClient.getMetrics(null);
    }

    @Test
    public void testCreateAlert() throws UnauthorizedException {
        AlertObject alert = mock(AlertObject.class);
        when(argusClient.svc().createAlert(anyString(), any())).thenReturn(alert);

        AlertObject createdAlert = argusClient.createAlert(alert);
        assertNotNull(createdAlert);
        assertEquals(alert, createdAlert);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAlertWithNull() throws UnauthorizedException {
        argusClient.createAlert(null);
    }

    @Test
    public void testLoadAllAlerts() throws UnauthorizedException {
        AlertObject alert = mock(AlertObject.class);
        when(argusClient.svc().getAllAlerts(anyString())).thenReturn(Collections.singletonList(alert));

        List<AlertObject> alerts = argusClient.loadAllAlerts();
        assertNotNull(alerts);
        assertEquals(1, alerts.size());
        assertEquals(alert, alerts.get(0));
    }

    @Test
    public void testDeleteAlert() throws UnauthorizedException {
        long alertId = 1L;
        doNothing().when(argusClient.svc()).deleteAlert(anyString(), eq(alertId));

        argusClient.deleteAlert(alertId);
        verify(argusClient.svc()).deleteAlert(anyString(), eq(alertId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAlertWithNullId() throws UnauthorizedException {
        argusClient.deleteAlert(0);
    }

    @Test
    public void testGetDashboardById() throws UnauthorizedException {
        long dashboardId = 1L;
        DashboardObject dashboard = mock(DashboardObject.class);
        when(argusClient.svc().getDashboardById(anyString(), eq(dashboardId))).thenReturn(dashboard);

        DashboardObject result = argusClient.getDashboardById(dashboardId);
        assertNotNull(result);
        assertEquals(dashboard, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDashboardByIdWithNullId() throws UnauthorizedException {
        argusClient.getDashboardById(0);
    }
}
