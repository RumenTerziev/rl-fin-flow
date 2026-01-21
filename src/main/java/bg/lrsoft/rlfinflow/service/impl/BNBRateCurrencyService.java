package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.config.properties.BNBCurrencyRateConfig;
import bg.lrsoft.rlfinflow.domain.constant.CurrencyCode;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

@Slf4j
@Service
@Qualifier("BNBRateCurrencyService")
public class BNBRateCurrencyService extends AbstractCurrencyService {


    private final BNBCurrencyRateConfig bnbCurrencyRateConfig;

    @Autowired
    public BNBRateCurrencyService(FinFlowUserService finFlowUserService,
                                  ConversionRepository conversionRepository,
                                  ConversionMapper conversionMapper,
                                  BNBCurrencyRateConfig bnbCurrencyRateConfig) {
        super(finFlowUserService, conversionRepository, conversionMapper);
        this.bnbCurrencyRateConfig = bnbCurrencyRateConfig;
    }

    @Override
    public CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto) {
        CurrencyCode from = requestDto.fromCurrency();
        CurrencyCode to = requestDto.toCurrency();
        double amount = requestDto.amount();
        double amountInEur = amount / getRateFromEur(from);
        double result = amountInEur * getRateFromEur(to);
        double rate = result / amount;
        CurrencyResponseDto dto = new CurrencyResponseDto(from, to, amount, result, getRoundedValue(rate, 4));
        conversionRepository.save(getConversionFromRespDto(dto));
        return dto;
    }

    private String getTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        return (list.getLength() > 0 && list.item(0) != null) ? list.item(0).getTextContent() : null;
    }


    private double getRateFromEur(CurrencyCode currency) {
        if (currency == CurrencyCode.EUR) {
            return 1.0;
        }

        if (currency == CurrencyCode.BGN) {
            return bnbCurrencyRateConfig.getEuroFixing();
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(
                    new URL(bnbCurrencyRateConfig.getUrl()).openStream()
            );
            doc.getDocumentElement().normalize();

            NodeList rows = doc.getElementsByTagName("ROW");

            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);

                String code = getTextContent(row, "CODE");
                String rate = getTextContent(row, "RATE");

                if (currency.name().equals(code) && rate != null) {
                    return getRoundedValue(Double.parseDouble(rate), 6);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load BNB rates", e);
        }

        throw new IllegalStateException("No BNB rate for " + currency);
    }
}
