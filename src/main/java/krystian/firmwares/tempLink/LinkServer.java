package krystian.firmwares.tempLink;

import krystian.firmwares.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * 2/21/2017 7:37 PM
 */
@Controller
public class LinkServer {

    private final LinkGeneratorService linkGeneratorService;
    private final StorageService storageService;


    @Autowired
    public LinkServer(LinkGeneratorService linkGeneratorService, StorageService storageService) {
        this.linkGeneratorService = linkGeneratorService;
        this.storageService = storageService;
    }

    @RequestMapping(value = "/fw/{key}", method = RequestMethod.GET)
    public ResponseEntity download(@PathVariable(name = "key") String key, HttpServletResponse response) {
        try {
            Resource r = storageService.loadAsResource(linkGeneratorService.getFile(key));
            response.setHeader("Content-Disposition", "attachment; filename=firmware");
            return ResponseEntity
                    .ok()
                    .contentLength(r.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(r.getInputStream()));
        } catch (Exception e) {
            response.setStatus(500);
            return null;
        }
    }
}
