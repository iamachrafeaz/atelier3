package ma.fstt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.model.User;
import ma.fstt.repository.UserRepository;
import ma.fstt.utils.PasswordUtils;

@Named
@RequestScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

    public boolean registerUser(String firstName,
                                String lastName,
                                String email,
                                String password,
                                Long phoneNumber,
                                String address) {

        if(!validateUserFields(firstName, lastName, email, password, phoneNumber, address)) return false;

        if(checkExistence(email)){
            showError("User with email already exists.");
            return false;
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setPassword(PasswordUtils.hashPassword(password));

        userRepository.save(user);
        showInfo("User registered successfully!");
        return true;
    }

    public boolean updateUser(String firstName,
                              String lastName,
                              String email,
                              String password,
                              Long phoneNumber,
                              String address) {
        if(!validateUserFields(firstName, lastName, email, password, phoneNumber, address)) return false;

        User existingUser = userRepository.findByEmail(email);
        if(existingUser == null) {
            showError("User not found!");
            return false;
        }

        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setPhoneNumber(phoneNumber);
        existingUser.setAddress(address);
        existingUser.setPassword(PasswordUtils.hashPassword(password));

        userRepository.save(existingUser);
        showInfo("User updated successfully!");
        return true;
    }


    public User loginUser(String email, String password) {
        if(email == null || email.isEmpty()) {
            showError("Email cannot be empty.");
            return null;
        }
        if(password == null || password.isEmpty()) {
            showError("Password cannot be empty.");
            return null;
        }


        User user = userRepository.findByEmail(email);

        if(user == null){
            showError("User not found!");
            return null;
        }

        if(!PasswordUtils.checkPassword(password, user.getPassword())){
            showError("Incorrect password.");
            return null;
        }

        showInfo("Login successful. Welcome " + user.getFirstName() + "!");

        return user;
    }


    private boolean validateUserFields(String firstName, String lastName, String email, String password, Long phoneNumber, String address) {
        if(firstName == null || firstName.isEmpty()) { showError("First name cannot be empty."); return false; }
        if(lastName == null || lastName.isEmpty()) { showError("Last name cannot be empty."); return false; }
        if(email == null || email.isEmpty()) { showError("Email cannot be empty."); return false; }
        if(password == null || password.isEmpty()) { showError("Password cannot be empty."); return false; }
        if(phoneNumber == null || phoneNumber <= 0) { showError("Invalid phone number."); return false; }
        if(address == null || address.isEmpty()) { showError("Address cannot be empty."); return false; }
        return true;
    }

    private boolean checkExistence(String email) {
        return userRepository.existsByEmail(email);
    }

    private void showError(String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
    }

    private void showInfo(String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
    }

    public User getUser(User user){
        if(user == null) return null;
        return userRepository.findByEmail(user.getEmail());
    }
}
