package votingsystem.service;

import votingsystem.domain.Poll;

import java.util.List;

/**
 * Created by ASHU on 02-03-2015.
 */
public interface PollService {

    Poll createPoll(Integer id, Poll poll);

    Poll viewPollWithoutResult(String pollId);

    Poll viewPoll(Integer modId, String pollId);

    List<Poll> listAllPolls(Integer modId);

    void deletePoll(int modId, String pollId);

    void voteAPoll(String pollId, int choice);
}
