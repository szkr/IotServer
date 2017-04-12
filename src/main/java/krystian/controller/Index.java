package krystian.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * 12/14/2016 11:11 PM
 */

@Controller
public class Index {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Principal principal) {

        return "index";
    }
}
