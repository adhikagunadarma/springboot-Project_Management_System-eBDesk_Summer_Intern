package ebdesk.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by asuss on 6/5/2017.
 */
@Entity
@Table(name = "user_project")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.user",
                joinColumns = @JoinColumn(name = "id_user")),
        @AssociationOverride(name = "primaryKey.project",
                joinColumns = @JoinColumn(name = "id_project")) })
public class UserProject implements Serializable{
    @EmbeddedId
    private UserProjectId primaryKey = new UserProjectId();
    @Column(name = "rate")
    private int rate;
    @Column(name = "roles")
    private int roles;

    public UserProject() {
    }

    public UserProject(UserProjectId primaryKey, int rate, int roles) {
        this.primaryKey = primaryKey;
        this.rate = rate;
        this.roles = roles;
    }

    public UserProjectId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(UserProjectId primaryKey) {
        this.primaryKey = primaryKey;
    }


    @Transient
    public User getUser() {
        return getPrimaryKey().getUser();
    }

    public void setUser(User user) {
        getPrimaryKey().setUser(user);
    }

    @Transient
    public Project getProject() {
        return getPrimaryKey().getProject();
    }

    public void setProject(Project project) {
        getPrimaryKey().setProject(project);
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRoles() {
        return roles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }
}
