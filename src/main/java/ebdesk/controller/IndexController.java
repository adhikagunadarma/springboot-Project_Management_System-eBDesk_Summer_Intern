package ebdesk.controller;

import ebdesk.repository.ProjectLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by asuss on 5/26/2017.
 */
@Controller
public class IndexController {

    @Autowired
    ProjectLogRepository prologRepo;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Model model, HttpSession session){

        model.addAttribute("logs",prologRepo.findAllByOrderByIdDesc());

        model.addAttribute("user",session.getAttribute("user"));
        return "index/index";

    }


}
