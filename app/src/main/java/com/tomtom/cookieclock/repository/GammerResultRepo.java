package com.tomtom.cookieclock.repository;

import java.util.List;

/**
 * Created by saramakm on 14-10-15.
 */
public interface GammerResultRepo {
    public void clear();
    public List<GammerResultDAO> getResults();
    public void saveGammerResult(GammerResultDAO gammerResultDAO);
}
