package dst.abc_bg.services.impl;

import dst.abc_bg.entities.Role;
import dst.abc_bg.repositories.RoleRepository;
import dst.abc_bg.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private static final String USER_ROLE = "USER";

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private Role getRoleByAuthority(String authority) {
        return this.roleRepository.findByAuthority(authority);
    }

    @Override
    public Role getUserRole() {
        return this.getRoleByAuthority(USER_ROLE);
    }

    @Override
    public Role save(Role role) {
        this.roleRepository.save(role);
        return role;
    }
}
