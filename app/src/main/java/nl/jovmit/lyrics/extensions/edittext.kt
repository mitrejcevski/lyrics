package nl.jovmit.lyrics.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

fun textChangeListener(
    before: ((CharSequence, Int, Int, Int) -> Unit)? = null,
    onChanged: ((CharSequence, Int, Int, Int) -> Unit)? = null,
    after: ((Editable) -> Unit)? = null
): TextWatcher {

    return object : TextWatcher {

        override fun beforeTextChanged(
            sequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            sequence?.let { before?.invoke(it, start, count, after) }
        }

        override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
            sequence?.let { onChanged?.invoke(it, start, before, count) }
        }

        override fun afterTextChanged(editable: Editable?) {
            editable?.let { after?.invoke(it) }
        }
    }
}

fun EditText.onAnyTextChange(block: () -> Unit) {
    addTextChangedListener(textChangeListener(onChanged = { _, _, _, _ ->
        block()
    }))
}

fun TextInputLayout.resetError() {
    error = null
}

fun TextInputLayout.setError(@StringRes resource: Int) {
    error = context.getString(resource)
}