package ebdesk.repository;

import ebdesk.model.Project;
import ebdesk.model.ProjectLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asuss on 6/2/2017.
 */
public interface ProjectLogRepository extends JpaRepository<ProjectLog, Integer> {


    @Query(value="SELECT * FROM PROJECT_LOG WHERE PROJECT_ID = ?1 ORDER BY ID DESC",nativeQuery=true)
    public List<ProjectLog> findAllByProjectID(int id);

    public List<ProjectLog> findAllByOrderByIdDesc();

    public List<ProjectLog> findTop10ByOrderByIdDesc();




}
