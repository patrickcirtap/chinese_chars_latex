#!/bin/bash

echo "请等一下..." &&
echo "compiling Main..." &&
    javac Main.java CharacterObject.java &&
echo "main a..."
    java Main a > characters.tex     && xelatex characters.tex     > /dev/null &&
echo "main b..."
    java Main b > characterslong.tex && xelatex characterslong.tex > /dev/null &&
echo "rm..."
    rm *.class *.aux *.log &&
echo "完成了"
