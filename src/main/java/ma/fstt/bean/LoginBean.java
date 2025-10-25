package ma.fstt.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.model.User;
import ma.fstt.service.UserService;

@Named("loginBean")
@RequestScoped
public class LoginBean {
    private String email;
    private String password;

    @Inject
    UserService userService;

    @Inject
    UserSessionBean userSessionBean;


    public String login() {
        try {
            User user = userService.loginUser(email, password);
            userSessionBean.login(user);
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            return null;
        }
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
