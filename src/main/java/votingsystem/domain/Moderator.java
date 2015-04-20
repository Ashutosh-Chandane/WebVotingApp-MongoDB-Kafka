package votingsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by ASHU on 21-02-2015.
 */
public class Moderator {
    private Integer id;

    private String name;

    @NotEmpty @Email(message="Invalid Email Format!")
    private String email;

    @NotEmpty @Size(min=2)
    @Length(min = 1, message = "Password is too short")
    private String password;

    private String created_at;

    private HashMap<String, Poll> polls;

    public Moderator()
    {
        polls = new HashMap<String, Poll>();
    }

    private Poll poll;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public Poll getPoll(String id) {
        return polls.get(id);
    }

    public void setPoll(String id, Poll poll) {
        polls.put(id, poll);
    }

    @JsonIgnore
    public Collection<Poll> getAllPolls() {
        return polls.values();
    }

    public void removePoll(String id) {
        polls.remove(id);
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void deletePoll(String id)
    {
        polls.remove(id);
    }

    @Override
    public String toString() {
        return "Moderator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created_at=" + created_at +
                ", poll=" + poll +
                '}';
    }
}
