package votingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import votingsystem.domain.Moderator;
import votingsystem.domain.Poll;

import java.util.*;

/**
 * Created by ASHU on 02-03-2015.
 */
@Service
public class PollServiceImpl implements PollService{

/*    @Autowired
    ModeratorServiceImpl moderatorService;*/

    @Autowired
    ModeratorRepository modRepository;

    @Override
    public Poll createPoll(Integer id, Poll poll) {
        String newId = generateAlphaNumAuthCode();
        poll.setId(newId);
        Moderator moderator = modRepository.findOne(id); //moderatorService.getModerators().get(id);
        moderator.setPoll(newId, poll);
        modRepository.save(moderator);
        return moderator.getPoll(newId);
    }

    @Override
    public Poll viewPollWithoutResult(String pollId) {
        Moderator moderatorTemp = null;
        Poll pollTemp = null;
        List<Moderator> lstModerators = modRepository.findAll();
/*        Set<Map.Entry<Integer, Moderator>> entrySet = //moderatorService.getModerators().entrySet();
        for(Map.Entry entry: entrySet)
        {
            moderatorTemp = (Moderator) entry.getValue();
            pollTemp = moderatorTemp.getPoll(pollId);
            if(moderatorTemp.getPoll(pollId).getId().equals(pollId))
            {
                break;
            }
        }*/
        Iterator<Moderator> itr = lstModerators.iterator();
        while(itr.hasNext())
        {
            moderatorTemp = (Moderator) itr.next();
            pollTemp = moderatorTemp.getPoll(pollId);
            if(pollTemp.getId().equals(pollId))
            {
                break;
            }
        }
        return pollTemp;
    }

    @Override
    public Poll viewPoll(Integer modId, String pollId) {
        System.out.println("In Service Impl: ModId->"+ modId +", pollId->"+ pollId);
        Moderator moderatorTemp  = modRepository.findOne(modId);//moderatorService.getModerators().get(modId);
        System.out.println(moderatorTemp);
        return moderatorTemp.getPoll(pollId);
    }

    @Override
    public List<Poll> listAllPolls(Integer modId) {
        Moderator moderatorTemp = modRepository.findOne(modId);//moderatorService.getModerators().get(modId);
        Collection<Poll> map = moderatorTemp.getAllPolls();
        List<Poll> listPoll = new ArrayList<Poll>();
        Iterator<Poll> itrPoll = map.iterator();
        while(itrPoll.hasNext())
        {
            listPoll.add(itrPoll.next());
        }
        return listPoll;
    }

    @Override
    public void deletePoll(int modId, String pollId) {
        Moderator moderatorTemp = modRepository.findOne(modId);//moderatorService.getModerators().get(modId);
        moderatorTemp.deletePoll(pollId);
        modRepository.save(moderatorTemp);
    }

    @Override
    public void voteAPoll(String pollId, int choice) {
        System.out.println("pollId "+pollId + " "+choice);
        Moderator mod=null;
        Poll returnPoll=null;
        List<Moderator> lstModerators = modRepository.findAll();
        Iterator<Moderator> itr = lstModerators.iterator();
        while(itr.hasNext())
        {
            mod = itr.next();
            returnPoll = mod.getPoll(pollId);
            if(returnPoll.getId().equals(pollId))
            {
                break;
            }
        }
        int[] val = returnPoll.getResults();
        if(null != val) {
            val[choice]++;
            returnPoll.setResults(val);
            mod.setPoll(pollId, returnPoll);
            modRepository.save(mod);
        }
    }

    public String generateAlphaNumAuthCode() {

        long range = 123456789L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);
        String thirteenAsBase36 = Long.toString(number, 36).toUpperCase();

        return thirteenAsBase36;

    }
}
