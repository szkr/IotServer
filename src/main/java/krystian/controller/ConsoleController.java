package krystian.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 2/21/2017 2:18 PM
 */
@Controller
public class ConsoleController {

    @RequestMapping("/console")
    public String console() {
        return "console";
    }
}
