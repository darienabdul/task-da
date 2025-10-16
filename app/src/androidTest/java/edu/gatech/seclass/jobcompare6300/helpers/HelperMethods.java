package edu.gatech.seclass.jobcompare6300.helpers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;

public class HelperMethods {

    // scroll and click on element
    public ViewInteraction goToId(int id){
        return onView(withId(id)).perform(scrollTo(), click());
    }

    //short hand for match actual element text with expected
    public ViewAssertion checkText(String text){
        return matches(withText(text));
    }

    // scroll to element without clicking
    public ViewInteraction navigate(int id){
        return onView(withId(id)).perform(scrollTo());
    }



}
