package ebdesk.controller;

import ebdesk.model.Skill;
import ebdesk.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by asuss on 6/19/2017.
 */
@Controller
public class SkillController {

    @Autowired
    SkillService skillService;

    @RequestMapping(value = "/viewAllSkills",method = RequestMethod.GET)
    public String viewAllSkills(Model m, HttpSession s) {

        return skillService.viewAllSkills(m,s);
    }

    @RequestMapping(value = "/addNewSkill",method = RequestMethod.POST)
    public String addNewSkill(Model m, HttpSession s,@RequestParam String skillname) {

        Skill skill = new Skill();
        skill.setName(skillname);
        return skillService.addNewSkill(m,s,skill);
    }

    @RequestMapping(value = "/deleteSkill/{id_skill}",method = RequestMethod.POST)
    public String deleteSkill(Model m, HttpSession s,@PathVariable String id_skill) {

        return skillService.deleteSkill(m,s,id_skill);
    }

    @RequestMapping(value = "/searchSkill",method = RequestMethod.GET)
    public String searchSkill(Model m, HttpSession s,@RequestParam String searchname){


        return skillService.searchSkill(m,s,searchname);
    }
}
