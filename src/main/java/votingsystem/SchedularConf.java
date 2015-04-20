package votingsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import votingsystem.service.ModeratorService;

/**
 * Created by ASHU on 15-04-2015.
 */

@Component
public class SchedularConf {
    @Autowired
    ModeratorService moderatorService;

    @Scheduled(fixedRate = 300000)
    public void messageScheduler()
    {
        System.out.println("Scheduler Started");
        moderatorService.checkForExpiredPolls();
    }
}
