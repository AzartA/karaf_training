package ru.training.karaf.wrapper;

import ru.training.karaf.view.SortParam;

public class SortParamImpl implements SortParam {
    private String by;
    private String order;

    public SortParamImpl() {
    }

    public <T> SortParamImpl(String by, String order, Class<T> type) {
        this.by = by;
        this.order = order;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SortParamImpl)) {
            return false;
        }

        SortParamImpl sortParam = (SortParamImpl) o;

        if (!by.equals(sortParam.by)) {
            return false;
        }
        return order.equals(sortParam.order);
    }

    @Override
    public int hashCode() {
        int result = by.hashCode();
        result = 31 * result + order.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SortParam{"
                + "by='" + by + '\''
                + ", order='" + order + '\''
                + '}';
    }
}

