package ebdesk.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by asuss on 7/13/2017.
 */
@Entity
@Table(name = "project_log")
public class ProjectLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private int status;
    //1=create-project, 2 = add-member, 3 = remove-member, 4 = edit-project + leader-change,5 = add-skill, 6 = remove-skill, 7= finish project, 8= remove project


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @NotNull
    private String date;

    public ProjectLog() {
    }

    public ProjectLog(int status, User user, Project project, Skill skill, String start_time) {
        this.status = status;
        this.user = user;
        this.project = project;
        this.skill = skill;
        this.date = start_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
