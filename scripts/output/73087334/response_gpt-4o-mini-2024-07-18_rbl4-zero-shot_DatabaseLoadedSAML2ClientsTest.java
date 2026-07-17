
package com.idc.webchannel.pac4j.extensions.saml.client;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pac4j.core.client.Client;
import org.pac4j.saml.client.SAML2Client;

import com.idc.webchannel.pac4j.extensions.saml.dao.api.SamlClientDao;

public class DatabaseLoadedSAML2ClientsTest {

    @Mock
    private SamlClientDao samlClientDao;

    @InjectMocks
    private DatabaseLoadedSAML2Clients databaseLoadedSAML2Clients;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadClientsInternal() {
        when(samlClientDao.loadClientNames()).thenReturn(Arrays.asList("client1", "client2"));

        List<SAML2Client> clients = databaseLoadedSAML2Clients.loadClientsInternal();

        assertEquals(2, clients.size());
        assertEquals("client1", clients.get(0).getName());
        assertEquals("client2", clients.get(1).getName());
    }

    @Test
    public void testLoadClientsInternalWithNoClients() {
        when(samlClientDao.loadClientNames()).thenReturn(Collections.emptyList());

        List<SAML2Client> clients = databaseLoadedSAML2Clients.loadClientsInternal();

        assertTrue(clients.isEmpty());
    }

    @Test(expected = TechnicalException.class)
    public void testInternalInitWithDuplicateClientNames() {
        when(samlClientDao.loadClientNames()).thenReturn(Arrays.asList("client1", "CLIENT1"));

        databaseLoadedSAML2Clients.internalInit();
    }

    @Test
    public void testGetClients() {
        when(samlClientDao.loadClientNames()).thenReturn(Arrays.asList("client1", "client2"));
        databaseLoadedSAML2Clients.internalInit();

        List<Client> clients = databaseLoadedSAML2Clients.getClients();

        assertEquals(2, clients.size());
        assertEquals("client1", clients.get(0).getName());
        assertEquals("client2", clients.get(1).getName());
    }

    @Test
    public void testGetClientsWithNoClients() {
        when(samlClientDao.loadClientNames()).thenReturn(Collections.emptyList());
        databaseLoadedSAML2Clients.internalInit();

        List<Client> clients = databaseLoadedSAML2Clients.getClients();

        assertTrue(clients.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetClientsList() {
        databaseLoadedSAML2Clients.setClients(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetClientsVarargs() {
        databaseLoadedSAML2Clients.setClients(new Client[0]);
    }
}
