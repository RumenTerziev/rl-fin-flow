package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.config.mapper.ConversionMapper;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.repository.ConversionRepository;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

@Service
@Qualifier("BNBRateCurrencyService")
public class BNBRateCurrencyService extends AbstractCurrencyService {

    private static final Logger log = LoggerFactory.getLogger(BNBRateCurrencyService.class);

    @Value("${bnb.currency.rate.url}")
    private String bnbCurrencyRateUrl;

    @Value("${bnb.currency.rate.euro-fixing}")
    private double euroFixing;

    @Autowired
    public BNBRateCurrencyService(FinFlowUserService finFlowUserService,
                                  ConversionRepository conversionRepository,
                                  ConversionMapper conversionMapper) {
        super(finFlowUserService, conversionRepository, conversionMapper);
    }

    @Override
    public CurrencyResponseDto processConvertRequest(CurrencyRequestDto requestDto) {
        final String euroCode = "EUR";
        final String bgnCode = "BGN";
        String desiredCode = "";
        double desiredRate = 0.0;

        if (euroCode.equals(requestDto.toCurrency().toString())) {
            desiredCode = euroCode;
            desiredRate = euroFixing;
            log.debug("{} fix: {}", euroCode, euroFixing);
            CurrencyResponseDto currencyResponseDto = getCurrencyResponseDto(bgnCode, desiredCode, requestDto.amount(), desiredRate);
            conversionRepository.save(getConversionFromRespDto(currencyResponseDto));
            return currencyResponseDto;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(bnbCurrencyRateUrl).openStream());
            doc.getDocumentElement().normalize();

            NodeList rows = doc.getElementsByTagName("ROW");

            for (int index = 0; index < rows.getLength(); index++) {
                Element row = (Element) rows.item(index);

                String code = getTextContent(row, "CODE");
                String ratioStr = getTextContent(row, "RATIO");
                String rateStr = getTextContent(row, "RATE");
                String reverse = getTextContent(row, "REVERSERATE");

                if (code == null || ratioStr == null || rateStr == null || reverse == null) {
                    log.warn("Skipping malformed row at index {}, Code: {}, Ratio: {}, rate: {}, reverse: {}",
                            index, code, ratioStr, rateStr, reverse);
                    continue;
                }

                if (requestDto.toCurrency().toString().equals(code)) {
                    desiredCode = code;
                    desiredRate = Double.parseDouble(rateStr);
                    log.debug("{} – {} unit(s): {} BGN (Reverse: {})",
                            code, ratioStr, rateStr, reverse);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error processing BNB currency rates: {}", e.getMessage(), e);
        }

        CurrencyResponseDto currencyResponseDto = getCurrencyResponseDto(bgnCode, desiredCode, requestDto.amount(), desiredRate);
        conversionRepository.save(getConversionFromRespDto(currencyResponseDto));
        return currencyResponseDto;
    }

    private String getTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        return (list.getLength() > 0 && list.item(0) != null) ? list.item(0).getTextContent() : null;
    }
}
