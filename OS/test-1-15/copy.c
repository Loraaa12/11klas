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

void copy_dir(const char *src, const char *dest){
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

int main(int argc, int *argv[]){
    if(argc < 3){
        fprintf(stderr, "Usage: %s <command> [args...]\n", PROGRAM_NAME);
        return EXIT_FAILURE;
    }

    struct stat src_stat, dist_stat;

    if(stat(argv[1], &src_stat) < 0){
        perror("Error accessing src");
        return EXIT_FAILURE;
    }

    if (stat(argv[2], &dest_stat) < 0 || !S_ISDIR(dest_stat.st_mode)) {
        perror("Destination dir must already exist");
        return EXIT_FAILURE;
    }

    if (S_ISDIR(src_stat.st_mode)) {
        copy_directory(argv[1], argv[2]);
    } else if (S_ISREG(src_stat.st_mode)) {
        char dest_path[4096];
        snprintf(dest_path, sizeof(dest_path), "%s/%s", argv[2], strrchr(argv[1], '/') ? strrchr(argv[1], '/') + 1 : argv[1]); //!!!!!!!!!!! 
        copy_file(argv[1], dest_path);
    }else {
        fprintf(stderr, "Src must be a file or directory.\n");
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}