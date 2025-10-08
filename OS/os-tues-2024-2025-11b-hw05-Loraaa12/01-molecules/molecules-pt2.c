// h2o_named.c
#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <fcntl.h>      // За O_* константи
#include <semaphore.h>
#include <string.h>
#include <errno.h>

#define SEM_MUTEX_NAME "/h2o_mutex"
#define SEM_HYDRO_NAME "/h2o_hydroQueue"
#define SEM_OXY_NAME   "/h2o_oxyQueue"

typedef struct {
    int hydrogens_waiting;
    int oxygens_waiting;
    int molecule_id_counter;
} shared_data_t;

shared_data_t *shared_data = NULL;
sem_t *mutex = NULL;
sem_t *hydroQueue = NULL;
sem_t *oxyQueue = NULL;

void bond(const char *type, pid_t pid, int molecule_id) {
    printf("%s %d bonded in molecule %d\n", type, pid, molecule_id);
}

void init_semaphores() {
    mutex = sem_open(SEM_MUTEX_NAME, O_CREAT | O_EXCL, 0666, 1);
    hydroQueue = sem_open(SEM_HYDRO_NAME, O_CREAT | O_EXCL, 0666, 0);
    oxyQueue = sem_open(SEM_OXY_NAME, O_CREAT | O_EXCL, 0666, 0);

    if (mutex == SEM_FAILED || hydroQueue == SEM_FAILED || oxyQueue == SEM_FAILED) {
        perror("sem_open failed");
        exit(1);
    }
}

void open_semaphores() {
    mutex = sem_open(SEM_MUTEX_NAME, 0);
    hydroQueue = sem_open(SEM_HYDRO_NAME, 0);
    oxyQueue = sem_open(SEM_OXY_NAME, 0);

    if (mutex == SEM_FAILED || hydroQueue == SEM_FAILED || oxyQueue == SEM_FAILED) {
        perror("sem_open (child) failed");
        exit(1);
    }
}

void destroy_semaphores() {
    sem_unlink(SEM_MUTEX_NAME);
    sem_unlink(SEM_HYDRO_NAME);
    sem_unlink(SEM_OXY_NAME);
}

void hydrogen() {
    open_semaphores();

    int shm_fd = shm_open("/h2o_shm", O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("shm_open hydrogen");
        exit(1);
    }
    shared_data = mmap(NULL, sizeof(shared_data_t), PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    close(shm_fd);

    sem_wait(mutex);
    shared_data->hydrogens_waiting++;

    if (shared_data->hydrogens_waiting >= 2 && shared_data->oxygens_waiting >= 1) {
        sem_post(hydroQueue);
        sem_post(hydroQueue);
        sem_post(oxyQueue);
        shared_data->hydrogens_waiting -= 2;
        shared_data->oxygens_waiting -= 1;
        shared_data->molecule_id_counter++;
        int molecule_id = shared_data->molecule_id_counter;
        sem_post(mutex);

        bond("Hydrogen", getpid(), molecule_id);
    } else {
        sem_post(mutex);
        sem_wait(hydroQueue);

        sem_wait(mutex);
        int molecule_id = shared_data->molecule_id_counter;
        sem_post(mutex);

        bond("Hydrogen", getpid(), molecule_id);
    }

    munmap(shared_data, sizeof(shared_data_t));
    sem_close(mutex);
    sem_close(hydroQueue);
    sem_close(oxyQueue);
}

void oxygen() {
    open_semaphores();

    int shm_fd = shm_open("/h2o_shm", O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("shm_open oxygen");
        exit(1);
    }
    shared_data = mmap(NULL, sizeof(shared_data_t), PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    close(shm_fd);

    sem_wait(mutex);
    shared_data->oxygens_waiting++;

    if (shared_data->hydrogens_waiting >= 2) {
        sem_post(hydroQueue);
        sem_post(hydroQueue);
        shared_data->hydrogens_waiting -= 2;
        shared_data->oxygens_waiting -= 1;
        shared_data->molecule_id_counter++;
        int molecule_id = shared_data->molecule_id_counter;
        sem_post(mutex);

        bond("Oxygen", getpid(), molecule_id);
    } else {
        sem_post(mutex);
        sem_wait(oxyQueue);

        sem_wait(mutex);
        int molecule_id = shared_data->molecule_id_counter;
        sem_post(mutex);

        bond("Oxygen", getpid(), molecule_id);
    }

    munmap(shared_data, sizeof(shared_data_t));
    sem_close(mutex);
    sem_close(hydroQueue);
    sem_close(oxyQueue);
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("Usage: %s start N M | H | O\n", argv[0]);
        exit(1);
    }

    if (strcmp(argv[1], "start") == 0) {
        if (argc != 4) {
            printf("Usage: %s start N M\n", argv[0]);
            exit(1);
        }

        int N = atoi(argv[2]);
        int M = atoi(argv[3]);

        int shm_fd = shm_open("/h2o_shm", O_CREAT | O_RDWR, 0666);
        if (shm_fd == -1) {
            perror("shm_open start");
            exit(1);
        }
        ftruncate(shm_fd, sizeof(shared_data_t));
        shared_data = mmap(NULL, sizeof(shared_data_t), PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
        close(shm_fd);

        shared_data->hydrogens_waiting = 0;
        shared_data->oxygens_waiting = 0;
        shared_data->molecule_id_counter = 0;

        init_semaphores();

        for (int i = 0; i < N; i++) {
            if (fork() == 0) {
                execl(argv[0], argv[0], "H", NULL);
                perror("execl H");
                exit(1);
            }
        }

        for (int i = 0; i < M; i++) {
            if (fork() == 0) {
                execl(argv[0], argv[0], "O", NULL);
                perror("execl O");
                exit(1);
            }
        }

        while (wait(NULL) > 0);

        destroy_semaphores();
        shm_unlink("/h2o_shm");

        munmap(shared_data, sizeof(shared_data_t));

    } else if (strcmp(argv[1], "H") == 0) {
        hydrogen();
    } else if (strcmp(argv[1], "O") == 0) {
        oxygen();
    } else {
        printf("Invalid argument. Usage: %s start N M | H | O\n", argv[0]);
        exit(1);
    }

    return 0;
}
