package dst.abc_bg.entities;

import javax.persistence.*;

@Entity
@Table(name = "receive_email")
public class ReceiveEmail extends BaseEmail {

    @Column(name = "sender", nullable = false)
    private String sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(name = "is_new", nullable = false)
    private Boolean isNew;

    public ReceiveEmail() {
        super();
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return this.recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public Boolean getNew() {
        return this.isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }
}