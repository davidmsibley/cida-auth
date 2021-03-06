package gov.usgs.cida.auth.client;

import gov.usgs.cida.auth.model.AuthToken;

import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author isuftin
 * @author thongsav
 */
public class AuthClientTest extends BaseClientTest{
	public AuthClientTest() {
	}
	
	@Before
	public void setUp() throws URISyntaxException {
		instance = new AuthClient(authUrl);
	}

	@Test
	public void testGetNewToken() throws URISyntaxException {
		System.out.println("getNewToken");
		String username = "testuser";
		String password = "testpassword";
		AuthToken result = instance.getNewToken(username, password);
		assertNotNull(result);
		assertThat(result.getTokenId(), is(equalTo("fda34827-f5d7-44d7-b46f-db6603accb7c")));
		assertThat(result.getExpires(), is(notNullValue()));
		assertThat(result.getIssued(), is(notNullValue()));
		assertThat(result.getLastAccess(), is(notNullValue()));
		assertFalse(result.isExpired());
	}
	
	@Test
	public void testGetRolesByToken() throws URISyntaxException {
		System.out.println("getRolesByToken");
		List<String> result = instance.getRolesByToken(tokenId);
		assertNotNull(result);
		assertThat(result.size(), is(equalTo(2)));
		assertThat(result.get(0), is(equalTo("AN_AUTH_ROLE")));
		assertThat(result.get(1), is(equalTo("AUTH_LEVEL_TWO")));
	}

	@Test
	public void testGetNewTokenWithErrorCode() throws URISyntaxException {
		System.out.println("testGetNewTokenGetting401");
		String username = "testuser";
		String password = "testpassword";
		instance = new AuthClient(authUrl + "invalid");
		AuthToken result = instance.getNewToken(username, password);
		assertNull(result);
	}

	@Test
	public void testInvalidateToken() throws URISyntaxException {
		System.out.println("testInvalidateToken");
		boolean deleted = instance.invalidateToken(tokenId);
		assertThat(deleted, is(true));
	}

	@Test
	public void testInvalidateTokenWithInvalidTokenID() throws URISyntaxException {
		System.out.println("testInvalidateTokenWithInvalidTokenID");
		boolean deleted = instance.invalidateToken(tokenId + "invalid");
		assertThat(deleted, is(false));
	}

	@Test
	public void testGetToken() throws URISyntaxException {
		System.out.println("getToken");
		AuthToken result = instance.getToken(tokenId);
		assertNotNull(result);
		assertThat(result.getTokenId(), is(equalTo("fda34827-f5d7-44d7-b46f-db6603accb7c")));
		assertThat(result.getExpires(), is(notNullValue()));
		assertThat(result.getIssued(), is(notNullValue()));
		assertThat(result.getLastAccess(), is(notNullValue()));
		assertFalse(result.isExpired());
		
		AuthToken result2 = instance.getToken(expiredTokenId);
		assertNotNull(result2);
		assertThat(result2.getTokenId(), is(equalTo(expiredTokenId)));
		assertThat(result2.getExpires(), is(notNullValue()));
		assertThat(result2.getIssued(), is(notNullValue()));
		assertThat(result2.getLastAccess(), is(notNullValue()));
		assertTrue(result2.isExpired());
	}

	@Test
	public void testGetWrongToken() throws URISyntaxException {
		System.out.println("testGetWrongToken");
		AuthToken result = instance.getToken(tokenId + '-');
		assertNull(result);
	}

}
