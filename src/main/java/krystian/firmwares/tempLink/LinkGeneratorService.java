package krystian.firmwares.tempLink;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2/21/2017 7:16 PM
 */
@Service
public class LinkGeneratorService {

    private final ConcurrentHashMap<String, TempFile> fileMap;

    public LinkGeneratorService() {
        fileMap = new ConcurrentHashMap<>();
    }

    public String mapFile(int minutesValid, String fileId) {
        String mappedTo = UUID.randomUUID().toString().replaceAll("-", "");
        fileMap.put(mappedTo, new TempFile(fileId, System.currentTimeMillis() + 60000L * minutesValid));
        return mappedTo;
    }

    public String getFile(String mappedTo) throws FileNotFoundException {
        if (!fileMap.containsKey(mappedTo)) throw new FileNotFoundException();
        TempFile tf = fileMap.get(mappedTo);
        if (System.currentTimeMillis() > tf.validTo) throw new FileNotFoundException();
        return tf.fileName;
    }

    private class TempFile {
        final String fileName;
        final long validTo;

        TempFile(String fileName, long validTo) {
            this.fileName = fileName;
            this.validTo = validTo;
        }
    }
}
