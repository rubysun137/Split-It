package com.ruby.splitmoney.frienddetail;

import com.ruby.splitmoney.MainActivity;
import com.ruby.splitmoney.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class FriendDetailFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);


    @Test
    public void onCreateView() {
        onView(withId(R.id.home_friend_button)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.friend_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//        onView(withId(R.id.friend_detail_clear_balance)).check(doesNotExist());
//        onView(withId(R.id.friend_detail_who_owe)).check(matches(isDisplayed()));
//        onView(withId(R.id.friend_detail_clear_balance)).perform(click());
//        onView(withId(R.id.dialog_who_owe)).check(matches(isDisplayed()));
    }
}