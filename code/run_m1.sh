#!/bin/bash

ant dist > /dev/null

for file in common/examples/m1/*
do
	echo ""
	echo ""
	echo "$file:"
	./fuc "$file"
done
