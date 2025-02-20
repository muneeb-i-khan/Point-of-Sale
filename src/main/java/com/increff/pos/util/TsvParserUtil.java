package com.increff.pos.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TsvParserUtil {

    private static final int MAX_LINES = 5000;

    public static <T> List<T> parseTSV(InputStream inputStream, Set<String> expectedHeaders,
                                       Function<CSVRecord, T> mapper) throws IOException {
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
            for (CSVRecord record : csvParser) {
                if (count >= MAX_LINES) {
                    throw new IOException("File exceeds the maximum allowed limit of " + MAX_LINES + " lines.");
                }
                recordsList.add(mapper.apply(record));
                count++;
            }
        }

        return recordsList;
    }
}
