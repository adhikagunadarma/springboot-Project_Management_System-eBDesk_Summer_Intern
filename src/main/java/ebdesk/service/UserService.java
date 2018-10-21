package ebdesk.service;

import ebdesk.model.*;
import ebdesk.repository.DivisionRepository;
import ebdesk.repository.ProjectLogRepository;
import ebdesk.repository.SkillRepository;
import ebdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asuss on 5/27/2017.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    SkillRepository skillRepo;
    @Autowired
    ProjectLogRepository prologRepo;
    @Autowired
    DivisionRepository divisionRepo;

    public String viewAllEmployees(Model model,HttpSession session ){
        ArrayList<User> userList = (ArrayList<User>) userRepo.findAll();
        model.addAttribute("users", userList);
        model.addAttribute("user",session.getAttribute("user"));
        return "employees/view_all_employees";
    }

    public String viewEmployeesAssignment(Model model,HttpSession session ){
        ArrayList<User> userEmployee = (ArrayList<User>) userRepo.findAllEmployeeAssignment();
        int arr_project[][] = new int[userEmployee.size()][];
        List<Integer> list_project;
        List<Integer> list_userP=new ArrayList<>();
        list_userP=userRepo.findAllUserIdEmpAssign();
        boolean ktm=false;
        List<String> project_name = new ArrayList<>();

        Map<Integer, List<Integer>> map_projectId = new HashMap<>();
        for (int i = 0; i <list_userP.size() ; i++) {
            list_project=userRepo.findAllEmployeeAssignmentId(list_userP.get(i));
            map_projectId.put(list_userP.get(i),list_project);
        }
        Map<Integer, List<String>> map_projectName = new TreeMap<>();
        for (int i = 0; i <list_userP.size() ; i++) {
            project_name=userRepo.findAllEmployeeAssignmentName(list_userP.get(i));
            map_projectName.put(list_userP.get(i),project_name);
        }


        System.out.println("------------------------------------------");
        System.out.println("MAP");
        for (Map.Entry<Integer, List<Integer>> entry : map_projectId.entrySet()) {
            System.out.println("Key = " + entry.getKey() + " : " + entry.getValue());
        }

        for (Map.Entry<Integer, List<String>> entry : map_projectName.entrySet()) {
            System.out.println("Key = " + entry.getKey() + " : " + entry.getValue());
        }
        model.addAttribute("users", userEmployee);
        model.addAttribute("map_project", map_projectName);
        model.addAttribute("user",session.getAttribute("user"));
        return "employees/employees_assignment";
    }

    public String viewLogin(){
        return"index/login";
    }

    public String goLogin(String email, String password, Model model,HttpSession session ){
        BCryptPasswordEncoder passwordDecoder = new BCryptPasswordEncoder();
        model.addAttribute("logs",prologRepo.findAllByOrderByIdDesc());
//        if((userRepo.findByEmail(email)!=null)&&(userRepo.findByEmail(email).getPassword().equals(password))){
        if((userRepo.findByEmail(email)!=null)&&(passwordDecoder.matches(password,userRepo.findByEmail(email).getPassword()))){
            session.setAttribute("user",userRepo.findByEmail(email));
            model.addAttribute("user",session.getAttribute("user"));
            return "index/index";
        }else{
            model.addAttribute("salahPassword", "salah");

            return "index/login";
        }
    }

    public String viewRegister(){
        return "index/register";
    }

    public String goRegister(User user,Model model){
        if(userRepo.findByEmail(user.getEmail())==null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            System.out.println("Pass si baru = "+user.getPassword());
//            System.out.println("INIIIIII"+passwordEncoder.encode(user.getPassword()));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(3);
            user.setProf_pic("default.png");
            Division d=new Division();
            d.setId(99);
            user.setDivision(d);
            userRepo.save(user);
            model.addAttribute("msg","Register Success");
            model.addAttribute("namaRe", user.getFirst_name());
            model.addAttribute("salahPassword", "buat");
            return "index/login";
        }else{
            model.addAttribute("msg","Email already registered, please login");
            System.out.println("gobs");
            return "index/register";
        }
    }

    public String editUser(User user,Model model,String first_name,String last_name, String phone, String aboutme, String line_id, HttpSession session,int id){


        System.out.println("FIRST NAME = "+first_name);
        System.out.println("LAST NAME = "+last_name);
        System.out.println( "ABOUT ME = "+aboutme);
        System.out.println("PHONE = "+phone);
        System.out.println("LINE ID = "+line_id);
        user=userRepo.findUserById(id);
        System.out.println("UDAH keambil "+ user.getEmail());
        user.setAbout_me(aboutme);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setPhone(phone);
        user.setLine_id(line_id);
        userRepo.save(user);

        model.addAttribute("requiredskills",skillRepo.findAllByUsersId(id));
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("userPilih",userRepo.findOne(id));
        session.setAttribute("user",userRepo.findUserById(id));
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("divisions",divisionRepo.findAll());
        return "employees/profile";
    }

    public String updateUserSkill(Model model, HttpSession session, int id, int idskill) {

        User usersession = (User) session.getAttribute("user");
        Skill s  = new Skill();
        Set<Skill> skills;
        User p = userRepo.findOne(id);
        if(p.getUser_skills()!=null){

            skills = p.getUser_skills();
            System.out.println("SIZE = "+skills.size());
        }else{
            skills = new HashSet<Skill>();
        }

        if(skills.contains(skillRepo.findOne(idskill))){
            System.out.println("SKillnya udah ada");

        }else{
            skills.add(skillRepo.findOne(idskill));
            System.out.println("skill yang mau ditambah = "+skillRepo.findOne(idskill));
            p.setUser_skills(skills);
            userRepo.save(p);
//            skillRepo.save(s);
        }

        List<Integer>mosi=userRepo.findEmployeeSkillId(id);
        List<String> skill_name = new ArrayList<>();
        for(int i=0 ; i<mosi.size();i++)
        {
            System.out.println("ANGKKA SKILLNYA = "+mosi.get(i));
            String nama=userRepo.findEmployeeSkillName(mosi.get(i));
            skill_name.add(nama);
        }
        model.addAttribute("skill_userPilih",skill_name);



        model.addAttribute("requiredskills",skillRepo.findAllByUsersId(id));
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("userPilih",userRepo.findOne(id));
        session.setAttribute("user",userRepo.findUserById(id));
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("divisions",divisionRepo.findAll());

        return "employees/profile";
    }


    public String viewEmployee(Model model, int id, HttpSession session) {

        model.addAttribute("userPilih",userRepo.findOne(id));
        List<Integer>mosi=userRepo.findEmployeeSkillId(id);
        List<String> skill_name = new ArrayList<>();
        for(int i=0 ; i<mosi.size();i++)
        {
            System.out.println("ANGKKA SKILLNYA = "+mosi.get(i));
            String nama=userRepo.findEmployeeSkillName(mosi.get(i));
            skill_name.add(nama);
        }

        model.addAttribute("requiredskills",skillRepo.findAllByUsersId(id));
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("skill_userPilih",skill_name);
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("divisions",divisionRepo.findAll());

        return "employees/profile";
    }


    public String searchUser(Model model, HttpSession session, String searchname, String searchcategory) {
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
        return "employees/view_all_employees";
    }

    public String searchSkillInUser(Model model, HttpSession session, String searchname, int user_id) {
        model.addAttribute("skills",skillRepo.findAllBySearchName("%"+searchname+"%"));
        model.addAttribute("modalOpen",3);

        model.addAttribute("userPilih",userRepo.findOne(user_id));
        List<Integer>mosi=userRepo.findEmployeeSkillId(user_id);
        List<String> skill_name = new ArrayList<>();
        for(int i=0 ; i<mosi.size();i++)
        {
            System.out.println("ANGKKA SKILLNYA = "+mosi.get(i));
            String nama=userRepo.findEmployeeSkillName(mosi.get(i));
            skill_name.add(nama);
        }

        model.addAttribute("requiredskills",skillRepo.findAllByUsersId(user_id));
        model.addAttribute("skill_userPilih",skill_name);
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("divisions",divisionRepo.findAll());
        return "employees/profile";
    }

    public String searchDivisionInUser(Model model, HttpSession session, String searchname, int user_id) {


        User usersession = (User) session.getAttribute("user");
        User p = userRepo.getOne(user_id);

        model.addAttribute("divisions",divisionRepo.findAllBySearchName("%"+searchname+"%"));
        model.addAttribute("modalOpen",2);


        model.addAttribute("userPilih",userRepo.findOne(user_id));
        List<Integer>mosi=userRepo.findEmployeeSkillId(user_id);
        List<String> skill_name = new ArrayList<>();
        for(int i=0 ; i<mosi.size();i++)
        {
            System.out.println("ANGKKA SKILLNYA = "+mosi.get(i));
            String nama=userRepo.findEmployeeSkillName(mosi.get(i));
            skill_name.add(nama);
        }

        model.addAttribute("requiredskills",skillRepo.findAllByUsersId(user_id));
        model.addAttribute("skill_userPilih",skill_name);
        model.addAttribute("user",session.getAttribute("user"));
        return "employees/profile";
    }

    public String deleteUserSkill(Model model, HttpSession session, int user_id, int skill_id) {

        User u = userRepo.getOne(user_id);
        Skill s = skillRepo.getOne(skill_id);

        u.getUser_skills().remove(s);
        s.getUsers().remove(u);

        userRepo.save(u);
        skillRepo.save(s);

        model.addAttribute("userPilih",userRepo.findOne(user_id));
        List<Integer>mosi=userRepo.findEmployeeSkillId(user_id);
        List<String> skill_name = new ArrayList<>();
        for(int i=0 ; i<mosi.size();i++)
        {
            System.out.println("ANGKKA SKILLNYA = "+mosi.get(i));
            String nama=userRepo.findEmployeeSkillName(mosi.get(i));
            skill_name.add(nama);
        }

        model.addAttribute("requiredskills",skillRepo.findAllByUsersId(user_id));
        model.addAttribute("skill_userPilih",skill_name);
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("divisions",divisionRepo.findAll());
        return "employees/profile";
    }

    public String updateUserDivision(Model model, HttpSession session, int id, int id_division) {

        User usersession = (User) session.getAttribute("user");
        Division d  = new Division();
        Set<Skill> skills;
        User u = userRepo.findOne(id);
        System.out.println("User yang mau dganti divisinya = "+u.getFirst_name());
        d=divisionRepo.findOne(id_division);
        System.out.println("divisinya = "+d.getName());

        u.setDivision(d);

        userRepo.save(u);
//

        List<Integer>mosi=userRepo.findEmployeeSkillId(id);
        List<String> skill_name = new ArrayList<>();
        for(int i=0 ; i<mosi.size();i++)
        {
            System.out.println("ANGKKA SKILLNYA = "+mosi.get(i));
            String nama=userRepo.findEmployeeSkillName(mosi.get(i));
            skill_name.add(nama);
        }
        model.addAttribute("skill_userPilih",skill_name);



        model.addAttribute("requiredskills",skillRepo.findAllByUsersId(id));
        model.addAttribute("skills",skillRepo.findAll());
        model.addAttribute("userPilih",userRepo.findOne(id));
        model.addAttribute("user",session.getAttribute("user"));
        model.addAttribute("divisions",divisionRepo.findAll());

        return "employees/profile";
    }


}
