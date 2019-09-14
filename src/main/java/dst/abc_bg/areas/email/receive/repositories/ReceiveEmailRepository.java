package dst.abc_bg.areas.email.receive.repositories;

import dst.abc_bg.areas.email.receive.entities.ReceiveEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReceiveEmailRepository extends JpaRepository<ReceiveEmail, String> {
    @Query("SELECT e FROM ReceiveEmail e WHERE e.recipient.username = ?1 AND e.deletedOn IS null ORDER BY e.sentOn DESC")
    Set<ReceiveEmail> getAllByRecipientAndDeletedOnNull(String username);

    @Query("SELECT e FROM ReceiveEmail e WHERE e.id = ?1 AND e.recipient.username = ?2 AND e.deletedOn IS NULL")
    ReceiveEmail getByIdRecipientAndDeletedOnNull(String id, String username);

    Set<ReceiveEmail> findAllByIdNotNullOrderBySentOnDesc();
}
