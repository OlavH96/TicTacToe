package controller;

import data.Opponents;
import data.Session;
import javax.ws.rs.NotAuthorizedException;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import org.junit.Before;
import org.junit.Test;
import repo.SessionRepository;

/**
 * @author nilstes
 */
public class SessionControllerTest {

    private SessionRepository repo;
    private SessionController controller;
    
    @Before
    public void setUp() {
        repo = new SessionRepository();
        controller = new SessionController(repo);
    }
    
    @Test
    public void testThatUserCanLogOnWithOnlyUsername() {
        Session session = controller.createSession("nils");
        assertNotNull(session.getLoggedOn());     
        assertTrue(repo.existsSession("nils"));
    }

    @Test(expected = NotAuthorizedException.class)
    public void testThatUserCannotLogonTwice() {
        Session session1 = controller.createSession("nils2");
        Session session2 = controller.createSession("nils2");
    }
    
    @Test
    public void testThatWeCanGetAListOfOpponentsNotIncludingMyself() {
        Session session1 = controller.createSession("hans");
        Session session2 = controller.createSession("petter");
        Session session3 = controller.createSession("fredrik");
        
        Opponents opponents = controller.getPossibleOpponents("hans");
        assertEquals(opponents.getUserNames().size(), 2);
        assertFalse(opponents.getUserNames().contains("hans"));    
    }
}
