#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <semaphore.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>

typedef struct {
    int hydrogens_waiting;
    int oxygens_waiting;
    int molecule_id_counter;
    sem_t mutex;
    sem_t hydroQueue;
    sem_t oxyQueue;
} SharedData;

SharedData *shared = NULL;

void bond(const char *type, int molecule_id) {
    printf("%s %d bonded in molecule %d\n", type, getpid(), molecule_id);
}

void hydrogen() {
    sem_wait(&shared->mutex);
    shared->hydrogens_waiting++;

    if (shared->hydrogens_waiting >= 2 && shared->oxygens_waiting >= 1) {
        sem_post(&shared->hydroQueue);
        sem_post(&shared->hydroQueue);
        shared->hydrogens_waiting -= 2;

        sem_post(&shared->oxyQueue);
        shared->oxygens_waiting -= 1;

        int molecule_id = ++shared->molecule_id_counter;

        sem_post(&shared->mutex);
        bond("Hydrogen", molecule_id);
    } else {
        sem_post(&shared->mutex);
        sem_wait(&shared->hydroQueue);
        sem_wait(&shared->mutex);
        int molecule_id = shared->molecule_id_counter;
        sem_post(&shared->mutex);
        bond("Hydrogen", molecule_id);
    }

    exit(0);
}

void oxygen() {
    sem_wait(&shared->mutex);
    shared->oxygens_waiting++;

    if (shared->hydrogens_waiting >= 2) {
        sem_post(&shared->hydroQueue);
        sem_post(&shared->hydroQueue);
        shared->hydrogens_waiting -= 2;

        sem_post(&shared->oxyQueue);
        shared->oxygens_waiting -= 1;

        int molecule_id = ++shared->molecule_id_counter;

        sem_post(&shared->mutex);
        bond("Oxygen", molecule_id);
    } else {
        sem_post(&shared->mutex);
        sem_wait(&shared->oxyQueue);
        sem_wait(&shared->mutex);
        int molecule_id = shared->molecule_id_counter;
        sem_post(&shared->mutex);
        bond("Oxygen", molecule_id);
    }

    exit(0);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <num_hydrogen> <num_oxygen>\n", argv[0]);
        exit(1);
    }

    int num_h = atoi(argv[1]);
    int num_o = atoi(argv[2]);

    shared = mmap(NULL, sizeof(SharedData), PROT_READ | PROT_WRITE,
                  MAP_SHARED | MAP_ANONYMOUS, -1, 0);
    if (shared == MAP_FAILED) {
        perror("mmap");
        exit(1);
    }

    shared->hydrogens_waiting = 0;
    shared->oxygens_waiting = 0;
    shared->molecule_id_counter = 0;

    sem_init(&shared->mutex, 1, 1);
    sem_init(&shared->hydroQueue, 1, 0);
    sem_init(&shared->oxyQueue, 1, 0);

    for (int i = 0; i < num_h; i++) {
        if (fork() == 0) {
            hydrogen();
        }
    }
    for (int i = 0; i < num_o; i++) {
        if (fork() == 0) {
            oxygen();
        }
    }

    for (int i = 0; i < num_h + num_o; i++) {
        wait(NULL);
    }

    sem_destroy(&shared->mutex);
    sem_destroy(&shared->hydroQueue);
    sem_destroy(&shared->oxyQueue);

    munmap(shared, sizeof(SharedData));

    return 0;
}
