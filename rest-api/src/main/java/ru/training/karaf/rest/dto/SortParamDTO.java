package ru.training.karaf.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ru.training.karaf.rest.validation.ConformingParams;
import ru.training.karaf.view.SortParam;

public class SortParamDTO implements SortParam {
    @NotNull
    private String by;
    @Pattern(regexp = "^asc|desc$", message = "order must be asc or desc only")
    private String order;

    public SortParamDTO() {
    }

    @ConformingParams(message = "There is no such field")
    public <T> SortParamDTO(String by, String order, Class<T> type) {
        this.by = by;
        this.order = order;
    }

    @Override
    public String getBy() {
        return by;
    }

    @Override
    public void setBy(String by) {
        this.by = by;
    }

    @Override
    public String getOrder() {
        return order;
    }

    @Override
    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SortParamDTO)) {
            return false;
        }

        SortParamDTO that = (SortParamDTO) o;

        if (!by.equals(that.by)) {
            return false;
        }
        return order.equals(that.order);
    }

    @Override
    public int hashCode() {
        int result = by.hashCode();
        result = 31 * result + order.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SortParamDTO{" +
                "by='" + by + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
