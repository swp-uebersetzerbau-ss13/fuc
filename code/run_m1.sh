#!/bin/bash

ant dist > /dev/null

for file in common/examples/m1/*
do
	echo "$file:"
	ant run-without-build -Dsource.file=$file
done
