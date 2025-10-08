# compilator i flagove kato gore (vmesto -o)
CC := gcc
CFLAGS := -Wall -Wextra -pedantic -std=c11
# tezi dva reda sa inspirenati ot tuk https://www.cs.colby.edu/maxwell/courses/tutorials/maketutor/

all: ls

# buildva q kato gcc flagove source -o i ime
ls: ls.c
	gcc $(CFLAGS) ls.c -o ls

# Runva sus primerite
run: ls
	./ls -l -A -R .

# Test s non existent file
test-error: ls
	./ls non_existent_file

clean:
	rm -f ls
