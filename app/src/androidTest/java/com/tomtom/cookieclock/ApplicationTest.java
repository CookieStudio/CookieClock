package com.tomtom.cookieclock;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tomtom.cookieclock.repository.GammerRepoHelper;
import com.tomtom.cookieclock.repository.GammerResultDAO;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public static final String GAMER1_NAME = "Mariusz";
    public static final String GAMER1_SURNAME = "Saramak";
    public static final String GAMMER_WITOLD_NAME = "Witold";

    public ApplicationTest() {
        super(Application.class);
    }


    public void testShouldSaveAndReadGammerResult(){
        //GIVEN gammer repo helper with one result
        GammerRepoHelper gammerRepoHelper = new GammerRepoHelper(getContext());

        //WHEN save one result
        gammerRepoHelper.saveGammerResult(GammerResultDAO.createGammerResult(GAMER1_NAME, GAMER1_SURNAME, 1000));
        //THEN one result should be save
        assert gammerRepoHelper.getResults().size() == 1;
        assert gammerRepoHelper.getResults().get(0).getName().equals(GAMER1_NAME);
        assert gammerRepoHelper.getResults().get(0).getSurname().equals(GAMER1_SURNAME);
        assert gammerRepoHelper.getResults().get(0).getTimeinMs() == 1000l;
    }

    public void testShouldSaveFewResultAndDisplayThemInCorrectTimeOrder(){
        //GIVEN gammer repo helper with few result
        GammerRepoHelper gammerRepoHelper = new GammerRepoHelper(getContext());
        //WHEN save one result
        gammerRepoHelper.saveGammerResult(GammerResultDAO.createGammerResult(GAMER1_NAME, GAMER1_SURNAME, 1002));
        gammerRepoHelper.saveGammerResult(GammerResultDAO.createGammerResult(GAMMER_WITOLD_NAME, "Olejniczak", 1001));
        //THEN results should be get in correct order
        assert gammerRepoHelper.getResults().size() == 2;
        assert gammerRepoHelper.getResults().get(0).getName().equals(GAMMER_WITOLD_NAME);
        assert gammerRepoHelper.getResults().get(1).getName().equals(GAMER1_NAME);

    }

    public void testShouldClearRepo(){
        //GIVEN gammer repo helper with few result
        GammerRepoHelper gammerRepoHelper = new GammerRepoHelper(getContext());
        //WHEN save one result
        gammerRepoHelper.saveGammerResult(GammerResultDAO.createGammerResult(GAMER1_NAME, GAMER1_SURNAME, 1002));
        gammerRepoHelper.saveGammerResult(GammerResultDAO.createGammerResult(GAMMER_WITOLD_NAME, "Olejniczak", 1001));
        //THEN clear should clear all table
        gammerRepoHelper.clear();
        assert gammerRepoHelper.getResults().size() == 0;
    }
}