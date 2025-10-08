#build: gcc -Wall -Wextra -pedantic -std=c11 lseek-decode.c -o lseek-decode

#clean: rm lseek-decode

# compilator i flagove kato gore (vmesto -o)
CC := gcc
CFLAGS := -Wall -Wextra -pedantic -std=c11

# kato makrota 
PROGRAM := lseek-decode
SRC := lseek-decode.c

all: $(PROGRAM)

# buildva q kato gcc flagove source -o i ime
$(PROGRAM):
	$(CC) $(CFLAGS) $(SRC) -o $(PROGRAM)

# probva sus secret message 
run: $(PROGRAM)
	./$(PROGRAM) example-inputs/secret-message.hidden

# probva s greshen file
test-error: $(PROGRAM)
	./$(PROGRAM) nonexistent.txt

clean:
	rm -f $(PROGRAM)
