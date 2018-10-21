package ebdesk.repository;

import ebdesk.model.Project;
import ebdesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asuss on 6/2/2017.
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query(value="SELECT * FROM PROJECT p JOIN USER_PROJECT up ON p.`id`=up.`id_project` JOIN USER u ON up.`id_user`=u.`id`WHERE u.id=?1",nativeQuery=true)
    public List<Project> findAllByUsersId(int id);

    //Search All Project
    @Query(value="SELECT * FROM PROJECT WHERE NAME LIKE ?1",nativeQuery=true)
    public ArrayList<Project> findAllBySearchName(String name);

    @Query(value="SELECT * FROM PROJECT WHERE START_TIME LIKE ?1",nativeQuery=true)
    public List<Project> findAllBySearchDate(String date);

    @Query(value="SELECT * FROM PROJECT WHERE SIZE LIKE ?1",nativeQuery=true)
    public List<Project> findAllBySearchSize(String size);

    @Query(value="SELECT * FROM PROJECT WHERE PRICE LIKE ?1",nativeQuery=true)
    public List<Project> findAllBySearchPrice(String price);

    @Query(value="SELECT * FROM PROJECT WHERE TIME LIKE ?1",nativeQuery=true)
    public List<Project> findAllBySearchTime(String time);

    public List<Project> findAllByStatus(int status);

    //Search Projects by User id

    @Query(value="SELECT * FROM PROJECT p JOIN USER_PROJECT up ON p.`id`=up.`id_project` JOIN USER u ON up.`id_user`=u.`id`WHERE u.id=?1 AND NAME LIKE ?2",nativeQuery=true)
    public ArrayList<Project> findAllByUserIdSearchName(int id,String name);

    @Query(value="SELECT * FROM PROJECT p JOIN USER_PROJECT up ON p.`id`=up.`id_project` JOIN USER u ON up.`id_user`=u.`id`WHERE u.id=?1 AND START_TIME LIKE ?2",nativeQuery=true)
    public List<Project> findAllByUserIdSearchDate(int id,String date);

    @Query(value="SELECT * FROM PROJECT p JOIN USER_PROJECT up ON p.`id`=up.`id_project` JOIN USER u ON up.`id_user`=u.`id`WHERE u.id=?1 AND SIZE LIKE ?2",nativeQuery=true)
    public List<Project> findAllByUserIdSearchSize(int id,String size);

    @Query(value="SELECT * FROM PROJECT p JOIN USER_PROJECT up ON p.`id`=up.`id_project` JOIN USER u ON up.`id_user`=u.`id`WHERE u.id=?1 AND PRICE LIKE ?2",nativeQuery=true)
    public List<Project> findAllByUserIdSearchPrice(int id,String price);

    @Query(value="SELECT * FROM PROJECT p JOIN USER_PROJECT up ON p.`id`=up.`id_project` JOIN USER u ON up.`id_user`=u.`id`WHERE u.id=?1 AND TIME LIKE ?2",nativeQuery=true)
    public List<Project> findAllByUserIdSearchTime(int id,String time);

    @Query(value="SELECT * FROM PROJECT p JOIN USER_PROJECT up ON p.`id`=up.`id_project` JOIN USER u ON up.`id_user`=u.`id`WHERE u.id=?1 AND STATUS = ?2",nativeQuery=true)
    public List<Project> findAllByUserIdStatus(int id,int status);



}
