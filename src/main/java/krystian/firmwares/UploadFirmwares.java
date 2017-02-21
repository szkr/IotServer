package krystian.firmwares;

import krystian.firmwares.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 2/19/2017 3:42 PM
 */
@Controller
public class UploadFirmwares {
    private final StorageService storageService;

    @Autowired
    public UploadFirmwares(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/uploadFirmwares")
    public String manageFirmwares(Model model) {
        return "firmwares/uploadFirmwares";
    }

    @PostMapping("/uploadFirmwares/add")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "{}";
    }
}
