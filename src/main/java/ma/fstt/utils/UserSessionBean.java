package ma.fstt.utils;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import ma.fstt.model.User;

import java.io.Serializable;

@Named
@SessionScoped
public class UserSessionBean implements Serializable {

    private User currentUser;

    public void login(User userSessionResponse) {
        this.currentUser = userSessionResponse;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
