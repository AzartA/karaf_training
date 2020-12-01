package ru.training.karaf.wrapper;

import ru.training.karaf.view.FilterParam;

public class FilterParamImpl implements FilterParam {
    private final String COND = "Condition must be one of: '=', '>', '<', '>=', '<=', '!=', 'contains', '!contains'";
    private String field;
    private String cond;
    private String value;

    public FilterParamImpl() {
    }

    public <T> FilterParamImpl(String field, String cond, String value, Class<T> type) {
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilterParamImpl)) {
            return false;
        }

        FilterParamImpl that = (FilterParamImpl) o;

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
        return "FilterParam{"
                + "field='" + field + '\''
                + ", cond='" + cond + '\''
                + ", value='" + value + '\''
                + '}';
    }
}
