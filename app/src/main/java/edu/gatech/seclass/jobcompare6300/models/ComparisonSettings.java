package edu.gatech.seclass.jobcompare6300.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.EnumMap;
import java.util.Map;

import edu.gatech.seclass.jobcompare6300.logic.JobScorer;

public class ComparisonSettings {
    //shared prefs setup
    private static final String PREFS_NAME = "CompSettingsPrefs";
    private static final String KEY_SETTINGS = "compSettings";

    // class attributes
    public enum WeightKey {BONUS, SALARY, K401K, INTERNET, ACCIDENT, TUITION}
    private final Map<WeightKey, Integer> weights;

    public ComparisonSettings(){
        weights = new EnumMap<>(WeightKey.class);
        //default values
        for (WeightKey k: WeightKey.values()){
            weights.put(k, 1);
        }
    }

    public int totalWeight() {
        int total = 0;
        for (int k: weights.values()){
            total+=k;
        }
        return total;
    }

    public int getKey(WeightKey key){
        return weights.get(key);
    }

    public void setKey(WeightKey key, int value){
        if( value < 0 || value > 9){
            throw new IllegalArgumentException("Weight value must be between 0 and 9");
        } else{
            weights.put(key, value);
        }
    }

    public void saveCompSettings(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        editor.putString(KEY_SETTINGS, gson.toJson(this));
        editor.apply();

        JobScorer.clearCache();
    }

    public static ComparisonSettings loadCompSettings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String compSettingsJson = prefs.getString(KEY_SETTINGS, null);
        if (compSettingsJson != null) {
            return new Gson().fromJson(compSettingsJson, ComparisonSettings.class);
        }
        return new ComparisonSettings();
    }
}
