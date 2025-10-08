#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define WORKER_COST 50
#define MARINE_COST 50
#define WORKER_MINING_TIME 3
#define WORKER_TRANSPORT_TIME 2
#define MARINE_TRAINING_TIME 1
#define WORKER_TRAINING_TIME 4
#define MINERALS_PER_BLOCK 500
#define MINERALS_PER_TRIP 8
#define INITIAL_WORKERS 5
#define INITIAL_BLOCKS 2
#define MAX_UNITS 200

int map_minerals;
int player_minerals = 0;
int worker_count = INITIAL_WORKERS;
int marine_count = 0;
int active_blocks;
int *minerals;
pthread_mutex_t minerals_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t command_center_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t training_mutex = PTHREAD_MUTEX_INITIALIZER;

void *worker_thread(void *arg) {
    int id = *(int *)arg; //castva void * kum int *
    free(arg);

    while (1) {
        int block_index = -1;
        pthread_mutex_lock(&minerals_mutex); // lockva mutexa za da go polzva samo to
        for (int i = 0; i < active_blocks; i++) { //checkva mineralite podred za da nameri neprazen
            if (minerals[i] > 0) {
                block_index = i;
                minerals[i] -= MINERALS_PER_TRIP;
                if (minerals[i] <= 0) active_blocks--;
                break;
            }
        }
        pthread_mutex_unlock(&minerals_mutex); // unlockva mutexa, mineralite moje da se polzvat ot drug thread

        if (block_index == -1) break; // svurshili sa mineralite

        printf("SCV %d is mining from mineral block %d\n", id, block_index + 1); // printi ako ima minerali i posle loopva pak
        sleep(WORKER_MINING_TIME);

        printf("SCV %d is transporting minerals\n", id);
        sleep(WORKER_TRANSPORT_TIME);

        pthread_mutex_lock(&command_center_mutex);
        player_minerals += MINERALS_PER_TRIP;
        printf("SCV %d delivered minerals to the Command Center\n", id);
        pthread_mutex_unlock(&command_center_mutex);
    }
    return NULL;
}

void train_marine() {
    pthread_mutex_lock(&training_mutex);
    if (player_minerals < MARINE_COST) {
        printf("Not enough minerals.\n");
    } else {
        player_minerals -= MARINE_COST;
        sleep(MARINE_TRAINING_TIME);
        marine_count++;
        printf("You wanna piece of me, boy?\n");
    }
    pthread_mutex_unlock(&training_mutex);
}

void train_worker() {
    pthread_mutex_lock(&training_mutex);
    if (player_minerals < WORKER_COST) {
        printf("Not enough minerals.\n");
    } else {
        player_minerals -= WORKER_COST;
        sleep(WORKER_TRAINING_TIME);
        worker_count++;
        printf("SCV good to go, sir.\n");
    }
    pthread_mutex_unlock(&training_mutex);
}

int main(int argc, char *argv[]) {
    //ako usera providene broi active blocks te se chetat ot command linea i se pravqt ot char na int s atoi
    // inache se polzva initial valueto koeto sme definirali 
    active_blocks = (argc > 1) ? atoi(argv[1]) : INITIAL_BLOCKS; 
    map_minerals = active_blocks * MINERALS_PER_BLOCK;

    minerals = malloc(active_blocks * sizeof(int));
    if (!minerals) {
        perror("malloc failed");
        exit(EXIT_FAILURE);
    } // allocateva array za mineralite

    for (int i = 0; i < active_blocks; i++) {
        minerals[i] = MINERALS_PER_BLOCK;
    } //pulni go

    pthread_t workers[INITIAL_WORKERS]; // pravi array za storevane na worker idtata
    for (int i = 0; i < INITIAL_WORKERS; i++) {
        int *id = malloc(sizeof(int)); // allocateva za vsqko id
        *id = i + 1;
        if (pthread_create(&workers[i], NULL, worker_thread, id)) { // createva thread za vseki worker s negovoto id
            perror("pthread_create failed");
            exit(EXIT_FAILURE);
        }
    }

    char command;
    while (scanf(" %c", &command) != EOF) {
        if (command == 'm') {
            train_marine();
        } else if (command == 's') {
            train_worker();
        }
    }

    for (int i = 0; i < INITIAL_WORKERS; i++) {
        pthread_join(workers[i], NULL); // join blockva executiona predi kraq na threada 
    } // tozi loop kazva na maina da izchaka dokato ne svurshat vsichki threadove ot workerite

    printf("Map minerals %d, player minerals %d, SCVs %d, Marines %d\n",
           map_minerals, player_minerals, worker_count, marine_count);

    free(minerals);
    return 0;
}

