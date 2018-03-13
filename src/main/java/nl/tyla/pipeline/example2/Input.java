package nl.tyla.pipeline.example2;

import java.util.List;

class Input {

    private final List<String> threeInput;

    Input(List<String> threeInput) {
        this.threeInput = threeInput;
    }

    List<String> getThreeInput() {
        return threeInput;
    }
}
