package dst.abc_bg.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "seder_id")
    private User sender;

    @ManyToOne
    @JoinColumn(nullable = false, name = "recipient_id")
    private User recipient;

    @Column(name = "sent_on", nullable = false)
    private LocalDateTime sentOn;

    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;

    public Email() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return this.recipient;
    }

    public void setRecipient(User recipient) {
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
