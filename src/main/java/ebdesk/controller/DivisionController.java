package ebdesk.controller;

import ebdesk.model.Division;
import ebdesk.model.Skill;
import ebdesk.service.DivisionService;
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
public class DivisionController {

    @Autowired
    DivisionService divisionService;

    @RequestMapping(value = "/viewAllDivisions",method = RequestMethod.GET)
    public String viewAllDivisions(Model m, HttpSession s) {

        return divisionService.viewAllDivisions(m,s);
    }

    @RequestMapping(value = "/addNewDivision",method = RequestMethod.POST)
    public String addNewDivision(Model m, HttpSession s,@RequestParam String divisionname) {

        Division division = new Division();
        division.setName(divisionname);
        return divisionService.addNewDivision(m,s,division);
    }

    @RequestMapping(value = "/deleteDivision/{id_division}",method = RequestMethod.POST)
    public String deleteDivision(Model m, HttpSession s,@PathVariable String id_division) {

        return divisionService.deleteDivision(m,s,id_division);
    }
    @RequestMapping(value = "/searchDivision",method = RequestMethod.GET)
    public String searchDivision(Model m, HttpSession s,@RequestParam String searchname){


        return divisionService.searchDivision(m,s,searchname);
    }
}
