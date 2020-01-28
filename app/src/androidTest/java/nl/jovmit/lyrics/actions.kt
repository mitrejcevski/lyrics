package nl.jovmit.lyrics

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

fun makeVisible(): ViewAction {

    return object : ViewAction {
        override fun getDescription(): String = "Make view visible"

        override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)

        override fun perform(uiController: UiController?, view: View?) {
            view?.visibility = View.VISIBLE
        }
    }
}

fun submitText(text: String): ViewAction {
    return object : ViewAction {

        override fun getDescription(): String = "Submit query"

        override fun getConstraints(): Matcher<View> = isAssignableFrom(SearchView::class.java)

        override fun perform(uiController: UiController, view: View) {
            (view as SearchView).setQuery(text, true)
        }
    }
}