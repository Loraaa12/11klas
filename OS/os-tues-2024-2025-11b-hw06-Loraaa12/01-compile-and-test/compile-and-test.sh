#!/bin/bash

SRC_DIR="src"
BIN_DIR="bin"
LOG_DIR="logs"

mkdir -p "$SRC_DIR" "$BIN_DIR" "$LOG_DIR"

if [ $# -lt 1 ]; then
  echo "Usage: $0 <source_file.c> [args...]"
  exit 1
fi

SOURCE_FILE="$1"
shift
ARGS="$@"
BASENAME=$(basename "$SOURCE_FILE" .c)
EXEC_FILE="$BIN_DIR/$BASENAME.out"

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
LOG_FILE="$LOG_DIR/log_$TIMESTAMP.txt"

echo "[$TIMESTAMP] Compiling $SRC_DIR/$SOURCE_FILE..." | tee -a "$LOG_FILE"
gcc "$SRC_DIR/$SOURCE_FILE" -o "$EXEC_FILE" 2>>"$LOG_FILE"

if [ $? -eq 0 ]; then
  echo "[$TIMESTAMP] Compilation successful." | tee -a "$LOG_FILE"
  echo "[$TIMESTAMP] Running $EXEC_FILE $ARGS..." | tee -a "$LOG_FILE"
  "$EXEC_FILE" $ARGS >>"$LOG_FILE" 2>&1
  EXIT_CODE=$?
  echo "[$TIMESTAMP] Program exited with code $EXIT_CODE." | tee -a "$LOG_FILE"
else
  echo "[$TIMESTAMP] Compilation failed." | tee -a "$LOG_FILE"
  exit 1
fi
