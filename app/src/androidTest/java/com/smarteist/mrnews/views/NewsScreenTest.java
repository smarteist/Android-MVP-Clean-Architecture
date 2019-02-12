package com.smarteist.mrnews.views;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;

import com.smarteist.mrnews.NavigationViewActions;
import com.smarteist.mrnews.R;
import com.smarteist.mrnews.util.EspressoIdlingResource;
import com.smarteist.mrnews.util.SimpleCountingIdlingResource;
import com.smarteist.mrnews.views.activities.NewsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewsScreenTest {
    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<NewsActivity> mNewsActivityTestRule =
            new ActivityTestRule<>(NewsActivity.class);

    /**
     * Prepare the test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests
     * significantly more reliable.
     */
    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Test scenario states:
     * Upon archiving a news (item), the tests checks whether that an item is being displayed
     * in the archived navigation list
     *
     * Note: this test should be executed while having no other items archived. If so, delete
     * the app's data in emulator/device: Settings -> Applications -> Mr. News -> Delete Data
     *
     * Note: we do not have to worry about any Espresso PerformException errors as our
     * EspressoIdlingResources take care of that in {@link SimpleCountingIdlingResource}
     */
    @Test
    public void clickArchiveNewsButtonOpenArchivedNews() {
            // quick check if the ListView is visible
            onView(withId(R.id.list_news)).check(matches(isDisplayed()));

            // click on the first news (item) from the list
            onData(allOf()).inAdapterView(withId(R.id.list_news)).atPosition(0)
                    .onChildView(withId(R.id.archive_news_ib))
                    .perform(click());

            // open drawer
            onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                    .perform(DrawerActions.open());

            // navigate to archived news
            onView(withId(R.id.nav_view))
                    .perform(NavigationViewActions.navigateTo(R.id.saved_navigation_menu_item));

            // Check if the previously archived news (item) is displayed
            // easiest way to check this is checking if the list has 1 item
            onView(withId(R.id.list_news)).check(ViewAssertions.matches(withListSize(1)));
        }

    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View> () {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }
}
