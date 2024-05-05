package com.arman.kitomjo

import android.util.Log
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.performScrollToNode

internal fun SemanticsNode.getEditableTextFromNode(): String =
    config.filter { it.key.name == "EditableText" }[0].value.toString()

@OptIn(ExperimentalTestApi::class)
internal fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 5000L
) {
    return waitUntilNodeCount(matcher, 1, timeoutMillis)
}

internal fun SemanticsNode.assertEditableValue(value: String) {
    assert(config.filter { it.key.name == "EditableText" }[0].value.toString() == value)
}

internal fun SemanticsNodeInteraction.tryScrollToNode(matcher: SemanticsMatcher): SemanticsNodeInteraction =
    try {
        performScrollToNode(matcher)
    } catch (ex: AssertionError) {
        Log.e("tryScrollTo", ex.message.toString())
        this
    }