#!/bin/bash

if [ "$#" -lt 1 ]; then
    echo "Usage: $0 file1 [file2 ...]"
    exit 1
fi

echo -n "String: "
read SEARCH_STRING

for FILE in "$@"; do
    if [ -f "$FILE" ]; then
        MATCH_COUNT=$(grep -c "$SEARCH_STRING" "$FILE")
        echo "File: $FILE - Match: $MATCH_COUNT"
    else
        echo "Error: '$FILE' doesn't exist / isn't a regular file."
    fi
done
