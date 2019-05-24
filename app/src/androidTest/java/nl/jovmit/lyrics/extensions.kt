package nl.jovmit.lyrics

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.AutoCompleteTextView
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

val toolbarBackButton: ViewInteraction = onView(withContentDescription(R.string.abc_action_bar_up_description))

val isDisplayed: ViewAssertion = matches(isDisplayed())

val isNotDisplayed: ViewAssertion = matches(not(isDisplayed()))

val doesNotExist: ViewAssertion = doesNotExist()

val gone = ViewMatchers.Visibility.GONE

val visible = ViewMatchers.Visibility.VISIBLE

fun toolbarWithTitle(@StringRes title: Int): ViewInteraction =
        onView(allOf(withText(title), withParent(isAssignableFrom(Toolbar::class.java))))

fun childInRecycler(rule: ActivityTestRule<*>, @IdRes id: Int, child: Int): View =
        childInRecycler(view(rule, id), child)

fun childInRecycler(recycler: RecyclerView, child: Int): View =
        recycler.getChildAt(child)

fun <T : View> view(rule: ActivityTestRule<*>, @IdRes id: Int): T =
        rule.activity.findViewById(id)

fun text(@StringRes resource: Int): ViewInteraction = onView(withText(resource))

fun text(value: String): ViewInteraction = onView(withText(value))

infix fun Int.check(action: ViewAssertion): ViewInteraction = onView(withId(this)).check(action)

infix fun Int.perform(action: ViewAction): ViewInteraction = onView(withId(this)).perform(action)

fun Int.perform(vararg actions: ViewAction): ViewInteraction = onView(withId(this)).perform(*actions)

infix fun ViewInteraction.check(action: ViewAssertion): ViewInteraction = this.check(action)

infix fun ViewInteraction.perform(action: ViewAction): ViewInteraction = this.perform(action)

infix fun View.hasVisibility(visibility: Int): Boolean = this.visibility == visibility

infix fun ViewInteraction.hasText(@StringRes string: Int): ViewInteraction = check(matches(withText(string)))

infix fun ViewInteraction.hasHint(@StringRes string: Int): ViewInteraction = check(matches(matchesWithHint(string)))

infix fun ViewInteraction.hasText(text: String): ViewInteraction = check(matches(withText(text)))

infix fun Int.hasText(@StringRes resource: Int): ViewInteraction = onView(withId(this)) hasText resource

infix fun Int.hasHint(@StringRes resource: Int): ViewInteraction = onView(withId(this)) hasHint resource

infix fun Int.hasText(text: String): ViewInteraction = onView(withId(this)) hasText text

infix fun Int.hasVisibility(visibility: Visibility): ViewAssertion = matches(withEffectiveVisibility(visibility))

infix fun Int.atIndex(index: Int): ViewInteraction = onView(withIndex(withId(this), index))

infix fun Int.typeSearchViewQuery(query: String): ViewInteraction {
    this perform click()
    return onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(typeText(query))
}
