package com.arkivanov.sample.shared.counters

import com.arkivanov.sample.shared.RProps
import com.arkivanov.sample.shared.counters.counter.CounterContent
import com.arkivanov.sample.shared.uniqueKey
import com.arkivanov.sample.shared.useAsState
import csstype.AlignItems
import csstype.BoxSizing
import csstype.Display
import csstype.FlexDirection
import csstype.pct
import csstype.px
import mui.material.Box
import mui.material.Container
import mui.material.Paper
import mui.material.PaperVariant
import mui.material.Stack
import mui.material.StackDirection
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import mui.system.sx
import react.FC
import react.key

val CountersContent: FC<RProps<Counters>> = FC { props ->
    val leftRouterState by props.component.firstRouterState.useAsState()
    val rightRouterState by props.component.secondRouterState.useAsState()

    Box {
        sx {
            width = 100.pct
            padding = 16.px
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
        }

        Paper {
            variant = PaperVariant.outlined

            sx {
                display = Display.inlineBlock
            }

            Stack {
                direction = ResponsiveStyleValue(StackDirection.row)
                spacing = ResponsiveStyleValue(2)

                sx {
                    boxSizing = BoxSizing.borderBox
                    padding = 16.px
                }

                CounterContent {
                    component = leftRouterState.activeChild.instance
                    key = component.uniqueKey()
                }

                CounterContent {
                    component = rightRouterState.activeChild.instance
                    key = component.uniqueKey()
                }
            }
        }
    }
}
