package com.miumiuhaskeer.fastmessage.repository.mongodb;

import com.miumiuhaskeer.fastmessage.model.entity.Chat;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query(value = "{ 'tags': { $all: ?0, $size: ?#{[0].size()} } }", exists = true)
    boolean existsByTags(List<Long> tags);

    @Aggregation(pipeline = {
            "{ '$match': { 'tags': { $all: ?0, $size: ?#{[0].size()} } } }",
            "{ '$limit': 1 }"
    })
    Optional<Chat> findFirstByTags(List<Long> tags);
}
