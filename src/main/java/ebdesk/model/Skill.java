package ebdesk.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by asuss on 6/2/2017.
 */

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "user_skills")
    private Set<User> users;

    @ManyToMany(mappedBy = "project_skills")
    private Set<Project> projects;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private Set<ProjectLog> logs;

    public Skill() {
    }

    public Skill(String name, Set<User> users, Set<Project> projects, Set<ProjectLog> logs) {
        this.name = name;
        this.users = users;
        this.projects = projects;
        this.logs = logs;
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

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<ProjectLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<ProjectLog> logs) {
        this.logs = logs;
    }
}
