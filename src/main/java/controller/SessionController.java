package controller;

import data.Opponents;
import data.Session;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ws.rs.NotAuthorizedException;
import repo.SessionRepository;

/**
 * @author nilstes
 */
public class SessionController {
    
    private SessionRepository repo;

    public SessionController(SessionRepository repo) {
        this.repo = repo;
    }
    
    public Session createSession(String userName) {
        if(repo.existsSession(userName)) {
            throw new NotAuthorizedException("Bruker er allerede pålogget");
        }
        Session session = new Session();
        session.setLoggedOn(new Date());
        session.setUserName(userName);
        repo.addSession(session);
        return session;
    }
    
    public void removeSession(String userName) {
        repo.removeSession(userName);
    } 
    
    public Opponents getPossibleOpponents(String myUserName) {
        Collection<String> userNames = repo.getAllUserNames();
        Opponents opponents = new Opponents();
        for(String userName : userNames) {
            if(!userName.equals(myUserName)) {
                opponents.addUserName(userName);
            }
        }
        return opponents;
    }
}