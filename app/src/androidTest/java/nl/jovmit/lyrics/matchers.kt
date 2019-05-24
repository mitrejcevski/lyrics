package nl.jovmit.lyrics

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> = object : TypeSafeMatcher<View>() {

    var currentIndex = 0

    override fun describeTo(description: Description) {
        description.appendText("with index: ")
        description.appendValue(index)
        matcher.describeTo(description)
    }

    override fun matchesSafely(view: View): Boolean {
        return matcher.matches(view) && currentIndex++ == index
    }
}

fun matchesWithHint(resource: Int): Matcher<View> {
    return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

        public override fun matchesSafely(textView: TextInputLayout): Boolean {
            val expected = textView.resources.getString(resource)
            val actual = textView.hint!!.toString()
            return actual == expected
        }

        override fun describeTo(description: Description) {
            description.appendText("with hint: ")
        }
    }
}