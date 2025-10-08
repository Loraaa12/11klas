#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>
#include <sys/stat.h>
#include <unistd.h>
#include <errno.h>
#include <pwd.h>
#include <grp.h>
#include <time.h>

// checkva vida file i permissionite mu
void print_permissions(mode_t mode) {
    char types[] = "-bcdpls";
    char file_type = (S_ISREG(mode)) ? '-' :
                     (S_ISBLK(mode)) ? 'b' :
                     (S_ISCHR(mode)) ? 'c' :
                     (S_ISDIR(mode)) ? 'd' :
                     (S_ISFIFO(mode)) ? 'p' :
                     (S_ISLNK(mode)) ? 'l' : 's';

    printf("%c", file_type);
    printf("%c%c%c", (mode & S_IRUSR) ? 'r' : '-',
                    (mode & S_IWUSR) ? 'w' : '-',
                    (mode & S_IXUSR) ? 'x' : '-');
    printf("%c%c%c", (mode & S_IRGRP) ? 'r' : '-',
                    (mode & S_IWGRP) ? 'w' : '-',
                    (mode & S_IXGRP) ? 'x' : '-');
    printf("%c%c%c", (mode & S_IROTH) ? 'r' : '-',
                    (mode & S_IWOTH) ? 'w' : '-',
                    (mode & S_IXOTH) ? 'x' : '-');
}

void print_file_info(const char *path, const char *name, int long_format) {
    char full_path[4096];
    snprintf(full_path, sizeof(full_path), "%s/%s", path, name);

    struct stat st;
    if (stat(full_path, &st) == -1) {
        fprintf(stderr, "ls: cannot access '%s': %s\n", name, strerror(errno));
        return;
    }


    // ako e long format, trwbva da se izprinti permissionite, owner i group name i tn
    if (long_format) {
        print_permissions(st.st_mode);
        printf(" %ld", st.st_nlink);

        struct passwd *pw = getpwuid(st.st_uid);
        struct group *gr = getgrgid(st.st_gid);

        printf(" %s %s", pw ? pw->pw_name : "unknown", gr ? gr->gr_name : "unknown");
        printf(" %ld", st.st_size);

        char time_str[100];
        struct tm *timeinfo = localtime(&st.st_mtime);
        strftime(time_str, sizeof(time_str), "%b %d %H:%M", timeinfo);

        printf(" %s %s\n", time_str, name);
    } else {
        printf("%s\n", name);
    }
}

void list_directory(const char *path, int show_hidden, int long_format, int recursive) {
    DIR *dir = opendir(path);
    if (!dir) {
        fprintf(stderr, "ls: cannot open directory '%s': %s\n", path, strerror(errno));
        return;
    }

    struct dirent *entry;
    printf("%s:\n", path);
    while ((entry = readdir(dir)) != NULL) {
        if (!show_hidden && entry->d_name[0] == '.') {
            continue;
        }
        if (!strcmp(entry->d_name, ".") || !strcmp(entry->d_name, "..")) {
            continue;
        }
        print_file_info(path, entry->d_name, long_format);
    }
    closedir(dir);

    if (recursive) {
        dir = opendir(path);
        while ((entry = readdir(dir)) != NULL) {
            if (entry->d_type == DT_DIR && 
                strcmp(entry->d_name, ".") != 0 && 
                strcmp(entry->d_name, "..") != 0) {

                char subdir[4096];
                snprintf(subdir, sizeof(subdir), "%s/%s", path, entry->d_name);
                list_directory(subdir, show_hidden, long_format, recursive);
            }
        }
        closedir(dir);
    }
}

int main(int argc, char *argv[]) {
    int show_hidden = 0, long_format = 0, recursive = 0;
    int arg_start = 1;

    // razlichnite opcii
    while (arg_start < argc && argv[arg_start][0] == '-') {
        for (int i = 1; argv[arg_start][i] != '\0'; i++) {
            switch (argv[arg_start][i]) {
                case 'A': show_hidden = 1; break;
                case 'l': long_format = 1; break;
                case 'R': recursive = 1; break;
                default:
                    fprintf(stderr, "ls: invalid option -- '%c'\n", argv[arg_start][i]);
                    return EXIT_FAILURE;
            }
        }
        arg_start++;
    }

    // printi current dir ako ne e kazano specifichno neshto
    if (arg_start == argc) {
        list_directory(".", show_hidden, long_format, recursive);
    } else {
        for (int i = arg_start; i < argc; i++) {
            struct stat st;
            if (stat(argv[i], &st) == -1) {
                fprintf(stderr, "ls: cannot access '%s': %s\n", argv[i], strerror(errno));
                continue;
            }

            if (S_ISDIR(st.st_mode)) {
                list_directory(argv[i], show_hidden, long_format, recursive);
            } else {
                print_file_info(".", argv[i], long_format);
            }
        }
    }

    return EXIT_SUCCESS;
}
