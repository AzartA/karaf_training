package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

public class ViewFacadeImpl implements ViewFacade {
    private List<ViewType> views;
    //private Map<Class<? extends ViewType>,ViewType> typesMap;

    public void setViews(List<ViewType> views) {
        this.views = views;
        //typesMap = views.stream().collect(Collectors.toMap(ViewType::getClass, vT -> vT));
    }

    public <T extends ViewType> T getView(Class<T> type){
        //Optional<ViewType> view = views.stream().filter(v-> v.getServiceClass().isInstance(type)).reduce((v, t)-> v);
        for (ViewType v :views) {
            if(type.isAssignableFrom(v.getServiceClass())){
                return (T) v;
            }
        }
        /*if(view.isPresent()){
            return (T)view.get();
        }*/
        throw new RuntimeException("view is absent today");
    }
}
