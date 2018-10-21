package ebdesk.repository;

import ebdesk.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asuss on 6/4/2017.
 */
public interface SkillRepository extends JpaRepository<Skill,Integer> {

    public List<Skill> findAllByProjectsId(int id);

    public List<Skill> findAllByUsersId(int id);

    @Query(value="SELECT * FROM SKILL s LEFT JOIN PROJECT_SKILL ps ON s.id = ps.`id_skill` LEFT JOIN PROJECT p ON ps.`id_project`=p.`id` WHERE NOT EXISTS (SELECT * FROM PROJECT_SKILL ps WHERE s.id = ps.id_skill AND p.id = ps.id_project)",nativeQuery=true)
    public List<Skill> findAllNotExistInProjectSkills();

    @Query(value="SELECT * FROM SKILL WHERE NAME LIKE ?1",nativeQuery=true)
    public List<Skill> findAllBySearchName(String name);
}
