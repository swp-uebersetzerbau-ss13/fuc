#!/bin/sh
java -cp ast.jar:backend.jar:controller.jar:interfaces.jar:intermediateCodeGenerator.jar:lexer.jar:parser.jar:symbolTable.jar:log4jprops.jar controller.Controller
