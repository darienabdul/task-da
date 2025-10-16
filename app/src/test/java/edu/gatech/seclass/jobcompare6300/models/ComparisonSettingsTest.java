package edu.gatech.seclass.jobcompare6300.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.gatech.seclass.jobcompare6300.models.ComparisonSettings.WeightKey;


public class ComparisonSettingsTest {

    private ComparisonSettings compSettings;

    @Before
    //each test has a fresh instance of comparison settings to work with (maybe change?)
    public void setup(){
        compSettings = new ComparisonSettings();
    }

    @Test
    // tests initial setup of comparison weights
    public void getKey_returnsInitialKey(){
        assertEquals(1, compSettings.getKey(WeightKey.SALARY));
        assertEquals(1, compSettings.getKey(WeightKey.BONUS));
        assertEquals(1, compSettings.getKey(WeightKey.K401K));
        assertEquals(1, compSettings.getKey(WeightKey.INTERNET));
        assertEquals(1, compSettings.getKey(WeightKey.ACCIDENT));
        assertEquals(1, compSettings.getKey(WeightKey.TUITION));
    }

    @Test
    // tests setter and getter for the comparison weights
    public void setKey_returnsCorrectKey(){
        for(WeightKey weight : WeightKey.values()){
            int randomWeight = (int)(Math.random() * 10); //random whole integer between 0-9
            compSettings.setKey(weight, randomWeight);
            assertEquals(randomWeight, compSettings.getKey(weight));
        }
    }

    @Test
    // tests invalid input of weights
    public void setKey_preventsInvalidInput(){
        assertThrows(IllegalArgumentException.class, () -> {
            compSettings.setKey(WeightKey.SALARY, 10);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            compSettings.setKey(WeightKey.TUITION, -1);
        });
    }

    @Test
    // tests totalWeight function
    public void totalWeight_returnsTotalWeight(){
        int randomTotal = 0;
        for(WeightKey weight : WeightKey.values()){
            int randomWeight = (int)(Math.random() * 10);
            compSettings.setKey(weight, randomWeight);
            randomTotal += randomWeight;
        }

        assertEquals(randomTotal, compSettings.totalWeight());
    }
}
