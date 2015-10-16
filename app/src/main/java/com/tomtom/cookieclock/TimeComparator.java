package com.tomtom.cookieclock;

import com.tomtom.cookieclock.repository.GammerResultDAO;

import java.util.Comparator;

/**
 * Copyright (C) TomTom International B.V., 2015
 * All rights reserved.
 */
public class TimeComparator implements Comparator<GammerResultDAO> {
    @Override
    public int compare(GammerResultDAO lhs, GammerResultDAO rhs) {
        return (int) (lhs.getTimeinMs() - rhs.getTimeinMs());
    }
}
