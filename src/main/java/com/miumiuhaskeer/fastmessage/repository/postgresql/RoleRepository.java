package com.miumiuhaskeer.fastmessage.repository.postgresql;

import com.miumiuhaskeer.fastmessage.model.entity.ERole;
import com.miumiuhaskeer.fastmessage.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}
