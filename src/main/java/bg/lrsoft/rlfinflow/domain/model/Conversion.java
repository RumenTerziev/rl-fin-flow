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
    private CurrencyCode fromCurrency;

    @Enumerated(STRING)
    private CurrencyCode toCurrency;

    private double amount;

    private double resultSum;

    private double currencyRate;

    private LocalDateTime createdAt;

    public Conversion(String loggedUsername, CurrencyCode fromCurrency, CurrencyCode toCurrency, double amount, double resultSum, double currencyRate) {
        this.loggedUsername = loggedUsername;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.resultSum = resultSum;
        this.currencyRate = currencyRate;
        this.createdAt = LocalDateTime.now();
    }
}
