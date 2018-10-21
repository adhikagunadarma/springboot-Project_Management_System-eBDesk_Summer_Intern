package ebdesk.controller;

import ebdesk.model.Project;
import ebdesk.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asuss on 5/26/2017.
 */
@Controller
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/viewAllProjects",method = RequestMethod.GET)
    public String viewAllProjects(Model m, HttpSession s) {

        return projectService.viewAllProjects(m,s);
    }

    @RequestMapping(value = "/viewProject/{id_project}",method = RequestMethod.GET)
    public String viewProject(@PathVariable String id_project,Model m,HttpSession s){
        int id = Integer.parseInt(id_project);
        return projectService.viewProject(m,id,s);
    }

    @RequestMapping(value = "/viewMyProjects/{id_user}",method = RequestMethod.GET)
    public String viewMyProjects(@PathVariable String id_user,Model m,HttpSession s){
        int id = Integer.parseInt(id_user);
        return projectService.viewMyProjects(m,id,s);
    }

    @RequestMapping(value = "/addNewProject",method = RequestMethod.GET)
    public String addNewProject(Model m, HttpSession s){
        return projectService.viewNewProject(m,s);
    }



    @RequestMapping(value = "/addNewProject",method = RequestMethod.POST)
    public String processNewProject(Model m, HttpSession s, @RequestParam String projectname ,@RequestParam int  projectsize, @RequestParam int projectprice,
                                    @RequestParam int projectwaktu_1, @RequestParam String projectwaktu_2, @RequestParam int projectidleader){

        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateobj = new Date();

        Project pro = new Project();
        pro.setPrice(projectprice);
        pro.setName(projectname);
        pro.setSize(projectsize);
        pro.setStatus(1);
        pro.setTime(projectwaktu_1 + " " + projectwaktu_2);
        pro.setStart_time(df.format(dateobj));

        return projectService.postNewProject(m,s,pro,projectidleader);
    }

    @RequestMapping(value = "/updateProjectSettings/{id_project}",method = RequestMethod.POST)
    public String updateProjectSettings(Model m, HttpSession s,@PathVariable String id_project, @RequestParam String projectname ,@RequestParam int  projectsize, @RequestParam int projectprice,
                                    @RequestParam int projectwaktu_1, @RequestParam String projectwaktu_2, @RequestParam int projectidleader, @RequestParam int projectidleaderbefore){
        int project_id = Integer.parseInt(id_project);
        return projectService.updateProjectSetting(m,s,project_id,projectprice,projectname,projectsize,projectwaktu_1,projectwaktu_2,projectidleader,projectidleaderbefore);
    }

    @RequestMapping(value = "/updateProjectSkills/{id_project}",method = RequestMethod.POST)
    public String updateProjectSkills(Model m, HttpSession s,@PathVariable String id_project, @RequestParam int id_skill){
        int project_id = Integer.parseInt(id_project);

        return projectService.updateProjectSkill(m,s,project_id,id_skill);
    }

    @RequestMapping(value = "/updateProjectMembers/{id_project}",method = RequestMethod.POST)
    public String updateProjectMembers(Model m, HttpSession s,@PathVariable String id_project, @RequestParam int id_user){
        int project_id = Integer.parseInt(id_project);

        return projectService.updateProjectMember(m,s,project_id,id_user);
    }

    @RequestMapping(value = "/deleteUserFromProject/{id_project}",method = RequestMethod.POST)
    public String deleteProjectMember(Model m, HttpSession s,@PathVariable String id_project, @RequestParam String id_user){
        int project_id = Integer.parseInt(id_project);
        int user_id = Integer.parseInt(id_user);

        return projectService.deleteProjectMember(m,s,project_id,user_id);
    }

    @RequestMapping(value = "/deleteSkillFromProject/{id_project}",method = RequestMethod.POST)
    public String deleteProjectSkill(Model m, HttpSession s,@PathVariable String id_project, @RequestParam String id_skill){
        int project_id = Integer.parseInt(id_project);
        int skill_id = Integer.parseInt(id_skill);

        return projectService.deleteProjectSkill(m,s,project_id,skill_id);
    }

    @RequestMapping(value = "/cancelProject/{id_project}",method = RequestMethod.POST)
    public String cancelProject(Model m, HttpSession s,@PathVariable String id_project){
        int project_id = Integer.parseInt(id_project);

        return projectService.cancelProject(m,s,project_id);
    }

    @RequestMapping(value = "/finishProject/{id_project}",method = RequestMethod.POST)
    public String finishProject(Model m, HttpSession s,@PathVariable String id_project){
        int project_id = Integer.parseInt(id_project);

        return projectService.finishProject(m,s,project_id);
    }

    @RequestMapping(value = "/updateUserRateProject",method = RequestMethod.POST)
    public String finishProject(Model m, HttpSession s, @RequestBody String a, @RequestBody String b, @RequestBody String c){

        String[] pecah = a.split("&");
        String[] value = null;
        int user_id=0,project_id=0,rate=0;
        for(int i = 0 ; i < pecah.length;i++){
//            System.out.println(pecah[i]);
            value = pecah[i].split("=");
            switch(i){
                case 0 :
                    user_id = Integer.parseInt(value[1]);//id_user
                    break;
                case 1 :
                    project_id = Integer.parseInt(value[1]);//id_project
                    break;
                case 2 :
                    rate = Integer.parseInt(value[1]);//ratingnya
                    break;
            }
//            for(int j = 0 ; j< value.length;j++){
//                System.out.println(j + " - "+value[j]);
//
//            }
        }

        System.out.println(user_id +"+"+project_id+"+"+rate);

        return projectService.updateUserRate(m,s,project_id,user_id,rate);
    }

    @RequestMapping(value = "/searchProject",method = RequestMethod.GET)
    public String searchProject(Model m, HttpSession s, @RequestParam String searchname , @RequestParam String searchcategory, @RequestParam String searchstatus){


        return projectService.searchProject(m,s,searchname,searchcategory,searchstatus);
    }

    @RequestMapping(value = "searchUserWhenAddNewProject",method = RequestMethod.GET)
    public String searchUserWhenAddNewProject(Model m, HttpSession s, @RequestParam String searchname , @RequestParam String searchcategory){


        return projectService.searchUserWhenAddNewProject(m,s,searchname,searchcategory);
    }

    @RequestMapping(value = "/searchUserInProject/{id_project}",method = RequestMethod.GET)
    public String searchUserInProject(Model m, HttpSession s,@RequestParam int searchid,@RequestParam String searchname ,@RequestParam String searchcategory,@PathVariable String id_project){
        int project_id = Integer.parseInt(id_project);

        return projectService.searchUserInProject(m,s,searchname,searchcategory,searchid,project_id);
    }

    @RequestMapping(value = "/searchSkillInProject/{id_project}",method = RequestMethod.GET)
    public String searchSkillInProject(Model m, HttpSession s,@RequestParam String searchname,@PathVariable String id_project){
        int project_id = Integer.parseInt(id_project);

        return projectService.searchSkillInProject(m,s,searchname,project_id);
    }


}
