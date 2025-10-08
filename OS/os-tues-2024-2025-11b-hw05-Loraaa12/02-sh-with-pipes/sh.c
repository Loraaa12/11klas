#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <errno.h>

#define MAX_ARGS 100
#define MAX_COMMANDS 100

void parse_command(char *input, char **args) {
    int i = 0;
    char *token = strtok(input, " \t\n");
    while (token != NULL && i < MAX_ARGS - 1) {
        args[i++] = token;
        token = strtok(NULL, " \t\n");
    }
    args[i] = NULL;
}

void parse_pipeline(char *input, char **commands) {
    int i = 0;
    char *token = strtok(input, "|");
    while (token != NULL && i < MAX_COMMANDS - 1) {
        commands[i++] = token;
        token = strtok(NULL, "|");
    }
    commands[i] = NULL;
}

void execute_pipeline(char **commands) {
    int num_cmds = 0;
    while (commands[num_cmds] != NULL)
        num_cmds++;

    int pipes[num_cmds - 1][2];

    for (int i = 0; i < num_cmds - 1; i++) {
        if (pipe(pipes[i]) == -1) {
            perror("pipe");
            exit(EXIT_FAILURE);
        }
    }

    for (int i = 0; i < num_cmds; i++) {
        pid_t pid = fork();
        if (pid < 0) {
            perror("fork");
            exit(EXIT_FAILURE);
        }
        if (pid == 0) {
            if (i > 0) {
                dup2(pipes[i-1][0], STDIN_FILENO);
            }

            if (i < num_cmds - 1) {
                dup2(pipes[i][1], STDOUT_FILENO);
            }

            for (int j = 0; j < num_cmds - 1; j++) {
                close(pipes[j][0]);
                close(pipes[j][1]);
            }

            char *args[MAX_ARGS];
            parse_command(commands[i], args);

            if (access(args[0], X_OK) != 0) {
                fprintf(stderr, "%s: No such file or directory\n", args[0]);
                exit(EXIT_FAILURE);
            }

            execvp(args[0], args);
            perror("execvp");
            exit(EXIT_FAILURE);
        }
    }

    for (int i = 0; i < num_cmds - 1; i++) {
        close(pipes[i][0]);
        close(pipes[i][1]);
    }

    for (int i = 0; i < num_cmds; i++) {
        wait(NULL);
    }
}

int main() {
    char *line = NULL;
    size_t len = 0;

    while (1) {
        printf("$ ");
        fflush(stdout);

        if (getline(&line, &len, stdin) == -1) {
            break; 
        }
        if (strcmp(line, "\n") == 0) {
            continue;
        }

        char *commands[MAX_COMMANDS];
        parse_pipeline(line, commands);

        if (commands[1] == NULL) {
            char *args[MAX_ARGS];
            parse_command(commands[0], args);

            pid_t pid = fork();
            if (pid < 0) {
                perror("fork");
                continue;
            }
            if (pid == 0) {
                if (access(args[0], X_OK) != 0) {
                    fprintf(stderr, "%s: No such file or directory\n", args[0]);
                    exit(EXIT_FAILURE);
                }
                execvp(args[0], args);
                perror("execvp");
                exit(EXIT_FAILURE);
            } else {
                waitpid(pid, NULL, 0);
            }
        } else {
            execute_pipeline(commands);
        }
    }

    free(line);
    return 0;
}
