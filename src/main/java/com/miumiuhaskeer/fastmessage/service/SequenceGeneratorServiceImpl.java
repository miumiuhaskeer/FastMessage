package com.miumiuhaskeer.fastmessage.service;

import com.miumiuhaskeer.fastmessage.model.entity.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    /**
     * Get next id number by sequenceName (should be const in Entity class for convenience)
     *
     * @param sequenceName name for Entity sequence generator
     * @return next id (minimum 1)
     */
    @Override
    public Long next(String sequenceName) {
        SequenceGenerator generator = getGenerator(sequenceName);

        if (generator == null) {
            createGenerator(sequenceName);
            generator = getGenerator(sequenceName);
        }

        return generator.getSequence();
    }

    private SequenceGenerator getGenerator(String sequenceName) {
        return mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("sequence", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                SequenceGenerator.class
        );
    }

    private void createGenerator(String sequenceName) {
        SequenceGenerator generator = new SequenceGenerator();

        generator.setId(sequenceName);
        generator.setSequence(0L);

        mongoOperations.save(generator);
    }
}
