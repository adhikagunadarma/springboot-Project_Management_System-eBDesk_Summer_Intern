package ebdesk.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by asuss on 6/2/2017.
 */

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private int status;

    @NotNull
    private int price;

    @NotNull
    private int current;

    @NotNull
    private int size;

    @NotNull
    private String time;

    @NotNull
    private String start_time;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "project_skill", joinColumns = @JoinColumn(name = "id_project", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_skill", referencedColumnName = "id"))
    private Set<Skill> project_skills;

//    @ManyToMany(mappedBy = "projects")
//    private Set<User> users;

    @OneToMany(mappedBy = "primaryKey.project",cascade = CascadeType.ALL)
    private Set<UserProject> userProjects;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectLog> logs;

    public Project() {
    }


    public Project(String name, int status, int price, int current, int size, String time, String start_time, Set<Skill> project_skills, Set<UserProject> userProjects,Set<ProjectLog> logs) {
        this.name = name;
        this.status = status;
        this.price = price;
        this.current = current;
        this.size = size;
        this.time = time;
        this.start_time = start_time;
        this.project_skills = project_skills;
        this.userProjects = userProjects;
        this.logs = logs;
    }

    public Set<Skill> getProject_skills() {
        return project_skills;
    }

    public void setProject_skills(Set<Skill> project_skills) {
        this.project_skills = project_skills;
    }

    public Set<UserProject> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(Set<UserProject> userProjects) {
        this.userProjects = userProjects;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Set<ProjectLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<ProjectLog> logs) {
        this.logs = logs;
    }
}
