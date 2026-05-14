package bg.lrsoft.rlfinflow.domain.model;

import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

/**
 * Represents an authenticated end-user.
 *
 * <p>Password-based authentication was removed: users only sign in via OAuth2 (Google).
 * Therefore this entity no longer implements {@code UserDetails} and no longer carries a
 * password column. Authorities are persisted so role checks survive across sessions.
 */
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@ToString
public class FinFlowUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String pictureUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private Set<String> authorities = new HashSet<>();

    private LocalDateTime createdAt;

    public FinFlowUser(String username, String email, String pictureUrl, Set<String> authorities) {
        this.username = username;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.authorities = authorities != null ? new HashSet<>(authorities) : new HashSet<>();
        this.createdAt = LocalDateTime.now();
    }

    public void updateFromPrincipal(FinFlowOath2User principal) {
        this.username = principal.getAttribute("name");
        this.pictureUrl = principal.getAttribute("picture");
        this.authorities.clear();
        principal.getAuthorities().forEach(a -> this.authorities.add(a.getAuthority()));
    }
}
