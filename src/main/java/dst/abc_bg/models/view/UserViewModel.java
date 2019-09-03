package dst.abc_bg.models.view;

import dst.abc_bg.entities.Role;

import java.time.LocalDate;
import java.util.Set;

public class UserViewModel {
    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private LocalDate deletedOn;

    private Set<Role> authorities;

    private Set<SendEmailViewModel> sentEmails;

    private Set<ReceiveEmailViewModel> receivedEmails;

    public UserViewModel() {
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

    public LocalDate getDeletedOn() {
        return this.deletedOn;
    }

    public void setDeletedOn(LocalDate deletedOn) {
        this.deletedOn = deletedOn;
    }

    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public Set<SendEmailViewModel> getSentEmails() {
        return this.sentEmails;
    }

    public void setSentEmails(Set<SendEmailViewModel> sentEmails) {
        this.sentEmails = sentEmails;
    }

    public Set<ReceiveEmailViewModel> getReceivedEmails() {
        return this.receivedEmails;
    }

    public void setReceivedEmails(Set<ReceiveEmailViewModel> receivedEmails) {
        this.receivedEmails = receivedEmails;
    }
}
