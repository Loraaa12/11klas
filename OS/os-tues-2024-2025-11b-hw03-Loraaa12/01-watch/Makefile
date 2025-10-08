CC = gcc
CFLAGS = -Wall -Wextra -pedantic -std=c99 -g

TARGET = watch
SRCS = watch.c
OBJS = $(SRCS:.c=.o)

all: watch

watch: $(OBJS)
	gcc $(CFLAGS) -o $@ $^

%.o: %.c
	gcc $(CFLAGS) -c $< -o $@

clean:
	rm -f $(OBJS) watch
