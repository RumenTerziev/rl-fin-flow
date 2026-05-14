package bg.lrsoft.rlfinflow.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

/**
 * Immutable audit record of a single GDPR consent acceptance by a user.
 *
 * <p>One row is written every time a user accepts a (versioned) privacy policy. We keep
 * older rows for traceability/audit, and look up the latest one matching the current
 * {@code app.gdpr.policy-version} to decide whether the user must re-consent.
 */
@Getter
@Entity
@Table(name = "consent_records", indexes = {
        @Index(name = "idx_consent_email_version", columnList = "email,policyVersion")
})
@NoArgsConstructor(access = PROTECTED)
public class ConsentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String policyVersion;

    @Column(nullable = false)
    private LocalDateTime acceptedAt;

    /** SHA-256 hash of the IP that submitted the consent. Never store the raw IP. */
    @Column(length = 64)
    private String ipHash;

    @Column(length = 512)
    private String userAgent;

    public ConsentRecord(String email, String policyVersion, LocalDateTime acceptedAt,
                         String ipHash, String userAgent) {
        this.email = email;
        this.policyVersion = policyVersion;
        this.acceptedAt = acceptedAt;
        this.ipHash = ipHash;
        this.userAgent = userAgent;
    }
}
