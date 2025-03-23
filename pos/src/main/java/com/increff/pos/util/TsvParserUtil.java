package com.increff.pos.util;

import com.increff.pos.spring.ApplicationProperties;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Component
public class TsvParserUtil {

    @Autowired
    private ApplicationProperties applicationProperties;

    public <T> List<T> parseTSV(InputStream inputStream, Set<String> expectedHeaders,
                                Function<CSVRecord, T> mapper) throws IOException , ApiException {
        List<T> recordsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT
                             .withDelimiter('\t')
                             .withFirstRecordAsHeader()
                             .withTrim()
                             .withIgnoreSurroundingSpaces())) {

            Set<String> fileHeaders = csvParser.getHeaderMap().keySet();
            if (!fileHeaders.containsAll(expectedHeaders)) {
                throw new IllegalArgumentException("Invalid headers in file. Expected: " + expectedHeaders +
                        ", Found: " + fileHeaders);
            }

            int count = 0;
            int maxLines = applicationProperties.getMaxTsvLines();
            for (CSVRecord record : csvParser) {
                if (count >= maxLines) {
                    throw new ApiException("File exceeds the maximum allowed limit of " + maxLines + " lines.");
                }
                recordsList.add(mapper.apply(record));
                count++;
            }
        }

        return recordsList;
    }


    public String escapeTsv(String value) {
        if (value == null) return "";
        return value.replace("\t", "\\t").replace("\n", "\\n");
    }
}