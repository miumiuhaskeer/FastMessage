package com.miumiuhaskeer.fastmessage.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "fm_refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "fm_refresh_token_id_seq", allocationSize = 1)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDateTime;
}
