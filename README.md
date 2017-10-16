# BBPhosphor
A tool to taint the branch based on Phosphor

## Directory

* `src`
    * `PreMain`, the controller,
    * `XDataAndControlFlowTagFactory`, 
    
* `tests/BBPhosphorTests`, a `helloworld` project for test

## Enviroment Setting

1. Have your phosphor built and the Java environment instrumented by phosphor

2. Build the phosphor-examples

3. Set the path in the `Makefile`.

Now you can run your test with "make"

* `make normal`, run the `phosphor-examples` with `phosphor`
* `make implicit`, run the `phosphor-examples` with `phosphor` with `lightImplicit` option.


* `make testphosphor`, run the `BBPhosphorTests` with `phosphor`
* `make testbb`, run the `BBPhosphorTests` with `BBPhosphor`

