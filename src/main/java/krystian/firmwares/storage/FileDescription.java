package krystian.firmwares.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * 2/20/2017 11:30 PM
 */
@Entity
public class FileDescription {

    @Id
    @Column(nullable = false, unique = true)
    private String name;

    private String originalName;
    private String description;
    private Timestamp creationDate;

    FileDescription() {
        setCreationDate(new Timestamp(System.currentTimeMillis()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
