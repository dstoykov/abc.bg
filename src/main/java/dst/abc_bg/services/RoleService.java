package dst.abc_bg.services;

import dst.abc_bg.entities.Role;

public interface RoleService {
    Role getUserRole();

    Role save(Role role);
}
