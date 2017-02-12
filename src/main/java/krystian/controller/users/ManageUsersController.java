package krystian.controller.users;

import krystian.security.user.User;
import krystian.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * 12/19/2016 2:12 PM
 */
@Controller
public class ManageUsersController {

    @Autowired
    UserRepository users;

    @Autowired
    ApplicationContext context;


    @RequestMapping(value = "/manageUsers")
    public String devices(Model m, @RequestParam(required = false, defaultValue = "-1", name = "open") int idToLoad) {
        m.addAttribute("idToLoad", idToLoad);
        m.addAttribute("users", users.findAll());
        return "manageUsers/manageUsers";
    }

    @RequestMapping(value = "/manageUsers/new")
    public String newDev(Model model) {
        return "manageUsers/new";
    }

    @RequestMapping(value = "/manageUsers/get/{id}")
    public String get(@PathVariable("id") String id, Model model) {
        if (id.equals("new"))
            return newDev(model);
        User found = users.findOne(Long.parseLong(id));
        if (found == null)
            return "users/test";

        model.addAttribute("curLogin", found.getLogin());
        model.addAttribute("curPass", found.getPassword());
        return "manageUsers/user";
    }

    @RequestMapping(value = "/manageUsers/{id}/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public String remove(@PathVariable("id") String id, Model model) {
        try {
            users.delete(Long.parseLong(id));
        } catch (Exception e) {
            return "error";
        }
        return "ok";
    }

    @RequestMapping(value = "/manageUsers/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@PathVariable("id") String id, @RequestBody MultiValueMap<String, String> formData) {

        try {
            User found = users.findOne(Long.parseLong(id));
            if (formData.containsKey("login"))
                found.setLogin(formData.getFirst("login"));
            if (formData.containsKey("pass"))
                found.setPassword(formData.getFirst("pass"));
            users.save(found);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "ok";
    }

    @RequestMapping(value = "/manageUsers/create", method = RequestMethod.POST)
    @ResponseBody
    public CreateResponse create(@RequestBody MultiValueMap<String, String> formData) {
        CreateResponse r = new CreateResponse();
        r.message = "empty";
        r.createdId = -1;
        if (!formData.containsKey("type") || formData.getFirst("type").isEmpty() || !formData.containsKey("login") || formData.getFirst("login").isEmpty() || !formData.containsKey("pass") || formData.getFirst("pass").isEmpty()) {
            r.status = "error";
            return r;
        }
        User u = new User();
        u.setPassword(formData.getFirst("pass"));
        u.setLogin(formData.getFirst("login"));
        try {
            users.save(u);
        } catch (DataIntegrityViolationException e) {
            r.status = "error";
            r.message = e.getMessage();
            return r;
        }
        r.createdId = (int) u.getId();
        r.status = "ok";
        return r;
    }
}
