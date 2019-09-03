package dst.abc_bg.repositories;

import dst.abc_bg.entities.ReceiveEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReceiveEmailRepository extends JpaRepository<ReceiveEmail, String> {
    Set<ReceiveEmail> getAllByDeletedOnNull();
}
