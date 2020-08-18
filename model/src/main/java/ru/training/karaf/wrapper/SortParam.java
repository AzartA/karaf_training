package ru.training.karaf.wrapper;

import javax.validation.constraints.NotNull;

public class SortParam {
    @NotNull
    private String by;
    @NotNull
    private String order;
}

