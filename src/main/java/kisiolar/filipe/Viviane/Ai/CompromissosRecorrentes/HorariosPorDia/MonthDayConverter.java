package kisiolar.filipe.Viviane.Ai.CompromissosRecorrentes.HorariosPorDia;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.MonthDay;

@Converter(autoApply = false)
public class MonthDayConverter implements AttributeConverter<MonthDay, String> {

    @Override
    public String convertToDatabaseColumn(MonthDay attribute) {
        return attribute != null ? attribute.toString() : null;
    }

    @Override
    public MonthDay convertToEntityAttribute(String dbData) {
        return dbData != null ? MonthDay.parse(dbData) : null;
    }
}

