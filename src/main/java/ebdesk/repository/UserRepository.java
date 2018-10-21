package ebdesk.repository;

import ebdesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by asuss on 5/27/2017.
 */
public interface UserRepository  extends JpaRepository<User, Integer> {
    public User findByEmail(String email);
    public List<User> findAllByRole(int role);

    @Query(value="SELECT * FROM USER u JOIN USER_PROJECT up ON u.`id`=up.`id_user` JOIN PROJECT p ON up.`id_project`=p.`id`WHERE p.id=?1",nativeQuery=true)
    public List<User> findAllByProjectsId(int id);

    @Query(value="SELECT CONCAT(FIRST_NAME , ' ' ,LAST_NAME) AS NAME FROM USER u  JOIN USER_PROJECT up ON u.id = up.id_user  JOIN PROJECT p ON up.id_project = p.id WHERE u.role= ?1 AND p.id = ?2",nativeQuery=true)
    public List<User> findAllByRoleAndProjectId(@Param("role") int role,@Param("idProject")  int idProject);

    @Query(value=" SELECT * FROM USER u LEFT JOIN USER_PROJECT up ON u.id = up.id_user WHERE u.role =?1 AND NOT EXISTS (SELECT * FROM USER_PROJECT up WHERE u.id = up.`id_user` AND up.id_project=?2)",nativeQuery=true)
    public List<User> findAllByRoleNotExistInUserProject(int role,int projectID);

    //buat cari leader
    @Query(value="SELECT * FROM USER u  JOIN USER_PROJECT up ON u.id = up.id_user  JOIN PROJECT p ON up.id_project = p.id WHERE up.roles= ?1 AND p.id = ?2",nativeQuery=true)
    public User findByUserProjectRoleAndProjectId(@Param("roles") int role,@Param("idProject")  int idProject);

    //buat cari employee assignment
    @Query(value = "SELECT * FROM USER u WHERE u.`ongoingproject`>0",nativeQuery = true)
    public List<User>  findAllEmployeeAssignment();

    //buat cari id_project employee assignment
    @Query(value = "SELECT id_project FROM user_project up JOIN project p ON p.`id`=up.`id_project` WHERE id_user=?1 AND p.`status`=1",nativeQuery = true)
    public List<Integer>  findAllEmployeeAssignmentId(int id_user);

    //cari namanya
    @Query(value = "SELECT NAME FROM user_project up JOIN project p ON p.`id`=up.`id_project` WHERE id_user=?1 AND p.`status`=1",nativeQuery = true)
    public List<String>  findAllEmployeeAssignmentName(int id_user);

    //buat cari id_user user_project
    @Query(value = "SELECT id_user FROM user_project ",nativeQuery = true)
    public List<Integer>  findAllUserIdEmpAssign();

    //buat cari id_user user_project
    @Query(value = "SELECT name FROM project WHERE id=?1",nativeQuery = true)
    public String  findNameProject(int id_project);



    //--------------- PROFILE -------------------------

    //buat cari id skill employee
    @Query(value="SELECT id_skill FROM user_skill us JOIN USER u ON u.`id`=us.`id_user` WHERE id_user=?1",nativeQuery = true)
    public List<Integer>  findEmployeeSkillId(int id_user);

    //buat cari nama skill employee
    @Query(value="SELECT NAME FROM skill WHERE id=?1",nativeQuery = true)
    public String  findEmployeeSkillName(int id_skill);

    @Query(value="SELECT * FROM USER WHERE id=?1",nativeQuery = true)
    public User findUserById(int id_user);



    //buat cari role employee
    @Query(value="SELECT role FROM user WHERE id=?1",nativeQuery = true)
    public Integer findEmployeeRole(int id_user);

    //search function
    @Query(value="SELECT * FROM USER WHERE FIRST_NAME LIKE ?1",nativeQuery=true)
    public List<User> findAllBySearchFirstName(String fname);

    @Query(value="SELECT * FROM USER WHERE LAST_NAME LIKE ?1",nativeQuery=true)
    public List<User> findAllBySearchLastName(String lname);

    @Query(value="SELECT * FROM USER WHERE EMAIL LIKE ?1",nativeQuery=true)
    public List<User> findAllBySearchEmail(String email);

    @Query(value="SELECT * FROM USER WHERE PHONE LIKE ?1",nativeQuery=true)
    public List<User> findAllBySearchPhone(String phone);

    @Query(value="SELECT * FROM USER WHERE TOTALSTAR/TOTALPROJECT LIKE ?1",nativeQuery=true)
    public List<User> findAllBySearchRate(String rate);

    @Query(value="SELECT * FROM USER u JOIN DIVISION d WHERE u.division_id=d.ID AND d.`name` LIKE ?1",nativeQuery=true)
    public List<User> findAllBySearchDivision(String dname);

    //search user in project function

    @Query(value="SELECT * FROM USER WHERE ROLE =3 AND FIRST_NAME LIKE ?1",nativeQuery=true)
    public List<User> findAllByRoleSearchFirstName(String fname);

    @Query(value="SELECT * FROM USER WHERE ROLE =3 AND LAST_NAME LIKE ?1",nativeQuery=true)
    public List<User> findAllByRoleSearchLastName(String lname);

    @Query(value="SELECT * FROM USER WHERE ROLE =3 AND EMAIL LIKE ?1",nativeQuery=true)
    public List<User> findAllByRoleSearchEmail(String email);

    @Query(value="SELECT * FROM USER WHERE ROLE =3 AND PHONE LIKE ?1",nativeQuery=true)
    public List<User> findAllByRoleSearchPhone(String phone);

    @Query(value="SELECT * FROM USER WHERE ROLE =3 AND TOTALSTAR/TOTALPROJECT LIKE ?1",nativeQuery=true)
    public List<User> findAllByRoleSearchRate(String rate);

    @Query(value="SELECT * FROM USER u JOIN DIVISION d WHERE ROLE =3 AND u.division_id=d.ID AND d.`name` LIKE ?1",nativeQuery=true)
    public List<User> findAllByRoleSearchDivision(String dname);



}
