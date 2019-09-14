package dst.abc_bg.areas.email.send.entities;

import dst.abc_bg.areas.email.base.entities.BaseEmail;
import dst.abc_bg.areas.user.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "send_emails")
public class SendEmail extends BaseEmail {

    @ManyToOne
    @JoinColumn(nullable = false, name = "sender_id")
    private User sender;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    public SendEmail() {
        super();
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}