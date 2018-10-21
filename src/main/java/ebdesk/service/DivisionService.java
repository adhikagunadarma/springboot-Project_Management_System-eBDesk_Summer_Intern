package ebdesk.service;

import ebdesk.model.Division;
import ebdesk.model.Project;
import ebdesk.model.User;
import ebdesk.repository.DivisionRepository;
import ebdesk.repository.ProjectRepository;
import ebdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

/**
 * Created by asuss on 6/19/2017.
 */
@Service
public class DivisionService {

    @Autowired
    DivisionRepository divRepo;

    @Autowired
    UserRepository userRepo;

    public String viewAllDivisions(Model model, HttpSession session) {
        model.addAttribute("divisions",divRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));

        return "divisions/view_divisions";
    }

    public String addNewDivision(Model model, HttpSession session, Division division) {
        divRepo.save(division);
        model.addAttribute("divisions",divRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));

        return "divisions/view_divisions";
    }

    public String deleteDivision(Model model, HttpSession session, String id_div) {

        Division d = divRepo.findOne(Integer.parseInt(id_div));
        for (User u : d.getUsers()){
            if(u!=null){
                u.setDivision(null);
                userRepo.save(u);
            }
        }
        d.setUsers(null);
        divRepo.delete(d);

        model.addAttribute("divisions",divRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));

        return "divisions/view_divisions";
    }


    public String searchDivision(Model model, HttpSession session, String searchname) {


        model.addAttribute("divisions",divRepo.findAllBySearchName("%"+searchname+"%"));
        model.addAttribute("user",session.getAttribute("user"));

        return "divisions/view_divisions";
    }
}
