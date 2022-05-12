package com.arkivanov.sample.shared.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.router.webhistory.WebHistoryController
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.sample.shared.counters.CountersComponent
import com.arkivanov.sample.shared.dynamicfeatures.DynamicFeaturesComponent
import com.arkivanov.sample.shared.dynamicfeatures.dynamicfeature.FeatureInstaller
import com.arkivanov.sample.shared.multipane.MultiPaneComponent
import com.arkivanov.sample.shared.root.Root.Child
import com.arkivanov.sample.shared.root.Root.Child.CountersChild
import com.arkivanov.sample.shared.root.Root.Child.DynamicFeaturesChild
import com.arkivanov.sample.shared.root.Root.Child.MultiPaneChild

@OptIn(ExperimentalDecomposeApi::class)
class RootComponent constructor(
    componentContext: ComponentContext,
    private val featureInstaller: FeatureInstaller,
    deepLink: DeepLink = DeepLink.None,
    webHistoryController: WebHistoryController? = null,
) : Root, ComponentContext by componentContext {

    private val router: Router<Config, Child> =
        router(
            initialStack = { getInitialStack(deepLink) },
            childFactory = ::child,
        )

    override val routerState: Value<RouterState<*, Child>> = router.state

    init {
        webHistoryController?.attach(
            router = router,
            getPath = { config ->
                when (config) {
                    Config.Counters -> "/a"
                    Config.MultiPane -> "/b"
                    Config.DynamicFeatures -> "/c"
                }
            },
            getConfiguration = {
                when (it.removePrefix("/")) {
                    "a" -> Config.Counters
                    "b" -> Config.MultiPane
                    "c" -> Config.DynamicFeatures
                    else -> Config.Counters
                }
            },
        )
    }

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Counters -> CountersChild(CountersComponent(componentContext))
            is Config.MultiPane -> MultiPaneChild(MultiPaneComponent(componentContext))
            is Config.DynamicFeatures -> DynamicFeaturesChild(DynamicFeaturesComponent(componentContext, featureInstaller))
        }

    override fun onCountersTabClicked() {
        router.bringToFront(Config.Counters)
    }

    override fun onMultiPaneTabClicked() {
        router.bringToFront(Config.MultiPane)
    }

    override fun onDynamicFeaturesTabClicked() {
        router.bringToFront(Config.DynamicFeatures)
    }

    private companion object {
        private fun getInitialStack(deepLink: DeepLink): List<Config> =
            when (deepLink) {
                is DeepLink.None -> listOf(Config.Counters)

                is DeepLink.Web ->
                    listOf(
                        when (deepLink.path.removePrefix("/")) {
                            "a" -> Config.Counters
                            "b" -> Config.MultiPane
                            "c" -> Config.DynamicFeatures
                            else -> Config.Counters
                        }
                    )
            }
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object Counters : Config

        @Parcelize
        object MultiPane : Config

        @Parcelize
        object DynamicFeatures : Config
    }

    sealed interface DeepLink {
        object None : DeepLink
        class Web(val path: String) : DeepLink
    }
}
