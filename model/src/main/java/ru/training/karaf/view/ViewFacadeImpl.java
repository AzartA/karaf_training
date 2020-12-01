package ru.training.karaf.view;

import java.util.List;

public class ViewFacadeImpl implements ViewFacade {
    private List<ViewType> views;

    public void setViews(List<ViewType> views) {
        this.views = views;
    }

    public <T extends ViewType> T getView(Class<T> type) {
        for (ViewType v : views) {
            if (type.isAssignableFrom(v.getServiceClass())) {
                return (T) v.get();
            }
        }
        throw new RuntimeException("view is absent today");
    }
}
