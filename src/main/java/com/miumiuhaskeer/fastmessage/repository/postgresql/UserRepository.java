package com.miumiuhaskeer.fastmessage.repository.postgresql;

import com.miumiuhaskeer.fastmessage.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    Integer countAllByEmailIn(Set<String> emails);
    boolean existsByEmail(String email);

    default boolean existsByEmails(Set<String> emails) {
        return countAllByEmailIn(emails).equals(emails.size());
    }

    Integer countAllByIdIn(Set<Long> ids);
    boolean existsById(Long id);

    default boolean existsByIds(Set<Long> ids) {
        return countAllByIdIn(ids).equals(ids.size());
    }
}
