package com.arkivanov.sample.shared.multipane

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.sample.shared.multipane.MultiPane.DetailsChild
import com.arkivanov.sample.shared.multipane.MultiPane.ListChild
import com.arkivanov.sample.shared.multipane.details.ArticleDetailsContent
import com.arkivanov.sample.shared.multipane.list.ArticleListContent

private val MULTI_PANE_WIDTH_THRESHOLD = 800.dp
private const val LIST_PANE_WEIGHT = 0.4F
private const val DETAILS_PANE_WEIGHT = 0.6F

@Composable
internal fun MultiPaneContent(component: MultiPane, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val model by component.models.subscribeAsState()
        val isMultiPane = model.isMultiPane

        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(if (isMultiPane) LIST_PANE_WEIGHT else 1F)) {
                ListPane(component.listRouterState)
            }

            if (isMultiPane) {
                Box(modifier = Modifier.weight(DETAILS_PANE_WEIGHT))
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (isMultiPane) {
                Box(modifier = Modifier.weight(LIST_PANE_WEIGHT))
            }

            Box(modifier = Modifier.weight(if (isMultiPane) DETAILS_PANE_WEIGHT else 1F)) {
                DetailsPane(component.detailsRouterState)
            }
        }

        val isMultiPaneRequired = this@BoxWithConstraints.maxWidth >= MULTI_PANE_WIDTH_THRESHOLD

        DisposableEffect(isMultiPaneRequired) {
            component.setMultiPane(isMultiPaneRequired)
            onDispose {}
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun ListPane(routerState: Value<RouterState<*, ListChild>>) {
    Children(
        routerState = routerState,
        animation = childAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is ListChild.List -> ArticleListContent(child.component)
            is ListChild.None -> Box {}
        }.let {}
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun DetailsPane(routerState: Value<RouterState<*, DetailsChild>>) {
    Children(
        routerState = routerState,
        animation = childAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is DetailsChild.None -> Box {}
            is DetailsChild.Details -> ArticleDetailsContent(child.component)
        }.let {}
    }
}
