@echo off
if "%~1"=="" (
    java -classpath "%TML_PATH%" com.github.tlind.turingmachine.TuringInterpreter
) else (
    java -classpath "%TML_PATH%" com.github.tlind.turingmachine.TuringInterpreter %1
)
