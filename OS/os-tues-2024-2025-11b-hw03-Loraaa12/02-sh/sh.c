#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <errno.h>

#define MAX_INPUT 1024
#define PROMPT "$ "

void execute_command(char *input) {
    char *args[MAX_INPUT / 2 + 1]; 
    int i = 0;

    char *token = strtok(input, " \n");// namira purvata "duma" ot stringa i q zapazva v token
    while (token != NULL) {
        args[i++] = token; // slaga  dumata v masiva
        token = strtok(NULL, " \n"); // mesti s edna duma napred i produljava. tova stava tui kato strtok slaga \0 kogato e viknato => slaga null
    }
    args[i] = NULL; 

    if (args[0] == NULL) {
        return;
    }

    pid_t pid = fork();  // 26 i 28 sa ot https://www.geeksforgeeks.org/fork-system-call/

    if (pid < 0) {
        fprintf(stderr, "fork: Cannot allocate memory\n");
        return;
    } else if (pid == 0) {
        execvp(args[0], args); // smenq currenta vuv forka(childa) s podadeniq argument

        fprintf(stderr, "%s: %s\n", args[0], strerror(errno));
        exit(EXIT_FAILURE);
    } else {
        int status;
        waitpid(pid, &status, 0); //37 i 38 sa ot https://www.educative.io/answers/what-is-the-waitpid-system-
    }
}

int main() {
    char input[MAX_INPUT];

    while (1) {
        printf(PROMPT); // slaga $ za da "imitira" terminala
        fflush(stdout); // maha stdout buffera za da sme sigurni che se printira vednaga prompta
        // 47 e ot https://pubs.opengroup.org/onlinepubs/000095399/functions/fflush.html

        if (fgets(input, sizeof(input), stdin) == NULL) { // poluchava red ot stdin i go pishe v input, kato go chete do sizeof(input)-1 za da ne stane buffer overflow
            printf("\n");
            break;
        }

        size_t len = strlen(input);
        if (len > 0 && input[len - 1] == '\n') {
            input[len - 1] = '\0'; // maha \n zashtoto tva prechi na nqkoi komandi
        }

        execute_command(input);
    }

    return 0;
}

