package nl.tyla.pipeline.example2;

class Result {
    private final Input input;
    private final ResultOne resultOne;
    private final ResultTwo resultTwo;
    private final ResultThree resultThree;

    Result(Input input, ResultOne resultOne, ResultTwo resultTwo, ResultThree resultThree) {
        this.input = input;
        this.resultOne = resultOne;
        this.resultTwo = resultTwo;
        this.resultThree = resultThree;
    }
}
