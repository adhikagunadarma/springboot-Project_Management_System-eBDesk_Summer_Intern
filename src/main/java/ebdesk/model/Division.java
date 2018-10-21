package ebdesk.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by asuss on 6/2/2017.
 */

@Entity
@Table(name = "division")
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    private Set<User> users;

    public Division() {
    }

    public Division(String name, Set<User> users) {
        this.name = name;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
