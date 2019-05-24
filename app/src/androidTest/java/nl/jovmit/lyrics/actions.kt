package nl.jovmit.lyrics

import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View
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