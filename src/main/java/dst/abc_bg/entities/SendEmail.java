package dst.abc_bg.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "send_emails")
public class SendEmail {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "sender_id")
    private User sender;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "sent_on", nullable = false)
    private LocalDateTime sentOn;

    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;

    public SendEmail() {
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

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
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
