package ebdesk.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by asuss on 6/6/2017.
 */

@Embeddable
public class UserProjectId implements Serializable{
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Project project;



    public UserProjectId() {
    }

    public UserProjectId(User user, Project project) {
        this.user = user;
        this.project = project;
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
}
