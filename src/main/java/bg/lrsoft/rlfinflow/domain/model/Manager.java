package bg.lrsoft.rlfinflow.domain.model;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.UUID;

import java.util.UUID;

@Entity
@Table(name = "managers")
public class Manager {

    @Id
    @GeneratedValue(strategy = UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phoneNumber;
}
