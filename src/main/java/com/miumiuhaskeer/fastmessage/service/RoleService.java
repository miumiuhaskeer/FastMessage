package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.ERole;
import com.miumiuhaskeer.fastmessage.model.entity.Role;

public interface RoleService {
    Role findByName(ERole eRole);
    boolean isRoleExist(ERole eRole);
}
