package ebdesk.service;

import ebdesk.model.*;
import ebdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.exceptions.TemplateInputException;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asuss on 6/2/2017.
 */

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    SkillRepository skillRepo;
    @Autowired
    UserProjectRepository upRepo;

    @Autowired
    ProjectLogRepository prologRepo;

    @Autowired
    UserService us;

    public String viewAllProjects(Model model, HttpSession session){

        model.addAttribute("projects",  projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("status",1);

//        model.addAttribute("projek2", projectList.get(1).getId());
////        model.addAttribute("leader",userRepo.findByRoleAndProjectId(3,1));
//        for(int i = 0 ; i<projectList.size();i++){
////            User u = userRepo.findAllByRoleAndProjectId(3,projectList.get(i).getId());
//            User u = userRepo.findAllByRoleAndProjectId(3,projectList.get(i).getId());
//            userLeaderList.add(u);
//        }
//        model.addAttribute("leaders",userLeaderList);
        return "projects/view_all_projects";
    }

    public String viewMyProjects(Model model,int id ,HttpSession session){
        model.addAttribute("projects", projectRepo.findAllByUsersId(id));
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("status",2);
        return "projects/view_all_projects";
    }

    public String viewNewProject(Model model, HttpSession session){
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("users",userRepo.findAllByRole(3));
        return "projects/new_project";
    }

    public String postNewProject(Model model, HttpSession session, Project project, int idLeader){
        User user = userRepo.findOne(idLeader);
        project.setUserProjects(new HashSet<UserProject>());

        UserProject userProject = new UserProject();

        userProject.setUser(user);
        userProject.setProject(project);
        userProject.setRoles(1);

        project.setCurrent(1);
        project.getUserProjects().add(userProject);
        user.setOngoingproject(user.getOngoingproject()+1);
        projectRepo.save(project);
        userRepo.save(user);


        ProjectLog pl = new ProjectLog();
        pl.setProject(project);
        pl.setStatus(1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateobj = new Date();
        pl.setDate(df.format(dateobj));
        prologRepo.save(pl);


        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_all_projects";
    }


    public String viewProject(Model model, int id, HttpSession session) {

        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,id);

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }


        model.addAttribute("logs",prologRepo.findAllByProjectID(id));
        model.addAttribute("project",projectRepo.findOne(id));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,id));
        model.addAttribute("all_users",userRepo.findAllByRole(3));
        model.addAttribute("users",userRepo.findAllByRoleNotExistInUserProject(3,id));
        model.addAttribute("members",userRepo.findAllByProjectsId(id));
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(id));
//        model.addAttribute("skills",skillRepo.findAllNotExistInProjectSkills());
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_project";
    }


    public String updateProjectSetting(Model model, HttpSession session, int project_id, int projectprice, String projectname, int projectsize, int projectwaktu_1, String projectwaktu_2, int idLeader, int idLeaderBefore) {

        User before = userRepo.findOne(idLeaderBefore);
        User after = userRepo.findOne(idLeader);
        Project pro = projectRepo.findOne(project_id);
        pro.setPrice(projectprice);
        pro.setName(projectname);
        pro.setSize(projectsize);
        pro.setTime(projectwaktu_1 + " " + projectwaktu_2);

        UserProject delete = new UserProject();
        delete.setUser(before);
        delete.setProject(pro);
        //asumsi project leader sebelum di delete
        delete.setRoles(1);
        pro.setCurrent(pro.getCurrent()-1);
        upRepo.delete(delete);

        //asumsi project leader sebelum di jadiin member biasa
//        delete.setRoles(2);
//        upRepo.save(delete);

        int check1 = upRepo.findAllByProjectId(pro.getId()).size();
        UserProject add = new UserProject();
        add.setUser(after);
        add.setProject(pro);
        add.setRoles(1);
        upRepo.save(add);
        int check2 = upRepo.findAllByProjectId(pro.getId()).size();

        if(check1!=check2){
            pro.setCurrent(pro.getCurrent()+1);

            after.setOngoingproject(after.getOngoingproject()+1);

        }
        before.setOngoingproject(before.getOngoingproject()-1);

        userRepo.save(before);
        userRepo.save(after);
        projectRepo.save(pro);

        ProjectLog pl = new ProjectLog();

        pl.setProject(pro);
        pl.setUser(after);
        pl.setStatus(4);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateobj = new Date();
        pl.setDate(df.format(dateobj));
        prologRepo.save(pl);

        ArrayList<Project> projectList = (ArrayList<Project>) projectRepo.findAll();
        model.addAttribute("projects", projectList);
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_all_projects";
    }


    public String updateProjectSkill(Model model, HttpSession session, int project_id, int projectidskill) {

        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,project_id);

        Skill s  = new Skill();

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }
        Set<Skill> skills;
        Set<Project> projects;
        Project p = projectRepo.findOne(project_id);
        if(p.getProject_skills()!=null){

            skills = p.getProject_skills();

        }else{

            skills = new HashSet<Skill>();
        }
        if(skills.contains(skillRepo.findOne(projectidskill))){

        }else{
            skills.add(skillRepo.findOne(projectidskill));
            p.setProject_skills(skills);

            s = skillRepo.findOne(projectidskill);
            if(s.getProjects()!=null){

                projects = s.getProjects();
            }else{

                projects = new HashSet<Project>();
            }
            projects.add(projectRepo.findOne(project_id));
            s.setProjects(projects);

            projectRepo.save(p);

            skillRepo.save(s);

            ProjectLog pl = new ProjectLog();
            pl.setProject(p);
            pl.setSkill(s);
            pl.setStatus(5);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date dateobj = new Date();
            pl.setDate(df.format(dateobj));
            prologRepo.save(pl);
        }
        model.addAttribute("logs",prologRepo.findAllByProjectID(p.getId()));
        model.addAttribute("project",projectRepo.findOne(p.getId()));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,p.getId()));
        model.addAttribute("all_users",userRepo.findAllByRole(3));
        model.addAttribute("users",userRepo.findAllByRoleNotExistInUserProject(3,p.getId()));
        model.addAttribute("members",userRepo.findAllByProjectsId(p.getId()));
        //        model.addAttribute("skills",skillRepo.findAllNotExistInProjectSkills());
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_project";

    }

    public String updateProjectMember(Model model, HttpSession session, int project_id, int user_id) {
      boolean cek = false;
        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,project_id);

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }

        User u = userRepo.findOne(user_id);
        Project p = projectRepo.findOne(project_id);

        if(p.getCurrent() < p.getSize()){

            UserProject userProject = new UserProject();
            userProject.setUser(u);
            userProject.setProject(p);
            userProject.setRoles(2);

            List<UserProject> userpro = upRepo.findAllByProjectId(p.getId());

            for (int i = 0 ; i< userpro.size();i++){
                if(userpro.get(i).getUser().equals(u)){
                    cek=true;
                }
            }



            if(cek==true){
                model.addAttribute("userContains",true);
            }else{
                u.setOngoingproject(u.getOngoingproject()+1);
                p.getUserProjects().add(userProject);
                p.setCurrent(p.getCurrent()+1);
                projectRepo.save(p);
                userRepo.save(u);

                ProjectLog pl = new ProjectLog();
                pl.setProject(p);
                pl.setUser(u);
                pl.setStatus(2);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dateobj = new Date();
                pl.setDate(df.format(dateobj));
                prologRepo.save(pl);

            }
        }else{
            model.addAttribute("projectFull",true);
        }


        model.addAttribute("logs",prologRepo.findAllByProjectID(p.getId()));
        model.addAttribute("project",projectRepo.findOne(p.getId()));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,p.getId()));
        model.addAttribute("all_users",userRepo.findAllByRole(3));

        model.addAttribute("members",userRepo.findAllByProjectsId(p.getId()));
        //        model.addAttribute("skills",skillRepo.findAllNotExistInProjectSkills());
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_project";

    }

    public String deleteProjectMember(Model model, HttpSession session, int project_id, int user_id) {

        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,project_id);

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }

        Project p = projectRepo.getOne(project_id);
        User u = userRepo.getOne(user_id);
        List<UserProject> userProject = upRepo.findAllByProjectId(project_id);
        for(int i = 0 ; i < userProject.size();i++){
            if (userProject.get(i).getUser().equals(u)){
                if(userProject.get(i).getRoles()!=1){
                    UserProject delete = new UserProject();
                    delete.setUser(u);
                    delete.setProject(p);
                    //asumsi project leader sebelum di delete
                    delete.setRoles(2);
                    p.setCurrent(p.getCurrent()-1);
                    upRepo.delete(delete);

                    ProjectLog pl = new ProjectLog();
                    pl.setProject(p);
                    pl.setUser(u);
                    pl.setStatus(3);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date dateobj = new Date();
                    pl.setDate(df.format(dateobj));
                    prologRepo.save(pl);

                }else{
                    model.addAttribute("deleteFail",true);
                }
            }
        }
        u.setOngoingproject(u.getOngoingproject()-1);
        projectRepo.save(p);
        userRepo.save(u);

        model.addAttribute("logs",prologRepo.findAllByProjectID(p.getId()));
        model.addAttribute("project",projectRepo.findOne(p.getId()));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,p.getId()));
        model.addAttribute("all_users",userRepo.findAllByRole(3));
        model.addAttribute("users",userRepo.findAllByRoleNotExistInUserProject(3,p.getId()));
        model.addAttribute("members",userRepo.findAllByProjectsId(p.getId()));
        //        model.addAttribute("skills",skillRepo.findAllNotExistInProjectSkills());
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_project";


    }

    public String deleteProjectSkill(Model model, HttpSession session, int project_id, int skill_id) {

        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,project_id);

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }

        Project p = projectRepo.getOne(project_id);
        Skill s = skillRepo.getOne(skill_id);

        p.getProject_skills().remove(s);
        s.getProjects().remove(p);

        projectRepo.save(p);
        skillRepo.save(s);

        ProjectLog pl = new ProjectLog();
        pl.setProject(p);
        pl.setSkill(s);
        pl.setStatus(6);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateobj = new Date();
        pl.setDate(df.format(dateobj));
        prologRepo.save(pl);

        model.addAttribute("logs",prologRepo.findAllByProjectID(p.getId()));
        model.addAttribute("project",projectRepo.findOne(p.getId()));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,p.getId()));
        model.addAttribute("all_users",userRepo.findAllByRole(3));
        model.addAttribute("users",userRepo.findAllByRoleNotExistInUserProject(3,p.getId()));
        model.addAttribute("members",userRepo.findAllByProjectsId(p.getId()));
        //        model.addAttribute("skills",skillRepo.findAllNotExistInProjectSkills());
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_project";
    }

    public String cancelProject(Model model, HttpSession session, int project_id) {
        Project p = projectRepo.findOne(project_id);
        List<User> users = userRepo.findAllByProjectsId(project_id);
        for(int i = 0 ; i < users.size();i++){
            users.get(i).setOngoingproject(users.get(i).getOngoingproject()-1);
            userRepo.save(users.get(i));
        }

        List<UserProject> userProjects = upRepo.findAllByProjectId(project_id);
        for(int i = 0; i<userProjects.size();i++){
            userProjects.get(i).setUser(null);
            userProjects.get(i).setProject(null);
            upRepo.save(userProjects.get(i));
        }
        p.setProject_skills(null);
        projectRepo.save(p);
        projectRepo.delete(project_id);

//        ProjectLog pl = new ProjectLog();
//        pl.setProject(p);
//        pl.setStatus(8);
//               DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Date dateobj = new Date();
//        pl.setDate(df.format(dateobj));
//        prologRepo.save(pl);


        ArrayList<Project> projectList = (ArrayList<Project>) projectRepo.findAll();
        model.addAttribute("projects", projectList);
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_all_projects";

    }

    public String finishProject(Model model, HttpSession session, int project_id) {

        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,project_id);

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }


        List<User> users= userRepo.findAllByProjectsId(project_id);
        for(int i = 0 ; i < users.size();i++){
            User member = users.get(i);
            member.setOngoingproject(member.getOngoingproject()-1);
            member.setTotalproject(member.getTotalproject()+1);
            UserProject up = upRepo.findByProjectIdAndUserId(member.getId(),project_id);
            member.setTotalstar(member.getTotalstar()+up.getRate());
            userRepo.save(member);
        }

        Project p = projectRepo.findOne(project_id);
        p.setStatus(2);
        projectRepo.save(p);

        ProjectLog pl = new ProjectLog();
        pl.setProject(p);
        pl.setStatus(7);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateobj = new Date();
        pl.setDate(df.format(dateobj));
        prologRepo.save(pl);

        model.addAttribute("logs",prologRepo.findAllByProjectID(p.getId()));
        model.addAttribute("project",projectRepo.findOne(p.getId()));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,p.getId()));
        model.addAttribute("all_users",userRepo.findAllByRole(3));
        model.addAttribute("users",userRepo.findAllByRoleNotExistInUserProject(3,p.getId()));
        model.addAttribute("members",userRepo.findAllByProjectsId(p.getId()));
        //        model.addAttribute("skills",skillRepo.findAllNotExistInProjectSkills());
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_project";
    }

    public String updateUserRate (Model m, HttpSession s, int project_id, int user_id, int value) {

        UserProject up = new UserProject();

        Project p = projectRepo.findOne(project_id);
        User u = userRepo.findOne(user_id);
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,p.getId());
        if(u.getId() == leader.getId()){//kalo leader sendiri
            up.setUser(u);
            up.setProject(p);
            up.setRate(value);
            up.setRoles(1);
        }else{
            up.setUser(u);
            up.setProject(p);
            up.setRate(value);
            up.setRoles(2);
        }
        upRepo.save(up);

        return "success";
    }

    public String searchProject(Model model, HttpSession session, String searchname, String searchcategory,String  searchstatus) {

        switch(Integer.parseInt(searchstatus)){
            case 1 :
                switch (searchcategory){
                    case "name" :
                        model.addAttribute("projects", projectRepo.findAllBySearchName("%"+searchname+"%"));
                        break;
                    case "date" :
                        model.addAttribute("projects", projectRepo.findAllBySearchDate("%"+searchname+"%"));
                        break;
                    case "size" :
                        model.addAttribute("projects", projectRepo.findAllBySearchSize("%"+searchname+"%"));
                        break;
                    case "time" :
                        model.addAttribute("projects", projectRepo.findAllBySearchTime("%"+searchname+"%"));
                        break;
                    case "price" :
                        model.addAttribute("projects", projectRepo.findAllBySearchPrice("%"+searchname+"%"));
                        break;
                    case "status" :
                        if(searchname.contains("c") || searchname.contains("m") || searchname.contains("p") || searchname.contains("l") ||searchname.contains("e") || searchname.contains("t") || searchname.contains("d")){
                            model.addAttribute("projects", projectRepo.findAllByStatus(2));
                        }else if (searchname.contains("n") || searchname.contains("g") || searchname.contains("i")){
                            model.addAttribute("projects",projectRepo.findAllByStatus(1));
                        }else{
                            model.addAttribute("projects",null);
                        }

                        break;
                }
                model.addAttribute("status",1);
                break;
            case 2 :
                User u = (User) session.getAttribute("user");
                switch (searchcategory){
                    case "name" :
                        model.addAttribute("projects", projectRepo.findAllByUserIdSearchName(u.getId(),"%"+searchname+"%"));
                        break;
                    case "date" :
                        model.addAttribute("projects", projectRepo.findAllByUserIdSearchDate(u.getId(),"%"+searchname+"%"));
                        break;
                    case "size" :
                        model.addAttribute("projects", projectRepo.findAllByUserIdSearchSize(u.getId(),"%"+searchname+"%"));
                        break;
                    case "time" :
                        model.addAttribute("projects", projectRepo.findAllByUserIdSearchTime(u.getId(),"%"+searchname+"%"));
                        break;
                    case "price" :
                        model.addAttribute("projects", projectRepo.findAllByUserIdSearchPrice(u.getId(),"%"+searchname+"%"));
                        break;
                    case "status" :
                        if(searchname.contains("c") || searchname.contains("m") || searchname.contains("p") || searchname.contains("l") ||searchname.contains("e") || searchname.contains("t") || searchname.contains("d")){
                            model.addAttribute("projects", projectRepo.findAllByUserIdStatus(u.getId(),2));
                        }else if (searchname.contains("n") || searchname.contains("g") || searchname.contains("i")){
                            model.addAttribute("projects",projectRepo.findAllByUserIdStatus(u.getId(),1));
                        }else{
                            model.addAttribute("projects",null);
                        }
                        break;
                }
                model.addAttribute("status",2);
                break;
        }
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_all_projects";
    }


    public String searchUserInProject(Model model, HttpSession session, String searchname, String searchcategory,int searchid,int project_id) {
        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,project_id);
        Project p = projectRepo.getOne(project_id);

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }

        switch (searchcategory) {
            case "fname":
                model.addAttribute("all_users", userRepo.findAllByRoleSearchFirstName("%" + searchname + "%"));
                break;
            case "lname":
                model.addAttribute("all_users", userRepo.findAllByRoleSearchLastName("%" + searchname + "%"));
                break;
            case "email":
                model.addAttribute("all_users", userRepo.findAllByRoleSearchEmail("%" + searchname + "%"));
                break;
            case "phone":
                model.addAttribute("all_users", userRepo.findAllByRoleSearchPhone("%" + searchname + "%"));
                break;
            case "rate":
                model.addAttribute("all_users", userRepo.findAllByRoleSearchRate("%" + searchname + "%"));
                break;
            case "division":
                model.addAttribute("all_users", userRepo.findAllByRoleSearchDivision("%" + searchname + "%"));
                break;
            case "role":
                if( searchname.contains("i") || searchname.contains("n") ){
                    model.addAttribute("all_users", userRepo.findAllByRole(1));
                }else if (searchname.contains("g") || searchname.contains("r") || searchname.contains("u")){
                    model.addAttribute("all_users", userRepo.findAllByRole(2));
                }else if (searchname.contains("m") || searchname.contains("y")){
                    model.addAttribute("all_users", userRepo.findAllByRole(3));
                }else{
                    model.addAttribute("all_users", null);
                }
                break;
        }
        if(searchid==1){
            model.addAttribute("modalOpen",1);
        }else{

            model.addAttribute("modalOpen",2);
        }
        model.addAttribute("logs",prologRepo.findAllByProjectID(p.getId()));
        model.addAttribute("project",projectRepo.findOne(p.getId()));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,p.getId()));
        model.addAttribute("members",userRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        return "projects/view_project";
    }

    public String searchSkillInProject(Model model, HttpSession session, String searchname, int project_id) {
        User usersession = (User) session.getAttribute("user");
        User leader = userRepo.findByUserProjectRoleAndProjectId(1,project_id);
        Project p = projectRepo.getOne(project_id);

        if(usersession.getId()==leader.getId()){
            model.addAttribute("permit",true);
        }

        model.addAttribute("logs",prologRepo.findAllByProjectID(p.getId()));
        model.addAttribute("project",projectRepo.findOne(p.getId()));
        model.addAttribute("leader",userRepo.findByUserProjectRoleAndProjectId(1,p.getId()));
        model.addAttribute("members",userRepo.findAllByProjectsId(p.getId()));
        //        model.addAttribute("skills",skillRepo.findAllNotExistInProjectSkills());
        model.addAttribute("skills",skillRepo.findAllBySearchName("%"+searchname+"%"));
        model.addAttribute("requiredskills",skillRepo.findAllByProjectsId(p.getId()));
        model.addAttribute("projects", projectRepo.findAll());
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("modalOpen",3);
        return "projects/view_project";
    }

    public String searchUserWhenAddNewProject(Model model, HttpSession session, String searchname, String searchcategory) {

        switch (searchcategory) {
            case "fname":
                model.addAttribute("users", userRepo.findAllBySearchFirstName("%" + searchname + "%"));
                break;
            case "lname":
                model.addAttribute("users", userRepo.findAllBySearchLastName("%" + searchname + "%"));
                break;
            case "email":
                model.addAttribute("users", userRepo.findAllBySearchEmail("%" + searchname + "%"));
                break;
            case "phone":
                model.addAttribute("users", userRepo.findAllBySearchPhone("%" + searchname + "%"));
                break;
            case "rate":
                model.addAttribute("users", userRepo.findAllBySearchRate("%" + searchname + "%"));
                break;
            case "division":
                model.addAttribute("users", userRepo.findAllBySearchDivision("%" + searchname + "%"));
                break;
            case "role":
                if( searchname.contains("i") || searchname.contains("n") ){
                    model.addAttribute("users", userRepo.findAllByRole(1));
                }else if (searchname.contains("g") || searchname.contains("r") || searchname.contains("u")){
                    model.addAttribute("users", userRepo.findAllByRole(2));
                }else if (searchname.contains("m") || searchname.contains("y")){
                    model.addAttribute("users", userRepo.findAllByRole(3));
                }else{
                    model.addAttribute("users", null);
                }
                break;
        }
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("modalOpen",1);
        return "projects/new_project";
    }
}
