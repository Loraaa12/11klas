# compilator i flagove kato gore
CC := gcc
CFLAGS := -Wall -Wextra -pedantic -std=c11
# tezi dva reda sa inspirenati ot tuk https://www.cs.colby.edu/maxwell/courses/tutorials/maketutor/

all: tail.c

# buildva q kato gcc flagove source -o i ime
tail.c:
	$(CC) $(CFLAGS) tail.c -o tail

# probva sus a i b
run: tail
	./tail example-inputs/a.txt example-inputs/b.txt

# probva s greshen file
test-error: tail
	./tail example-inputs/a.txt example-inputs/b.txt example-inputs/c.txt

clean:
	rm -f tail

