package ru.vtb.insurance.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.vtb.insurance.domain.Clinic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@Profile("csv")
public class CsvParser extends DataFileParser {
    @Override
    public Collection<Clinic> readData(String file) throws IOException {
        List<Clinic> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(file)));
        while (reader.ready()) {
            String line = reader.readLine();
            String[] cells = line.split(";");
            int length = cells.length;

            try {
                Clinic newClinic = new Clinic(
                        Long.parseLong(cells[0]),
                        parseServices(cells[5]),
                        parseCategory(cells[4]),
                        cells[1],
                        cells[2],
                        cells[3],
                        Double.parseDouble(cells[6]),
                        Double.parseDouble(cells[7]),
                        cells[8].equals("нет данных") ? null : cells[8],
                        cells[9].equals("нет данных") ? null : cells[9]
                );
                result.add(newClinic);
            } catch (ArrayIndexOutOfBoundsException e) {
                log.info(line);
                log.info(e.getMessage());
            }
        }
        return result;
    }
}
