package ru.training.karaf.wrapper;

import ru.training.karaf.validation.ConformingFilterParams;
import ru.training.karaf.validation.FilterParamsValidatorImpl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FilterParam {
    private final String COND = "Condition must be one of: '=', '>', '<', '>=', '<=', '!=', 'contains', '!contains'";
    @NotNull
    private String field;
    @Pattern(regexp = "^[><=]$|^[!<>]=$|^!?contains$",
            message = "{filter.condition.field}")
    @Size(min = 1, max = 9, message = "{filter.condition.field}")
    private String cond;
    @NotNull
    private String value;

    public FilterParam() {
    }
    @ConformingFilterParams(message = "There is no such field, or condition is not applicable to the field's type")
    public <T> FilterParam(String field, String cond, String value, Class<T> type) {
        FilterParamsValidatorImpl validator = new FilterParamsValidatorImpl();
        validator.validate(field, cond, value, type);
        this.field = field;
        this.cond = cond;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FilterParam))
            return false;

        FilterParam that = (FilterParam) o;

        if (!field.equals(that.field))
            return false;
        if (!cond.equals(that.cond))
            return false;
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
        return "FilterParam{" +
                "field='" + field + '\'' +
                ", cond='" + cond + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
