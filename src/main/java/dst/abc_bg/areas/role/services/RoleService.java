package dst.abc_bg.areas.role.services;

import dst.abc_bg.areas.role.entities.Role;

public interface RoleService {
    Role getUserRole();

    Role save(Role role);
}
