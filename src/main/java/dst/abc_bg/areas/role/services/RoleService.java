package dst.abc_bg.areas.role.services;

import dst.abc_bg.areas.role.entities.Role;
import dst.abc_bg.areas.user.entities.User;

public interface RoleService {
    Role getUserRole();

    Role save(Role role);

    Role addUserToRole(User user, Role role);
}
