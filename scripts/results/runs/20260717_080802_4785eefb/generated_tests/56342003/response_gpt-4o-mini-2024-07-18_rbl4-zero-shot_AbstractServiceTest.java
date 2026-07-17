package net.krotscheck.kangaroo.authz.admin.v1.resource;

import net.krotscheck.kangaroo.authz.admin.v1.resource.AbstractService;
import net.krotscheck.kangaroo.authz.common.database.entity.Application;
import net.krotscheck.kangaroo.authz.common.database.entity.User;
import net.krotscheck.kangaroo.authz.common.database.entity.OAuthToken;
import net.krotscheck.kangaroo.common.hibernate.id.MalformedIdException;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.SearchFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.math.BigInteger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractServiceTest {

    private AbstractService service;
    private InjectionManager injector;
    private Session session;
    private SearchFactory searchFactory;
    private FullTextSession fullTextSession;
    private SecurityContext securityContext;
    private UriInfo uriInfo;

    @Before
    public void setUp() {
        service = Mockito.mock(AbstractService.class, Mockito.CALLS_REAL_METHODS);
        injector = mock(InjectionManager.class);
        session = mock(Session.class);
        searchFactory = mock(SearchFactory.class);
        fullTextSession = mock(FullTextSession.class);
        securityContext = mock(SecurityContext.class);
        uriInfo = mock(UriInfo.class);

        service.setInjector(injector);
        service.setSession(session);
        service.setSearchFactory(searchFactory);
        service.setFullTextSession(fullTextSession);
        service.setSecurityContext(securityContext);
        service.setUriInfo(uriInfo);
    }

    @Test
    public void testGetInjector() {
        assertEquals(injector, service.getInjector());
    }

    @Test
    public void testGetSession() {
        assertEquals(session, service.getSession());
    }

    @Test
    public void testGetSearchFactory() {
        assertEquals(searchFactory, service.getSearchFactory());
    }

    @Test
    public void testGetFullTextSession() {
        assertEquals(fullTextSession, service.getFullTextSession());
    }

    @Test
    public void testGetUriInfo() {
        assertEquals(uriInfo, service.getUriInfo());
    }

    @Test
    public void testGetCurrentUser_UserExists() {
        User user = mock(User.class);
        OAuthToken token = mock(OAuthToken.class);
        when(token.getIdentity()).thenReturn(mock(OAuthToken.Identity.class));
        when(token.getIdentity().getUser()).thenReturn(user);
        when(securityContext.getUserPrincipal()).thenReturn(token);

        assertEquals(user, service.getCurrentUser());
    }

    @Test
    public void testGetCurrentUser_NoUser() {
        when(securityContext.getUserPrincipal()).thenReturn(null);
        assertNull(service.getCurrentUser());
    }

    @Test(expected = NotFoundException.class)
    public void testAssertCanAccess_NullEntity() {
        service.assertCanAccess(null, "requiredScope");
    }

    @Test
    public void testAssertCanAccess_UserIsOwner() {
        User user = mock(User.class);
        AbstractAuthzEntity entity = mock(AbstractAuthzEntity.class);
        when(entity.getOwner()).thenReturn(user);
        when(securityContext.getUserPrincipal()).thenReturn(mock(OAuthToken.class));
        when(service.getCurrentUser()).thenReturn(user);

        service.assertCanAccess(entity, "requiredScope");
    }

    @Test(expected = NotFoundException.class)
    public void testAssertCanAccess_UserNotOwnerAndNoScope() {
        User user = mock(User.class);
        AbstractAuthzEntity entity = mock(AbstractAuthzEntity.class);
        when(entity.getOwner()).thenReturn(mock(User.class));
        when(securityContext.isUserInRole("requiredScope")).thenReturn(false);
        when(service.getCurrentUser()).thenReturn(user);

        service.assertCanAccess(entity, "requiredScope");
    }

    @Test(expected = NotFoundException.class)
    public void testAssertCanAccess_UserNotOwnerAndHasScope() {
        User user = mock(User.class);
        AbstractAuthzEntity entity = mock(AbstractAuthzEntity.class);
        when(entity.getOwner()).thenReturn(mock(User.class));
        when(securityContext.isUserInRole("requiredScope")).thenReturn(true);
        when(service.getCurrentUser()).thenReturn(user);

        service.assertCanAccess(entity, "requiredScope");
    }

    @Test(expected = BadRequestException.class)
    public void testResolveOwnershipFilter_AdminScopeAndOwnerIdNull() {
        when(securityContext.isUserInRole(service.getAdminScope())).thenReturn(true);
        service.resolveOwnershipFilter(null);
    }

    @Test(expected = InvalidScopeException.class)
    public void testResolveOwnershipFilter_NoCurrentUser() {
        when(securityContext.isUserInRole(service.getAdminScope())).thenReturn(false);
        when(service.getCurrentUser()).thenReturn(null);
        service.resolveOwnershipFilter(BigInteger.ONE);
    }

    @Test(expected = InvalidScopeException.class)
    public void testResolveOwnershipFilter_OwnerIdNotEqualCurrentUser() {
        when(securityContext.isUserInRole(service.getAdminScope())).thenReturn(false);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(BigInteger.ONE);
        when(service.getCurrentUser()).thenReturn(currentUser);
        service.resolveOwnershipFilter(BigInteger.TEN);
    }

    @Test
    public void testResolveEntityInput_EntityIdNull() {
        assertNull(service.resolveEntityInput(Application.class, (BigInteger) null));
    }

    @Test(expected = MalformedIdException.class)
    public void testResolveEntityInput_EntityNotFound() {
        when(session.get(Application.class, BigInteger.ONE)).thenReturn(null);
        service.resolveEntityInput(Application.class, BigInteger.ONE);
    }

    @Test
    public void testExecuteQuery() {
        FullTextQuery query = mock(FullTextQuery.class);
        when(query.getResultSize()).thenReturn(10);
        when(query.list()).thenReturn(new Object[]{});

        Response response = service.executeQuery(Application.class, query, 0, 10);
        assertNotNull(response);
        assertEquals(10, response.getMetadata().get("total").get(0));
    }
}
