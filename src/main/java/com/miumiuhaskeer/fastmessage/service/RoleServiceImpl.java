package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.ERole;
import com.miumiuhaskeer.fastmessage.model.entity.Role;
import com.miumiuhaskeer.fastmessage.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * Find role by enum ERole
     *
     * @param eRole role to found
     * @return entity role
     * @throws EntityNotFoundException if role not found
     */
    @Override
    public Role findByName(ERole eRole) {
        Optional<Role> role = roleRepository.findByName(eRole);

        if (!role.isPresent()) {
            throw new EntityNotFoundException();
        }

        return role.get();
    }

    @Override
    public boolean isRoleExist(ERole eRole) {
        return roleRepository.existsByName(eRole);
    }
}
