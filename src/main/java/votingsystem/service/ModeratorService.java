package votingsystem.service;

import votingsystem.domain.Moderator;
import votingsystem.domain.Poll;

/**
 * Created by ASHU on 21-02-2015.
 */
public interface ModeratorService {

    Moderator saveModerator(Moderator moderator);

    Moderator viewModerator(Integer id);

    Moderator updateModerator(Integer id, Moderator moderator);

    void checkForExpiredPolls();

   /* Poll savePoll(Integer id, Poll poll);*/
}
