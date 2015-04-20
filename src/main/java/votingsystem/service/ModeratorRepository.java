package votingsystem.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import votingsystem.domain.Moderator;

/**
 * Created by ASHU on 29-03-2015.
 */
@Component
public interface ModeratorRepository extends MongoRepository<Moderator, Integer> {

    Moderator save(Moderator saved);
}
