#build:gcc -Wall -Wextra -pedantic -std=c11 head.c -o head

#clean:rm head

# compilator i flagove kato gore (vmesto -o)
CC := gcc
CFLAGS := -Wall -Wextra -pedantic -std=c11

# kato makrota 
PROGRAM := head
SRC :=head.c

all: $(PROGRAM)

# buildva q kato gcc flagove source -o i ime
$(PROGRAM):
	$(CC) $(CFLAGS) $(SRC) -o $(PROGRAM)

# probva sus a i b
run: $(PROGRAM)
	./$(PROGRAM) example-inputs/a.txt example-inputs/b.txt

# probva s greshen file
test-error: $(PROGRAM)
	./$(PROGRAM) example-inputs/a.txt example-inputs/b.txt example-inputs/c.txt

clean:
	rm -f $(PROGRAM)
