#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/wait.h>

#define PROGRAM_NAME "./watch"

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

void error_executing_fork() {
    fprintf(stderr, "%s: fork: Cannot allocate memory\n", PROGRAM_NAME);
}

int validate_command(const char *cmd) {
    if (access(cmd, X_OK) != 0) { // x_ok testva dali cmd moje da e accessnat za execution
        nonexistent(cmd);
        return 0; 
    }
    return 1;
}

void execute_watch(int argc, char *argv[]) {
      if (!validate_command(argv[1])) {
        return;
    }

    while (1) { // infinite loopa pochva
        pid_t pid = fork(); // red 48 i 50 sa ot https://www.geeksforgeeks.org/fork-system-call/

        if (pid < 0) 
            error_executing_fork();
            sleep(2);
            continue; // probva pak 
        } else if (pid == 0) {
            execvp(argv[1], &argv[1]); // executeva commandata, priema za argumenti samata komanda i spisuka s ostanalite argumenti (kato "-u")
            
            fprintf(stderr, "%s: %s: %s\n", PROGRAM_NAME, argv[1], strerror(errno));
            exit(EXIT_FAILURE);
        } else {
            int status;
            //60 i 62 sa ot https://www.educative.io/answers/what-is-the-waitpid-system-
            if (waitpid(pid, &status, 0) == -1) {
              error_reading_file("child process status");
            }

            sleep(2);
        }
    }

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <command> [args...]\n", PROGRAM_NAME);
        return EXIT_FAILURE;
    }

    execute_watch(argc, argv);
    return EXIT_SUCCESS;
}
