package krystian.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 12/21/2016 7:36 PM
 */
@ControllerAdvice()
public class CAdvice {
    @ModelAttribute
    public void globalAttributes(Model model) {
        try {
            model.addAttribute("servIp", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            model.addAttribute("servIp", "unknown");
        }
    }
}