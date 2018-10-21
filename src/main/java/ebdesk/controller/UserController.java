package ebdesk.controller;

import ebdesk.model.User;
import ebdesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by asuss on 5/26/2017.
 */
@Controller
@Scope("session")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/viewAllEmployees",method = RequestMethod.GET)
    public String viewAllEmployees(Model m,HttpSession s){
        return userService.viewAllEmployees(m,s);
    }

    @RequestMapping(value = "/viewAllEmployeesAssignment",method = RequestMethod.GET)
    public String viewAllEmployeesAssignment(Model m,HttpSession s){
        return userService.viewEmployeesAssignment(m,s);
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return userService.viewLogin();
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String tologin(@RequestParam String email, @RequestParam String password, Model m,HttpSession s){
        System.out.println("PAssword nya = "+password);
        return userService.goLogin(email,password,m,s);
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String getRegister(){
        return userService.viewRegister();
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String postRegister(Model m ,@RequestParam String email,  @RequestParam String password,@RequestParam String first_name,@RequestParam String last_name,@RequestParam String phone){
        User u = new User(email,password,first_name,last_name,phone,3,"halo saya "+first_name);
        return  userService.goRegister(u,m);
    }

    @RequestMapping(value = "/viewEmployee/{id_user}",method = RequestMethod.GET)
    public String viewEmployee(@PathVariable String id_user, Model m, HttpSession s){
        int id = Integer.parseInt(id_user);
        System.out.println("ID USER = "+id);
        return userService.viewEmployee(m,id,s);
    }

    @RequestMapping(value = "/searchUser",method = RequestMethod.GET)
    public String searchUser(Model m, HttpSession s,@RequestParam String searchname ,@RequestParam String searchcategory){
        return userService.searchUser(m,s,searchname,searchcategory);
    }

    @RequestMapping(value = "/viewEmployee/{id_user}",method = RequestMethod.POST)
    public String editUser(@PathVariable String id_user, Model m,@RequestParam String first_name,@RequestParam String last_name,@RequestParam String phone,@RequestParam String aboutme,@RequestParam String line_id,HttpSession s){
        int id = Integer.parseInt(id_user);
        System.out.println("ID USER POST = "+id);
        User u = new User();
        return userService.editUser(u,m,first_name,last_name,phone,aboutme,line_id,s,id);
    }

    @RequestMapping(value = "/updateUserSkill/{id_user}",method = RequestMethod.POST)
    public String updateProjectSkills(Model m, HttpSession s,@PathVariable String id_user, @RequestParam int idskill){
        int user_id = Integer.parseInt(id_user);

        return userService.updateUserSkill(m,s,user_id,idskill);
    }

    @RequestMapping(value = "/searchSkillInUser/{id_user}",method = RequestMethod.GET)
    public String searchSkillInProject(Model m, HttpSession s,@RequestParam String searchname,@PathVariable String id_user){
        int project_id = Integer.parseInt(id_user);

        return userService.searchSkillInUser(m,s,searchname,project_id);
    }

    @RequestMapping(value = "/searchDivisionInUser/{id_user}",method = RequestMethod.GET)
    public String searchDivisionInProject(Model m, HttpSession s,@RequestParam String searchname,@PathVariable String id_user){
        int project_id = Integer.parseInt(id_user);

        return userService.searchDivisionInUser(m,s,searchname,project_id);
    }

    @RequestMapping(value = "/deleteSkillFromUser/{id_user}",method = RequestMethod.POST)
    public String deleteProjectSkill(Model m, HttpSession s,@PathVariable String id_user, @RequestParam String id_skill){
        int user_id = Integer.parseInt(id_user);
        int skill_id = Integer.parseInt(id_skill);

        return userService.deleteUserSkill(m,s,user_id,skill_id);
    }

    @RequestMapping(value = "/updateUserDivision/{id_user}",method = RequestMethod.POST)
    public String updateUserDivision(Model m, HttpSession s,@PathVariable String id_user, @RequestParam int id_division){
        int user_id = Integer.parseInt(id_user);

        return userService.updateUserDivision(m,s,user_id,id_division);
    }



}
