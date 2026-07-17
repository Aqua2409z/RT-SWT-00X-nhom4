package net.krotscheck.kangaroo.authz.admin.v1.resource;

import net.krotscheck.kangaroo.authz.admin.v1.exception.EntityRequiredException;
import net.krotscheck.kangaroo.authz.admin.v1.servlet.Config;
import net.krotscheck.kangaroo.authz.common.database.entity.Application;
import net.krotscheck.kangaroo.authz.common.database.entity.OAuthToken;
import net.krotscheck.kangaroo.authz.common.database.entity.User;
import net.krotscheck.kangaroo.authz.oauth2.exception.RFC6749.InvalidScopeException;
import net.krotscheck.kangaroo.common.hibernate.entity.AbstractEntity;
import net.krotscheck.kangaroo.common.hibernate.id.MalformedIdException;
import org.apache.commons.configuration.Configuration;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.SearchFactory;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.math.BigInteger;

import static org.mockito.Mockito.*;

public class AbstractServiceTest {

    private AbstractService service;
    private InjectionManager injector;
    private Configuration config;
    private Session session;
    private SearchFactory searchFactory;
    private FullTextSession fullTextSession;
    private SecurityContext securityContext;
    private UriInfo uriInfo;

    @Before
    public void setUp() {
        service = spy(new AbstractService() {
            @Override
            protected String getAdminScope() {
                return "admin_scope";
            }

            @Override
            protected String getAccessScope() {
                return "user_scope";
            }
        });

        injector = mock(InjectionManager.class);
        config = mock(Configuration.class);
        session = mock(Session.class);
        searchFactory = mock(SearchFactory.class);
        fullTextSession = mock(FullTextSession.class);
        securityContext = mock(SecurityContext.class);
        uriInfo = mock(UriInfo.class);

        service.setInjector(injector);
        service.setConfig(config);
        service.setSession(session);
        service.setSearchFactory(searchFactory);
        service.setFullTextSession(fullTextSession);
        service.setSecurityContext(securityContext);
        service.setUriInfo(uriInfo);
    }

    @Test
    public void testGetInjector() {
        assertNotNull(service.getInjector());
    }

    @Test
    public void testGetUriInfo() {
        assertNotNull(service.getUriInfo());
    }

    @Test
    public void testGetSession() {
        assertNotNull(service.getSession());
    }

    @Test
    public void testGetSearchFactory() {
        assertNotNull(service.getSearchFactory());
    }

    @Test
    public void testGetFullTextSession() {
        assertNotNull(service.getFullTextSession());
    }

    @Test
    public void testGetConfig() {
        assertNotNull(service.getConfig());
    }

    @Test
    public void testGetSecurityContext() {
        assertNotNull(service.getSecurityContext());
    }

    @Test(expected = NotFoundException.class)
    public void testAssertCanAccess_NullEntity() {
        service.assertCanAccess(null, "user_scope");
    }

    @Test
    public void testAssertCanAccess_Owner() {
        User user = mock(User.class);
        when(securityContext.getUserPrincipal()).thenReturn(mock(OAuthToken.class));
        when(service.getCurrentUser()).thenReturn(user);
        AbstractAuthzEntity entity = mock(AbstractAuthzEntity.class);
        when(entity.getOwner()).thenReturn(user);

        service.assertCanAccess(entity, "user_scope");
    }

    @Test(expected = NotFoundException.class)
    public void testAssertCanAccess_NotOwner() {
        User user = mock(User.class);
        User owner = mock(User.class);
        when(securityContext.getUserPrincipal()).thenReturn(mock(OAuthToken.class));
        when(service.getCurrentUser()).thenReturn(user);
        AbstractAuthzEntity entity = mock(AbstractAuthzEntity.class);
        when(entity.getOwner()).thenReturn(owner);

        service.assertCanAccess(entity, "user_scope");
    }

    @Test(expected = InvalidScopeException.class)
    public void testResolveOwnershipFilter_NonAdmin_NoOwner() {
        when(securityContext.isUserInRole("admin_scope")).thenReturn(false);
        when(service.getCurrentUser()).thenReturn(null);

        service.resolveOwnershipFilter(null);
    }

    @Test(expected = BadRequestException.class)
    public void testResolveOwnershipFilter_Admin_NonExistentOwner() {
        when(securityContext.isUserInRole("admin_scope")).thenReturn(true);
        when(session.get(User.class, BigInteger.ONE)).thenReturn(null);

        service.resolveOwnershipFilter(BigInteger.ONE);
    }

    @Test
    public void testExecuteQuery() {
        FullTextQuery query = mock(FullTextQuery.class);
        when(query.list()).thenReturn(new Object[]{});
        when(query.getResultSize()).thenReturn(0);

        Response response = service.executeQuery(AbstractEntity.class, query, 0, 10);
        assertNotNull(response);
        assertEquals(0, response.getEntity().size());
    }

    @Test(expected = MalformedIdException.class)
    public void testResolveEntityInput_NullId() {
        service.resolveEntityInput(Application.class, (BigInteger) null);
    }

    @Test(expected = MalformedIdException.class)
    public void testResolveEntityInput_EntityNotFound() {
        when(session.get(Application.class, BigInteger.ONE)).thenReturn(null);
        service.resolveEntityInput(Application.class, BigInteger.ONE);
    }

    @Test
    public void testRequireEntityInput_EntityExists() {
        Application app = new Application();
        when(session.get(Application.class, BigInteger.ONE)).thenReturn(app);

        Application result = service.requireEntityInput(Application.class, app);
        assertNotNull(result);
    }

    @Test(expected = EntityRequiredException.class)
    public void testRequireEntityInput_EntityDoesNotExist() {
        Application app = new Application();
        when(session.get(Application.class, BigInteger.ONE)).thenReturn(null);

        service.requireEntityInput(Application.class, app);
    }
}
