package ebdesk.service;

import ebdesk.model.Project;
import ebdesk.model.Skill;
import ebdesk.model.User;
import ebdesk.repository.ProjectRepository;
import ebdesk.repository.SkillRepository;
import ebdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.persistence.PreRemove;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asuss on 6/19/2017.
 */
@Service
public class SkillService {

    @Autowired
    SkillRepository skillRepo;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    UserRepository userRepo;

    public String viewAllSkills(Model model, HttpSession session) {
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));

        return "skills/view_skills";
    }

    public String addNewSkill(Model model, HttpSession session, Skill skill) {
        skillRepo.save(skill);
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));

        return "skills/view_skills";
    }

    public String deleteSkill(Model model, HttpSession session, String id_skill) {

        Skill s = skillRepo.findOne(Integer.parseInt(id_skill));
        for (Project p : s.getProjects()){
            if(p!=null){
                p.getProject_skills().remove(s);
                projectRepo.save(p);
            }

        }

        for (User u : s.getUsers()){
            if(u!=null){
                u.getUser_skills().remove(s);
                userRepo.save(u);
            }
        }

        skillRepo.delete(Integer.parseInt(id_skill));

        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));

        return "skills/view_skills";
    }


    public String searchSkill(Model model, HttpSession session, String searchname) {


        model.addAttribute("skills",skillRepo.findAllBySearchName("%"+searchname+"%"));
        model.addAttribute("user",session.getAttribute("user"));

        return "skills/view_skills";

    }
}
