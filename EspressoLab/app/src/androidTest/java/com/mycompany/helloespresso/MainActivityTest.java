/*******************************************************************************
 * Â© Copyright 2015, James P White,  All Rights Reserved.
 * Created by Jim White on 11/12/2015.
 ******************************************************************************/
package com.mycompany.helloespresso;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Jim White on 6/20/2015.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);


    /*
    This test method checks the main activity when it is first displayed.  Here is what it needs to check for:
    that the eMail edit text is displayed and enabled, has focus and contains the appropriate eMail hint ("Your eMail")
    that the Find Me button is displayed and enabled
    that all the other view components are displayed but disabled (which may vary per widget) and contain hints as necessary.
    The bonus part of this lab requires you build a custom matcher to find the action bar to make sure it contains the app name text.
     */
    @Test
    public void initialDisplay() {
        // get rid of soft keyboard
        closeSoftKeyboard();
        // check for action bar title with new matcher
        onView(allOf(isDescendantOfA(withResourceName("android:id/action_bar_container")), withText(R.string.app_name)));
        onView(withId(R.id.emailET)).check(matches(allOf(isDisplayed(), isEnabled(), hasFocus(), withHint(R.string.email_hint))));
        onView(withId(R.id.findBT)).check(matches(allOf(isDisplayed(), isEnabled())));
        onView(withId(R.id.findBT)).check(isRightOf(withId(R.id.emailET)));

        onView(withId(R.id.nameET)).check(matches(allOf(isDisplayed(), not(isEnabled()), withHint(R.string.name_hint))));
        onView(withId(R.id.maleRB)).check(matches(allOf(isDisplayed(), not(isEnabled()), isNotChecked())));
        onView(withId(R.id.femaleRB)).check(matches(allOf(isDisplayed(), not(isEnabled()), isNotChecked())));
        onView(withId(R.id.seniorCB)).check(matches(allOf(isDisplayed(), not(isEnabled()), isNotChecked())));
        onView(withId(R.id.teamET)).check(matches(allOf(isDisplayed(), not(isEnabled()), withHint(R.string.team_hint))));
        onView(withId(R.id.sportSP)).check(matches(allOf(isDisplayed(), not(isClickable()))));
        onView(withId(R.id.submitBT)).check(matches(allOf(isDisplayed(), not(isEnabled()))));
    }

    /*
    This test method checks that the non-email view components remain disabled when no eMail entry has been made in the eMail edit text and
    the Find Me button is pushed.
     */
    @Test
    public void pushFindMeWithNoEmail() {
        // get rid of soft keyboard
        closeSoftKeyboard();
        // alternate to withId - withText
        onView(withHint(R.string.email_hint)).perform(clearText());
        onView(withText(R.string.find_button)).perform(click());

        // get rid of soft keyboard
        closeSoftKeyboard();
        onView(withId(R.id.nameET)).check(matches(allOf(isDisplayed(), not(isEnabled()), withHint(R.string.name_hint))));
        onView(withId(R.id.maleRB)).check(matches(allOf(isDisplayed(), not(isEnabled()), isNotChecked())));
        onView(withId(R.id.femaleRB)).check(matches(allOf(isDisplayed(), not(isEnabled()), isNotChecked())));
        onView(withId(R.id.seniorCB)).check(matches(allOf(isDisplayed(), not(isEnabled()), isNotChecked())));
        onView(withId(R.id.teamET)).check(matches(allOf(isDisplayed(), not(isEnabled()), withHint(R.string.team_hint))));
        onView(withId(R.id.sportSP)).check(matches(allOf(isDisplayed(), not(isClickable()))));
        onView(withId(R.id.submitBT)).check(matches(allOf(isDisplayed(), not(isEnabled()))));
    }

    /*
    This test checks that appropriate data is supplied to data entry widgets (name, gender radio buttons, favorite sport and team, etc.) when the Find Me
    button is pushed and an eMail address has been entered into the eMail edit text.  Specifically, it checks that the following conditions are satisfied after
    the Find Me button is clicked:
    that the Name edit text is enabled, displayed, and contains "Barrack Obama"
    that the gender radio buttons are enabled, displayed, and the male gender is on
    that the senior citizen check box is enabled, displayed and not checked
    that the favorite sport spinner is enabled, displayed and set to Basketball
    that the favorite team is enabled, displayed and set to "Chicago Bulls"
    that the Submit button is enabled and displayed.
     */
    @Test
    public void pushFindMeWithEmail() {
        // get rid of softkey board
        closeSoftKeyboard();
        // alternate to withId - withText
        onView(withHint(R.string.email_hint)).perform(typeText("joe@somebody.com"));
        onView(withText(R.string.find_button)).perform(click());

        // get rid of softkey board
        closeSoftKeyboard();
        onView(withId(R.id.nameET)).check(matches(allOf(isDisplayed(), isEnabled(), withText("Barrack Obama"))));
        onView(withId(R.id.maleRB)).check(matches(allOf(isDisplayed(), isEnabled(), isChecked())));
        onView(withId(R.id.femaleRB)).check(matches(allOf(isDisplayed(), isEnabled(), isNotChecked())));
        onView(withId(R.id.seniorCB)).check(matches(allOf(isDisplayed(), isEnabled(), isNotChecked())));
        onView(withId(R.id.teamET)).check(matches(allOf(isDisplayed(), isEnabled(), withText("Chicago Bulls"))));
        onData(allOf(is(instanceOf(String.class)), is("Basketball"))).check(matches(isDisplayed()));
        onView(withId(R.id.findBT)).check(isRightOf(withId(R.id.emailET)));
        onView(withId(R.id.submitBT)).check(matches(allOf(isDisplayed(), isEnabled())));
    }

    /*
    This test checks to ensure the About dialog is displayed when the About menu item is selected from the About overflow menu options.
    It also makes sure that pressing the back button when the dialog is displayed causes it to disappear and return to the main activity.
     */
    @Test
    public void pushAboutActionMenu() {
        // get rid of softkey board
        closeSoftKeyboard();
        // Open the overflow menu from contextual action mode.
        openContextualActionModeOverflowMenu();
        // Click on the About menu item.
        onView(withText("About")).perform(click());
        // check that dialog is displayed
        onView(withText(R.string.about_message)).check(matches(isDisplayed()));
        // hit back button to return to activity
        pressBack();
        onView(withId(R.id.emailET)).check(matches(allOf(isDisplayed(), isEnabled(), hasFocus(), withHint(R.string.email_hint))));
        onView(withId(R.id.findBT)).check(matches(allOf(isDisplayed(), isEnabled())));

    }


    /*
    Lab2 bonus - you need to add these methods in order to locate (or match) a View with the specific resource name.
     */
    private static Matcher<View> withResourceName(String resourceName) {
        return withResourceName(is(resourceName));
    }

    private static Matcher<View> withResourceName(final Matcher<String> resourceNameMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with resource name: ");
                resourceNameMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                int id = view.getId();
                return id != View.NO_ID && id != 0 && view.getResources() != null
                        && resourceNameMatcher.matches(view.getResources().getResourceName(id));
            }
        };
    }

}