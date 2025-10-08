#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

#define BUFFER_SIZE 1
#define PROGRAM_NAME "./head"

void nonexistent(const char *filename) {
    fprintf(stderr, "%s: cannot open '%s' for reading: %s\n", 
            PROGRAM_NAME, filename, strerror(errno));
}

void error_writing_stdout() {
    fprintf(stderr, "%s: error writing 'standard output': %s\n", 
            PROGRAM_NAME, strerror(errno));
}

void error_closing_file(const char *filename) {
    fprintf(stderr, "%s: error closing '%s': %s\n", 
            PROGRAM_NAME, filename, strerror(errno));
}

void error_reading_file(const char *filename) {
    fprintf(stderr, "%s: error reading '%s': %s\n", 
            PROGRAM_NAME, filename, strerror(errno));
}

void print_lines(int fd, int lines_to_read) {
    char buffer[BUFFER_SIZE];
    int line_count = 0;
    ssize_t bytes_read;

    while (line_count < lines_to_read && (bytes_read = read(fd, buffer, BUFFER_SIZE)) > 0) {
        if (write(STDOUT_FILENO, buffer, bytes_read) < 0) {
            error_writing_stdout();
            return;
        }
        if (buffer[0] == '\n') {
            line_count++;
        }
    }

    if (bytes_read < 0) {
        error_reading_file("Unknown file");
    }
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s [-n <number>] <file>...\n", argv[0]);
        return EXIT_FAILURE;
    }

    int lines_to_read = 10;
    int arg_index = 1;

    // Checkva za -n
    if (argc >= 3 && strcmp(argv[1], "-n") == 0) {
        if (argc < 4) {
            fprintf(stderr, "Usage: %s -n <number> <file>...\n", argv[0]);
            return EXIT_FAILURE;
        }
        lines_to_read = atoi(argv[2]);
        if (lines_to_read <= 0) {
            fprintf(stderr, "%s: invalid number of lines: %s\n", PROGRAM_NAME, argv[2]);
            return EXIT_FAILURE;
        }
        arg_index = 3; // tova pochva ot sled numbera 
    }

    int print_filename = (argc - arg_index > 1); // smqta kolko filea ima, dori i da ima nomer

    for (int i = arg_index; i < argc; i++) {
        const char *filename = argv[i];
        // otvarq faila v read only
        int fd = open(filename, O_RDONLY);

      // ako ne moje da otvori faila
        if (fd < 0) {
            nonexistent(filename);
            continue;
        }

        // ako ima samo edin fail ne psieh file name
        if (print_filename) {
          printf("==> %s <==\n", filename);
        }

        print_lines(fd, lines_to_read);

        if (close(fd) < 0) {
            error_closing_file(filename);
        }
    }

    return EXIT_SUCCESS;
}
