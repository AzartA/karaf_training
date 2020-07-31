package ru.training.karaf.converter;

import ru.training.karaf.model.CapabilityImpl;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CapabilityConverter implements AttributeConverter<CapabilityImpl, String> {
    private final static String DELIMITER = " | ";

    @Override
    public String convertToDatabaseColumn(CapabilityImpl capability) {
        return capability.getRange() +
                DELIMITER +
                capability.getAccuracy() +
                DELIMITER +
                capability.getResolution();
    }

    @Override
    public CapabilityImpl convertToEntityAttribute(String strField) {
        String[] arr = strField.split(DELIMITER);
        CapabilityImpl capability = new CapabilityImpl();
        capability.setRange(arr[0]);
        capability.setAccuracy(arr[1]);
        capability.setResolution(arr[2]);
        return capability;
    }
}
