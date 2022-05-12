package com.arkivanov.sample.shared.dynamicfeatures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.sample.shared.dynamicfeatures.dynamicfeature.DynamicFeature

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun <T : Any> DynamicFeatureContent(
    dynamicFeature: DynamicFeature<T>,
    modifier: Modifier = Modifier,
    contentSupplier: () -> DynamicFeatureContent<T>,
) {
    Children(
        routerState = dynamicFeature.routerState,
        modifier = modifier,
        animation = childAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is DynamicFeature.Child.Loading -> Loading(name = child.name, modifier = Modifier.fillMaxSize())

            is DynamicFeature.Child.Feature -> {
                val content = remember(contentSupplier)
                content(component = child.feature, modifier = Modifier.fillMaxSize())
            }

            is DynamicFeature.Child.Error -> Error(name = child.name, modifier = Modifier.fillMaxSize())
        }.let {}
    }
}

@Composable
private fun Loading(name: String, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Loading $name...")
    }
}

@Composable
private fun Error(name: String, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Error loading $name...")
    }
}
