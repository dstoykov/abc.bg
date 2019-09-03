package dst.abc_bg.repositories;

import dst.abc_bg.entities.SendEmail;
import dst.abc_bg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SendEmailRepository extends JpaRepository<SendEmail, String> {
    Set<SendEmail> getAllBySenderAndDeletedOnNullOrderBySentOnDesc(User sender);
}
