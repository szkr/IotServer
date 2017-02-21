package krystian.firmwares;

import krystian.firmwares.storage.FileDescription;
import krystian.firmwares.storage.FileDescriptionRepository;
import krystian.firmwares.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * 2/20/2017 10:09 PM
 */
@Controller
public class ManageFirmwares {

    private final FileDescriptionRepository fileDescriptionRepository;
    private final StorageService storageService;

    @Autowired
    public ManageFirmwares(FileDescriptionRepository fileDescriptionRepository, StorageService storageService) {
        this.fileDescriptionRepository = fileDescriptionRepository;
        this.storageService = storageService;
    }

    @RequestMapping("/manageFirmwares")
    public String manageFirmwares(Model model) {
        model.addAttribute("files", fileDescriptionRepository.findAll());
        return "firmwares/manageFirmwares";
    }

    @RequestMapping(value = "/manageFirmwares/download/{key}", method = RequestMethod.GET)
    public ResponseEntity download(@PathVariable(name = "key") String key, HttpServletResponse response) {
        try {
            FileDescription fd = fileDescriptionRepository.findOne(key);
            Resource r = storageService.loadAsResource(key);
            response.setHeader("Content-Disposition", "attachment; filename=" + fd.getOriginalName());
            return ResponseEntity
                    .ok()
                    .contentLength(r.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(r.getInputStream()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(401).body("file not found");
        }
    }

    @RequestMapping(value = "/manageFirmwares/remove/{key}", method = RequestMethod.GET)
    public synchronized String remove(@PathVariable(name = "key") String key) {
        try {
            FileDescription fd = fileDescriptionRepository.findOne(key);
            Resource r = storageService.loadAsResource(key);
            if (r.getFile().delete())
                fileDescriptionRepository.delete(fd);
        } catch (Exception ignored) {
        }
        return "redirect:/manageFirmwares";
    }
}
