package dst.abc_bg.areas.user.repositories;

import dst.abc_bg.areas.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsernameEquals(String username);

    User findUserByUsernameAndDeletedOnNull(String username);

    User findByUsernameAndDeletedOnNotNull(String username);

    Set<User> getAllByUsernameNotOrderByDeletedOn(String username);

    @Override
    @Query("SELECT u FROM User u WHERE u.deletedOn IS NULL")
    List<User> findAll();

    @Override
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedOn IS NULL")
    User getOne(@Param("id") String id);
}
