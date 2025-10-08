#include "ui.h"
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int gold = 100;
int soldiers = 50;
int health = 100;
int zombies = 13;
int game_over = 0;

pthread_mutex_t lock;

void *miner_thread(void *arg) {
    while (!game_over) {
        sleep(1); // infinite loop prez 1 sec dokato ne svurshi igrata
        pthread_mutex_lock(&lock); // lockva accessa kum golda
        gold += 10;
        print_gold(gold); //updateva UIa
        pthread_mutex_unlock(&lock); // unlockva golda
    }
    return NULL;
}

void *zombie_thread(void *arg) {
    while (!game_over) {
        for (int i = 5; i >= 0; i--) {
            print_zombies(i, zombies);
            sleep(1);
        }

        pthread_mutex_lock(&lock);
        if (zombies > soldiers) {
            int damage = zombies - soldiers;
            health -= damage;
            print_fail("Zombie attack succeeded ;(!");
            print_health(health);
            if (health <= 0) {
                game_over = 1;
                game_end(zombies);
            }
        } else {
            print_succ("Zombie attack deflected! :)");
        }
        zombies *= 2;
        pthread_mutex_unlock(&lock);
    }
    return NULL;
}

void handle_input(char command) {
    pthread_mutex_lock(&lock);
    switch (command) {
        case 'm':
            if (gold >= 100) {
                gold -= 100;
                pthread_t miner; // suzdava ID za threada
                pthread_create(&miner, NULL, miner_thread, NULL); // createva threada i go assignva na IDto
                print_msg("Miner created!");
                print_gold(gold);
            } else {
                print_fail("Not enough gold!");
            }
            break;
        case 's':
            if (gold >= 10) {
                gold -= 10;
                soldiers++;
                print_msg("Soldier created!");
                print_gold(gold);
                print_soldiers(soldiers);
            } else {
                print_fail("Not enough gold!");
            }
            break;
        case 'x':
            if (gold >= 100) {
                gold -= 100;
                soldiers += 10;
                print_msg("10 x soldiers created!");
                print_gold(gold);
                print_soldiers(soldiers);
            } else {
                print_fail("Not enough gold!");
            }
            break;
        default:
            break;
    }
    pthread_mutex_unlock(&lock);
}

int main() {
    pthread_mutex_init(&lock, NULL);
    init();
    print_gold(gold);
    print_soldiers(soldiers);
    print_zombies(5, zombies);
    print_health(health);

    pthread_t zombie_thread_id;
    pthread_create(&zombie_thread_id, NULL, zombie_thread, NULL);

    while (1) {
        int ch = get_input();
        switch (ch) {
            case 'q':
                game_end(0);
                break;
            default:
                handle_input(ch);
                break;
        }
    }

    pthread_mutex_destroy(&lock);
    return 0;
}

