package votingsystem.controller;

/**
 * Created by ASHU on 21-02-2015.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import votingsystem.domain.Moderator;
import votingsystem.domain.Poll;
import votingsystem.service.ModeratorService;
import votingsystem.service.PollService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class MainController {

    @Autowired
    ModeratorService moderatorService;

    @Autowired
    PollService pollService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class BadRequestException extends IllegalArgumentException
    {
        public BadRequestException(String message)
        {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public class UnauthorizedException extends IllegalArgumentException
    {
        public UnauthorizedException(String message)
        {
            super(message);
        }
    }

    public boolean basicAuthentication(HttpServletRequest req)
    {
        String authorization = req.getHeader("Authorization");
        if(authorization == null || authorization.equals(""))
            return false;
        String credentials = authorization.substring("Basic".length()).trim();
        byte[] decoded = DatatypeConverter.parseBase64Binary(credentials);
        String decodedString = new String(decoded);
        String[] actualCredentials = decodedString.split(":");
        String ID = actualCredentials[0];
        String Password = actualCredentials[1];
        return (ID.equals("foo") && Password.equals("bar"));
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public String homePage(){
        return "Voting App Home Page";
    }

    @RequestMapping(value ="/moderators", method=RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Moderator createModerator (@Valid @RequestBody Moderator moderator, HttpServletRequest request, BindingResult result) {
        if(moderator.getName() == null || moderator.getName().equals(""))
            throw new BadRequestException("Name cannot be Empty/Null!");
        if(result.hasErrors())
            throw new BadRequestException("Request Parameters cannot be Empty!");
        return moderatorService.saveModerator(moderator);
    }

    @RequestMapping(value ="/moderators/{moderator_id}", method=RequestMethod.GET)
    public @ResponseBody Moderator viewModerator (@PathVariable("moderator_id") Integer id, HttpServletRequest request) {
        if(!basicAuthentication(request))
            throw new UnauthorizedException("Authentication Failed!");
        return moderatorService.viewModerator(id);
    }

    @RequestMapping(value ="/moderators/{moderator_id}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Moderator updateModerator (@Valid @PathVariable("moderator_id") Integer id,@RequestBody Moderator moderator, HttpServletRequest request) {
        if(!basicAuthentication(request))
            throw new UnauthorizedException("Authentication Failed!");
        Moderator moderatorTemp = null;
        try{
            moderatorTemp = moderatorService.updateModerator(id, moderator);
        }catch (NullPointerException e)
        {
            throw new BadRequestException("Id does not exist!");
        }
        return moderatorTemp;
    }

    @RequestMapping(value ="/moderators/{moderator_id}/polls", method=RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Map<String, Object> createPoll (@Valid @PathVariable("moderator_id") Integer id,@RequestBody Poll poll) {
        Map<String, Object> pollMap = new LinkedHashMap<String, Object>();
        try {
            Poll pollTemp = pollService.createPoll(id, poll);
            pollMap.put("id", pollTemp.getId());
            pollMap.put("question", pollTemp.getQuestion());
            pollMap.put("started_at", pollTemp.getStarted_at());
            pollMap.put("expired_at", pollTemp.getExpired_at());
            pollMap.put("choice", pollTemp.getChoice());
        }
        catch (NullPointerException e)
        {
            throw new BadRequestException("Id does not exist!");
        }
        return pollMap;
    }

    //view Poll without result
    @RequestMapping(value="/polls/{poll_id}", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> viewPollWithoutResult(@PathVariable("poll_id") String pollId)
    {
        Map<String, Object> pollMap = new LinkedHashMap<String, Object>();
        try {
            Poll poll = pollService.viewPollWithoutResult(pollId);
            pollMap.put("id", poll.getId());
            pollMap.put("question", poll.getQuestion());
            pollMap.put("started_at", poll.getStarted_at());
            pollMap.put("expired_at", poll.getExpired_at());
            pollMap.put("choice", poll.getChoice());
        }
        catch (NullPointerException e)
        {
            throw new BadRequestException("Id does not exist");
        }
        return pollMap;
    }

    @RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.GET)
    public @ResponseBody Poll viewPoll(@PathVariable("moderator_id") Integer modId, @PathVariable("poll_id") String pollId, HttpServletRequest request)
    {
        if(!basicAuthentication(request))
            throw new UnauthorizedException("Authentication Failed!");
        return pollService.viewPoll(modId, pollId);
    }

    @RequestMapping(value="/moderators/{moderator_id}/polls", method = RequestMethod.GET)
    public @ResponseBody List<Poll> listAllPolls(@PathVariable("moderator_id") Integer modId, HttpServletRequest request)
    {
        if(!basicAuthentication(request))
            throw new UnauthorizedException("Authentication Failed!");
        return pollService.listAllPolls(modId);
    }

    @RequestMapping(value="/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoll(@PathVariable("moderator_id") int modId, @PathVariable("poll_id") String pollId)
    {
        try {
            pollService.deletePoll(modId, pollId);
        }
        catch (NullPointerException e)
        {
            throw new BadRequestException("Id does not exist");
        }
    }

    @RequestMapping(value="/polls/{poll_id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void voteAPoll(@Valid @PathVariable("poll_id") String pollId, @RequestParam(value="choice") int choice)
    {
        try {
            pollService.voteAPoll(pollId, choice);
        }
        catch (NullPointerException e)
        {
            throw new BadRequestException("Id does not exist");
        }
    }
}
