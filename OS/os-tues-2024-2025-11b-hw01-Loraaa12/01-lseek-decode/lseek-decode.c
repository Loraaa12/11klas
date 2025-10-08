#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

typedef struct {
    char data;
    unsigned char next_element_address;
} block;

#define BLOCK_SIZE sizeof(block)

// errno e global var kudeto se storeva tipa greahka. sterror go pravi na choveshki ezik, i funciqta go printi v stderr, koeto e kato stdio ama za errori
void print_error(const char *filename) {
    fprintf(stderr, "./sh: %s: %s\n", filename, strerror(errno));
}

int main(int argc, char *argv[]) {
  // proverqva za pravilniq broi arg
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <file>\n", argv[0]);
        return EXIT_FAILURE;
    }

    const char *filename = argv[1];
    // otvarq faila v read only
    int fd = open(filename, O_RDONLY);

    if (fd < 0) {
        print_error(filename);
        return EXIT_FAILURE;
    }

    block current_block;
    off_t offset = 0;

    while (1) {
      // proverwva dali moje da dostupi faila
        if (lseek(fd, offset, SEEK_SET) == (off_t) -1) {
            print_error(filename);
            close(fd);
            return EXIT_FAILURE;
        }

        // chete blocka
        ssize_t bytes_read = read(fd, &current_block, BLOCK_SIZE);
        //ako ne moje da prochete nishto vrushta -1
        if (bytes_read < 0) {
            print_error(filename);
            close(fd);
            return EXIT_FAILURE;
        } else if (bytes_read != BLOCK_SIZE) {
            fprintf(stderr, "Error: Incomplete block read\n");
            close(fd);
            return EXIT_FAILURE;
        }

        // print kum consolata
        write(STDOUT_FILENO, &current_block.data, 1);

        // checkva dali sme na posledniq element
        if (current_block.next_element_address == 0) {
            break;
        }

        // smenq elementa
        offset = current_block.next_element_address * BLOCK_SIZE;
    }

    close(fd);
    return EXIT_SUCCESS;
}
