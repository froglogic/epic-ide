#!/bin/sh

cd org.epic.perleditor/src/org/epic/core/parser
for grammar in *.g; do
    java -cp ../../../../../../org.epic.lib/lib/antlr-2.7.5.jar antlr.Tool $grammar
done
