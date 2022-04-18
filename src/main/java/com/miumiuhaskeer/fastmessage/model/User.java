package com.miumiuhaskeer.fastmessage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "public")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "id_seq", allocationSize = 1)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Builder.Default
    @Enumerated
    @Column(columnDefinition = "smallint")
    private Role role = Role.USER;

    @Builder.Default
    @Column(name = "is_locked")
    private boolean isLocked = false;

    @Builder.Default
    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @Column(name = "creation_datetime")
    private String creationDateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
