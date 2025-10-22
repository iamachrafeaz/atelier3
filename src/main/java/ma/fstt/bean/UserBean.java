package ma.fstt.bean;


import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;
import ma.fstt.model.User;
import ma.fstt.service.UserService;
import ma.fstt.utils.UserSessionBean;

import java.io.Serializable;

@Named
@RequestScoped
@Getter
@Setter
@NoArgsConstructor
public class UserBean implements Serializable {
    private User user;
    private String password;

    @Inject
    UserService userService;

    @Inject
    UserSessionBean userSessionBean;

    public void loadProfile() {
        User sessionUser = userSessionBean.getCurrentUser();
        if(sessionUser != null) {
            user = userService.getUser(sessionUser);
            password = ""; // clear password field
        }
    }

    public void updateProfile() {
        boolean success = userService.updateUser(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                password,
                user.getPhoneNumber(),
                user.getAddress()
        );

        if(success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated successfully!", ""));
        }
    }

    public void login()
    {
        userService.loginUser(user.getEmail(), user.getPassword());
        userSessionBean.setCurrentUser(user);

        // redirect
    }

    public void register()
    {
        userService.registerUser(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                password,
                user.getPhoneNumber(),
                user.getAddress()
        );

        //redirect
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
