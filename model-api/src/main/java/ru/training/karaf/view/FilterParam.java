package ru.training.karaf.view;

public interface FilterParam {
    String getField();
    String getCond();
    String getValue();
    void setField(String field);
    void setCond(String cond);
    void setValue(String value);

}
