package com.example.cs2340a_team1.model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SortAlpha implements Sorting {
    //sort by alpha
    @Override
    public Set<String> sort(Set<String> names, HashMap<String, Integer> recipes) {
        names = names.stream().sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return names;
    }
}