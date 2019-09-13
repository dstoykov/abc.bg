package dst.abc_bg.tasks;

import dst.abc_bg.services.ReceiveEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailReceiveTask {
    private final ReceiveEmailService receiveEmailService;

    @Autowired
    public EmailReceiveTask(ReceiveEmailService receiveEmailService) {
        this.receiveEmailService = receiveEmailService;
    }

    @Scheduled(fixedRate = 60000)
    public void saveNewReceivedEmails() {
        try {
            this.receiveEmailService.receiveEmails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
