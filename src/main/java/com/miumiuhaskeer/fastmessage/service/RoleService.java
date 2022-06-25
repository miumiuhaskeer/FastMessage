package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.ERole;
import com.miumiuhaskeer.fastmessage.model.entity.Role;

public interface RoleService {

    /** {@inheritDoc} */
    Role findByName(ERole eRole);

    /** {@inheritDoc} */
    boolean isRoleExist(ERole eRole);
}
