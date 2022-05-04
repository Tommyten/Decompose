//
//  CounterView.swift
//  ios-app
//
//  Created by Arkadii Ivanov on 9/11/20.
//  Copyright © 2020 Arkadii Ivanov. All rights reserved.
//

import SwiftUI
import Counter

struct CounterView: View {
    private let counter: Counter
    
    @ObservedObject
    private var observableModel: ObservableValue<CounterModel>

    private var model: CounterModel { observableModel.value }
    
    init(_ counter: Counter) {
        self.counter = counter
        observableModel = ObservableValue(counter.model)
    }

    var body: some View {
        VStack(spacing: 8) {
            Text(model.title)
                .font(.title)
            
            Text(model.text)
            
            Button("Next", action: counter.onNextClicked)
            
            Button("Prev", action: counter.onPrevClicked)
                .disabled(!model.isBackEnabled)
        }
        .padding()
        .frame(width: 180)
        .border(Color.black, width: 2)
    }
}

struct CounterView_Previews: PreviewProvider {
    static var previews: some View {
        CounterView(CounterPreview())
    }
}

class CounterPreview : Counter {
    let model: Value<CounterModel> = mutableValue(
        CounterModel(
            title: "Counter 0",
            text: "123",
            isBackEnabled: false
        )
    )
    
    func onNextClicked() {}
    func onPrevClicked() {}
}
