package bg.lrsoft.rlfinflow.domain.model;

import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

@Getter
@Entity
@Table(name = "conversions")
@NoArgsConstructor
public class Conversion {

    @Id
    @GeneratedValue(strategy = UUID)
    private UUID id;

    private String loggedUsername;

    @Enumerated(STRING)
    private CurrencyCode baseCurrency;

    @Enumerated(STRING)
    private CurrencyCode currencyToConvertTo;

    private double sumToConvert;

    private double resultSum;

    private double currencyRate;

    private LocalDateTime createdAt;

    public Conversion(String loggedUsername, CurrencyCode baseCurrency, CurrencyCode currencyToConvertTo, double sumToConvert, double resultSum, double currencyRate) {
        this.loggedUsername = loggedUsername;
        this.baseCurrency = baseCurrency;
        this.currencyToConvertTo = currencyToConvertTo;
        this.sumToConvert = sumToConvert;
        this.resultSum = resultSum;
        this.currencyRate = currencyRate;
        this.createdAt = LocalDateTime.now();
    }
}
