#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

#define BUFFER_SIZE 1
#define PROGRAM_NAME "./tail"

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

void print_last_lines(int fd, int lines_to_read) {
    char buffer[BUFFER_SIZE];
    off_t file_size = lseek(fd, 0, SEEK_END);
    if (file_size == -1) {
        perror("lseek");
        return;
    }

    off_t position = file_size;
    int line_count = 0;

    // chete naobratno za da prebroi redovete vuv faila
    while (position > 0 && line_count < lines_to_read) {
        position = lseek(fd, -BUFFER_SIZE, SEEK_CUR);
        if (position == -1) {
            perror("lseek");
            return;
        }

        ssize_t bytes_read = read(fd, buffer, BUFFER_SIZE);
        if (bytes_read < 0) {
            error_reading_file("Unknown file");
            return;
        }

        if (buffer[0] == '\n') {
            line_count++;
        }

        if (lseek(fd, -BUFFER_SIZE, SEEK_CUR) == -1 && position > 0) {
            perror("lseek");
            return;
        }
    }

    // slaga cursora pred poslednite redove
    if (lseek(fd, position, SEEK_SET) == -1) {
        perror("lseek");
        return;
    }

    // chete i printira poslednite redove
    ssize_t bytes_read;
    while ((bytes_read = read(fd, buffer, BUFFER_SIZE)) > 0) {
        if (write(STDOUT_FILENO, buffer, bytes_read) < 0) {
            error_writing_stdout();
            return;
        }
    }

    if (bytes_read < 0) {
        error_reading_file("Unknown file");
    }
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <file>...\n", argv[0]);
        return EXIT_FAILURE;
    }

    int lines_to_read = 10;
    int arg_index = 1;

    int print_filename = (argc - arg_index > 1);

    for (int i = arg_index; i < argc; i++) {
        const char *filename = argv[i];
        int fd = open(filename, O_RDONLY);

        if (fd < 0) {
            nonexistent(filename);
            continue;
        }

        if (print_filename) {
            printf("==> %s <==\n", filename);
        }

        print_last_lines(fd, lines_to_read);

        if (close(fd) < 0) {
            error_closing_file(filename);
        }
    }

    return EXIT_SUCCESS;
}


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>
#include <errno.h>

void copy_file(const char *src, const char *dest) {
    int src_fd, dest_fd;
    char buffer[4096];
    ssize_t bytes_read, bytes_written;

    src_fd = open(src, O_RDONLY);
    if (src_fd < 0) {
        perror("Error opening source file");
        exit(EXIT_FAILURE);
    }

    dest_fd = open(dest, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (dest_fd < 0) {
        perror("Error opening destination file");
        close(src_fd);
        exit(EXIT_FAILURE);
    }

    while ((bytes_read = read(src_fd, buffer, sizeof(buffer))) > 0) {
        bytes_written = write(dest_fd, buffer, bytes_read);
        if (bytes_written != bytes_read) {
            perror("Error writing to destination file");
            close(src_fd);
            close(dest_fd);
            exit(EXIT_FAILURE);
        }
    }

    if (bytes_read < 0) {
        perror("Error reading source file");
    }

    close(src_fd);
    close(dest_fd);
}

void copy_directory(const char *src, const char *dest) {
    DIR *src_dir = opendir(src);
    if (!src_dir) {
        perror("Error opening source directory");
        exit(EXIT_FAILURE);
    }

    struct stat st;
    if (stat(dest, &st) == -1) {
        if (mkdir(dest, 0755) < 0) {
            perror("Error creating destination directory");
            closedir(src_dir);
            exit(EXIT_FAILURE);
        }
    }

    struct dirent *entry;
    char src_path[4096], dest_path[4096];
    while ((entry = readdir(src_dir)) != NULL) {
        if (strcmp(entry->d_name, ".") == 0 || strcmp(entry->d_name, "..") == 0) {
            continue;
        }

        snprintf(src_path, sizeof(src_path), "%s/%s", src, entry->d_name);
        snprintf(dest_path, sizeof(dest_path), "%s/%s", dest, entry->d_name);

        if (entry->d_type == DT_DIR) {
            copy_directory(src_path, dest_path);
        } else if (entry->d_type == DT_REG) {
            copy_file(src_path, dest_path);
        }
    }

    closedir(src_dir);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <source> <destination>\n", argv[0]);
        return EXIT_FAILURE;
    }

    struct stat src_stat, dest_stat;

    if (stat(argv[1], &src_stat) < 0) {
        perror("Error accessing source");
        return EXIT_FAILURE;
    }

    if (stat(argv[2], &dest_stat) < 0 || !S_ISDIR(dest_stat.st_mode)) {
        fprintf(stderr, "Destination must be an existing directory.\n");
        return EXIT_FAILURE;
    }

    if (S_ISDIR(src_stat.st_mode)) {
        copy_directory(argv[1], argv[2]);
    } else if (S_ISREG(src_stat.st_mode)) {
        char dest_path[4096];
        snprintf(dest_path, sizeof(dest_path), "%s/%s", argv[2], strrchr(argv[1], '/') ? strrchr(argv[1], '/') + 1 : argv[1]);
        copy_file(argv[1], dest_path);
    } else {
        fprintf(stderr, "Source is not a regular file or directory.\n");
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}

