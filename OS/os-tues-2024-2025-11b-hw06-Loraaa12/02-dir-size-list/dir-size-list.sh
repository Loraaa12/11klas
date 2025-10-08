#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <dir name> <size>"
    exit 1
fi

DIR_NAME=$1
MIN_SIZE=$2

if [ -d "$DIR_NAME" ]; then
    echo "Files in '$DIR_NAME' with size > $MIN_SIZE bites:"
    
    find "$DIR_NAME" -type f -size +"$MIN_SIZE"c -exec basename {} \;

else
    echo "Error: dir '$DIR_NAME' doesn't exist."
fi
