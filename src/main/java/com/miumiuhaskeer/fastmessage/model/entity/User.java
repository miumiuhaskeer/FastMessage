package com.miumiuhaskeer.fastmessage.model.entity;

import com.miumiuhaskeer.fastmessage.annotation.Password;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "fm_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "fm_user_id_seq", allocationSize = 1)
    private Long id;

    @Email
    @Size(max = 50)
    private String email;

    @Password
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "fm_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDateTime;

    public User() {
    }

    public User(
            @Email
            @Size(max = 50) String email,
            @Password String password
    ) {
        this.email = email;
        this.password = password;
    }
}
