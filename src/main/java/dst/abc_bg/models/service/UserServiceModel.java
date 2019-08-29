package dst.abc_bg.models.service;

import dst.abc_bg.entities.Role;

import java.time.LocalDate;
import java.util.Set;

public class UserServiceModel {
    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private Set<Role> authorities;

    private LocalDate deletedOn;

    private Set<EmailServiceModel> sentEmails;

    private Set<EmailServiceModel> receivedEmails;

    public UserServiceModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public LocalDate getDeletedOn() {
        return this.deletedOn;
    }

    public void setDeletedOn(LocalDate deletedOn) {
        this.deletedOn = deletedOn;
    }
}
