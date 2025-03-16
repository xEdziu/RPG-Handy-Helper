package dev.goral.rpgmanager.rpgSystems;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class RpgSystems {
    @Id
    @SequenceGenerator(
            name = "rpgSustems_sequence",
            sequenceName = "rpgSustems_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rpgSustems_sequence"
    )
    private Long id;
    private String name;
    private String description;

    public RpgSystems() {
    }

    public RpgSystems(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RpgSystems(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "rpgSystems{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
