package votingsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;


/**
 * Created by ASHU on 21-02-2015.
 */
public class Poll {
    private String id;
    private String question;
    private String started_at;
    private String expired_at;
    private String[] choice;
    private int[] results;
    @JsonIgnore
    boolean emailSent=false;

    Poll()
    {
        results = new int[2];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int[] getResults() {
        return results;
    }

    public void setResults(int[] results) {
        this.results = results;
    }

    public String[] getChoice() {
        return choice;
    }

    public void setChoice(String[] choice) {
        this.choice = choice;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", started_at='" + started_at + '\'' +
                ", expired_at='" + expired_at + '\'' +
                ", choice=" + Arrays.toString(choice) +
                ", results=" + Arrays.toString(results) +
                ", emailSent=" + emailSent +
                '}';
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }
}