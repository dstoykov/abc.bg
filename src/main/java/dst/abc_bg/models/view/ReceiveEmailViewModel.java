package dst.abc_bg.models.view;

import java.time.LocalDateTime;

public class ReceiveEmailViewModel {
    private String id;

    private String sender;

    private UserViewModel recipient;

    private String subject;

    private String content;

    private LocalDateTime sentOn;

    private LocalDateTime deletedOn;

    public ReceiveEmailViewModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public UserViewModel getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserViewModel recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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
