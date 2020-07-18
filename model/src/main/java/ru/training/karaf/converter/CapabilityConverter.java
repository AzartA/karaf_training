package ru.training.karaf.converter;

import ru.training.karaf.model.Capability;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CapabilityConverter implements AttributeConverter<Capability, String> {
    private final static String DELIMITER = " | ";

    @Override
    public String convertToDatabaseColumn(Capability capability) {
        return capability.getRange() +
                DELIMITER +
                capability.getAccuracy() +
                DELIMITER +
                capability.getResolution();
    }

    @Override
    public Capability convertToEntityAttribute(String strField) {
        String[] arr = strField.split(DELIMITER);
        Capability capability = new Capability();
        capability.setRange(arr[0]);
        capability.setAccuracy(arr[1]);
        capability.setResolution(arr[2]);
        return capability;
    }
}
