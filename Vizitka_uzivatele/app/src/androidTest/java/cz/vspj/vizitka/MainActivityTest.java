package cz.vspj.vizitka;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public ActivityScenarioRule<MainActivity> activity = new ActivityScenarioRule<>(MainActivity.class);

    @Test public void potvrzenyFormularZobraziUdaje() {
        onView(withId(R.id.jmeno)).perform(typeText("Jan Novak"));
        onView(withId(R.id.vek)).perform(typeText("25"));
        closeSoftKeyboard();
        onView(withId(R.id.souhlas)).perform(scrollTo(), click());
        onView(withId(R.id.odeslat)).perform(scrollTo(), click());
        onView(withId(R.id.vysledek)).check(matches(withText("Jméno: Jan Novak\nVěk: 25\nSystém: Android")));
    }

    @Test public void bezSouhlasuSeVysledekNezobrazi() {
        onView(withId(R.id.jmeno)).perform(typeText("Jan Novak"));
        onView(withId(R.id.vek)).perform(typeText("25"));
        closeSoftKeyboard();
        onView(withId(R.id.odeslat)).perform(scrollTo(), click());
        onView(withId(R.id.vysledek)).check(matches(withText("")));
    }
}
