package nl.jovmit.lyrics.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.annotation.Dimension
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import nl.jovmit.lyrics.R
import java.util.concurrent.TimeUnit

class InfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private companion object {

        private const val DEFAULT_TIMEOUT = 1000L
    }

    private var timeout: Long = DEFAULT_TIMEOUT
    private var onDismissCallback: () -> Unit = {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        initialize()
    }

    private fun initialize() {
        visibility = View.GONE
        gravity = Gravity.CENTER
        setPadding(0, 16, 0, 16)
        setTextSize(Dimension.SP, 18f)
    }

    fun timeout(timeout: Long) {
        this.timeout = timeout
    }

    fun timeout(duration: Long, unit: TimeUnit) {
        timeout(unit.toMillis(duration))
    }

    fun setOnDismissCallback(onDismissCallback: () -> Unit) {
        this.onDismissCallback = onDismissCallback
    }

    fun displayError(@StringRes stringResource: Int) {
        displayError(context.getString(stringResource))
    }

    fun displayError(error: String) {
        setBackgroundResource(R.color.errorColor)
        setTextColor(ContextCompat.getColor(context, R.color.infoMessageTextColor))
        showUp(error)
    }

    fun displayInfo(@StringRes stringResource: Int) {
        displayInfo(context.getString(stringResource))
    }

    fun displayInfo(info: String) {
        setBackgroundResource(R.color.successColor)
        setTextColor(ContextCompat.getColor(context, R.color.infoMessageTextColor))
        showUp(info)
    }

    private fun showUp(message: String) {
        text = message
        val transition = CustomTransition()
        TransitionManager.beginDelayedTransition(parent as ViewGroup, transition)
        visibility = View.VISIBLE
        postDelayed({
            text = ""
            TransitionManager.beginDelayedTransition(parent as ViewGroup, transition)
            visibility = View.GONE
            onDismissCallback.invoke()
        }, timeout)
    }

    private class CustomTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            interpolator = OvershootInterpolator()
            addTransition(Fade())
            addTransition(ChangeBounds())
        }
    }
}