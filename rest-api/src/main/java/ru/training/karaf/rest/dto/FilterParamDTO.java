package ru.training.karaf.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import ru.training.karaf.rest.validation.ConformingParams;
import ru.training.karaf.view.FilterParam;

public class FilterParamDTO implements FilterParam {
    private static final String COND = "Condition must be one of: '=', '>', '<', '>=', '<=', '!=', 'contains', '!contains'";

    @NotNull
    private String field;

    @Pattern(regexp = "^[><=]$|^[!<>]=$|^!?contains$", message = COND)
    @Size(min = 1, max = 9, message = COND)
    private String cond;

    @NotNull
    private String value;

    public FilterParamDTO() {
    }

    @ConformingParams(message = "There is no such field, or condition is not applicable to the field's type")
    public <T> FilterParamDTO(String field, String cond, String value, Class<T> type) {
        this.field = field;
        this.cond = cond;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String getCond() {
        return cond;
    }

    @Override
    public void setCond(String cond) {
        this.cond = cond;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilterParamDTO)) {
            return false;
        }

        FilterParamDTO that = (FilterParamDTO) o;

        if (!field.equals(that.field)) {
            return false;
        }
        if (!cond.equals(that.cond)) {
            return false;
        }
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = field.hashCode();
        result = 31 * result + cond.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FilterParamDTO{" +
                "field='" + field + '\'' +
                ", cond='" + cond + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
