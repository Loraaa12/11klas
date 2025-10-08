#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 N"
    exit 1
fi

if ! [[ "$1" =~ ^[0-9]+$ ]]; then
    echo "N must be > 0 whole num"
    exit 2
fi

N=$1

if [ "$N" -eq 0 ]; then
    echo "0"
    exit 0
elif [ "$N" -eq 1 ]; then
    echo "1"
    exit 0
fi

a=0
b=1

for (( i=2; i<=N; i++ ))
do
    fn=$((a + b))
    a=$b
    b=$fn
done

echo "$b"
