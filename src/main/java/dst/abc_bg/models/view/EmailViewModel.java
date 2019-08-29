package dst.abc_bg.models.view;

import dst.abc_bg.models.service.UserServiceModel;

import java.time.LocalDateTime;

public class EmailViewModel {
    private String id;

    private UserServiceModel sender;

    private UserServiceModel recipient;

    private LocalDateTime sentOn;

    private LocalDateTime deletedOn;

    public EmailViewModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserServiceModel getSender() {
        return this.sender;
    }

    public void setSender(UserServiceModel sender) {
        this.sender = sender;
    }

    public UserServiceModel getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserServiceModel recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getSentOn() {
        return this.sentOn;
    }

    public void setSentOn(LocalDateTime sentOn) {
        this.sentOn = sentOn;
    }

    public LocalDateTime getDeletedOn() {
        return this.deletedOn;
    }

    public void setDeletedOn(LocalDateTime deletedOn) {
        this.deletedOn = deletedOn;
    }
}
