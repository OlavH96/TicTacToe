package controller;

import data.Game;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import repo.GameRepository;

/**
 * @author nilstes
 */
public class GameControllerTest {

    @Test(expected = NotFoundException.class)
    public void testThatPlayerCannotMoveIfGameDoesNotExist() {
        GameController c = new GameController(new GameRepository());
        c.move("player", "nonExistentGameId", 1, 2);
    }

    @Test(expected = ClientErrorException.class)
    public void testThatPlayerCannotMoveIfNotPlayersTurn() {
        GameController c = new GameController(new GameRepository());
        Game game = c.createGame("inviter", "invitee", 10);
        if(game.getTurn().equals("inviter")) {
            c.move(game.getGameId(), "invitee", 2, 3);
        } else {
            c.move(game.getGameId(), "inviter", 2, 3);
        }
    }
    
    @Test(expected = ClientErrorException.class)
    public void testThatPlayerCannotMoveToPositionOutOfRange() {
        GameController c = new GameController(new GameRepository());
        Game game = c.createGame("inviter", "invitee", 10);
        assertNotNull(game);
        c.move(game.getGameId(), game.getTurn(), 12, 21);
    }
    
    @Test(expected = ClientErrorException.class)
    public void testThatPlayerCannotChooseOccupiesSquare() {
        GameController c = new GameController(new GameRepository());
        Game game = c.createGame("inviter", "invitee", 10);
        c.move(game.getGameId(), game.getTurn(), 2, 1);
        c.move(game.getGameId(), game.getTurn(), 2, 1);
    }
    
    @Test
    public void testThatUsersCanReceiveInvitesFromOtherUsers() {
        GameController c = new GameController(new GameRepository());
        Game game1 = c.createGame("inviter1", "me", 10);
        Game game2 = c.createGame("inviter2", "me", 10);
        Game game3 = c.createGame("inviter3", "me", 10);
        List<Game> invites = c.getInvites("me");
        assertEquals(invites.size(), 3);
    }
    
    @Test
    public void testThatInviteIsReceivedOnlyOnce() {
        GameController c = new GameController(new GameRepository());
        Game game1 = c.createGame("inviter1", "me", 10);
        Game game2 = c.createGame("inviter2", "me", 10);
        Game game3 = c.createGame("inviter3", "me", 10);
        List<Game> invites = c.getInvites("me");
        assertEquals(invites.size(), 3);        
        invites = c.getInvites("me");
        assertEquals(invites.size(), 0);        
    } 
}
