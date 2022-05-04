package com.arkivanov.sample.counter.shared.ui.android

import android.content.res.Configuration
import android.view.View
import android.widget.LinearLayout
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.android.RouterView
import com.arkivanov.decompose.extensions.android.ViewContext
import com.arkivanov.decompose.extensions.android.layoutInflater
import com.arkivanov.decompose.extensions.android.resources
import com.arkivanov.sample.counter.shared.R
import com.arkivanov.sample.counter.shared.tab.Tab

@ExperimentalDecomposeApi
@Suppress("FunctionName") // Factory function
fun ViewContext.TabView(tab: Tab): View {
    val layout = layoutInflater.inflate(R.layout.counter_tab, parent, false) as LinearLayout
    val firstRouter: RouterView = layout.findViewById(R.id.router_first)
    val secondRouter: RouterView = layout.findViewById(R.id.router_second)

    layout.orientation =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.HORIZONTAL
        } else {
            LinearLayout.VERTICAL
        }

    firstRouter.children(tab.firstRouterState, lifecycle) { parent, child, _ ->
        parent.removeAllViews()
        parent.addView(CounterView(child))
    }

    secondRouter.children(tab.secondRouterState, lifecycle) { parent, child, _ ->
        parent.removeAllViews()
        parent.addView(CounterView(child))
    }

    return layout
}
