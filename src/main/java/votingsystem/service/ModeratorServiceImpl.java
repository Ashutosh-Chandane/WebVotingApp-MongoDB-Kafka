package votingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import votingsystem.domain.Moderator;
import votingsystem.domain.Poll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ASHU on 21-02-2015.
 */
@Service
public class ModeratorServiceImpl implements ModeratorService {

    @Autowired
    ModeratorRepository modRepository;

    @Autowired
    KafkaProducer producerObj;

    AtomicInteger seqModerator = new AtomicInteger();
    Map<Integer, Moderator> moderators = new HashMap<Integer, Moderator>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public Moderator saveModerator(final Moderator moderator) {
        int sequence = seqModerator.incrementAndGet();
        moderator.setId(sequence);
        moderator.setCreated_at(dateFormat.format(new Date()));
        modRepository.save(moderator);
        System.out.print(moderator);
        return moderator;
    }

    @Override
    public Moderator viewModerator(Integer id) {
        //return moderators.get(id);
        return modRepository.findOne(id);
    }

    @Override
    public Moderator updateModerator(Integer id, Moderator moderator) {
        Moderator moderatorTemp = modRepository.findOne(id);//moderators.get(id);
        if(moderator.getEmail() != null && !"".equals(moderator.getEmail()))
        {
            moderatorTemp.setEmail(moderator.getEmail());
        }
        if(null!=moderator.getPassword() && !"".equals(moderator.getPassword()))
        {
            moderatorTemp.setPassword(moderator.getPassword());
        }
        if(null!=moderator.getName() && !"".equals(moderator.getName()))
        {
            moderatorTemp.setName(moderator.getName());
        }
        //moderators.put(id,moderatorTemp);
        modRepository.save(moderatorTemp);
        return moderatorTemp;
    }

    @Override
    public void checkForExpiredPolls() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Moderator> modLst = modRepository.findAll();
        Date dbDate = null;
        Date currentDate = new Date();
        for(Moderator mod: modLst)
        {
            for(Poll poll: mod.getAllPolls())
            {
                String expiredDate = poll.getExpired_at();
                try {
                    dbDate = sdf.parse(expiredDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(currentDate.compareTo(dbDate) == 1)
                {
                    System.out.print("\nPoll Expired of Moderator "+mod.getEmail());
                    if(!poll.isEmailSent()) {
                        producerObj.sendExpiredMessage(mod.getEmail(), poll.getResults());
                        poll.setEmailSent(true);
                        mod.setPoll(poll.getId(), poll);
                        modRepository.save(mod);
                    }
                }

            }
        }
    }

    public Map<Integer, Moderator> getModerators() {
        return moderators;
    }

}
