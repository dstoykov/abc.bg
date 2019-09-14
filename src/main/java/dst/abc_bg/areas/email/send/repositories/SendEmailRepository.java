package dst.abc_bg.areas.email.send.repositories;

import dst.abc_bg.areas.email.send.entities.SendEmail;
import dst.abc_bg.areas.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SendEmailRepository extends JpaRepository<SendEmail, String> {
    Set<SendEmail> getAllBySenderAndDeletedOnNullOrderBySentOnDesc(User sender);

    SendEmail getByIdEqualsAndSenderEqualsAndDeletedOnNull(String id, User sender);

    Set<SendEmail> findAllByIdNotNullOrderBySentOnDesc();
}
