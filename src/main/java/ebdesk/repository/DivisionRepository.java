package ebdesk.repository;

import ebdesk.model.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by asuss on 6/4/2017.
 */
public interface DivisionRepository extends JpaRepository<Division,Integer> {

    @Query(value="SELECT * FROM DIVISION WHERE NAME LIKE ?1",nativeQuery=true)
    public List<Division> findAllBySearchName(String name);

}
