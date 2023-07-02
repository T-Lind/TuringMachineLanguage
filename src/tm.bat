@echo off

if "%~1"=="" (
    java -classpath "C:\Program Files (x86)\T-Lind\Turing Machine Language" org.tlind.turingmachine.TuringInterpreter
) else (
    java -classpath "C:\Program Files (x86)\T-Lind\Turing Machine Language" org.tlind.turingmachine.TuringInterpreter %1
)
