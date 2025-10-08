# Задача 2 - `head` (200 точки)

- Напишете програма на C, аналогична със системната команда head.

- Програмата трябва да приема поне един задължителен команден аргумент - имената на вече
  съществуващи текстови файлове.

- Програмата трябва да извежда първите 10 реда от всеки файл.

- Ако във файл има по-малко от 10 реда, цялото му съдържание се извежда.

- Например, ако са дадени два файла със следното съдържание `example-inputs/a.txt`:

  ```plaintext
  one
  two
  three
  four
  five
  six
  seven
  eight
  nine
  ten
  eleven
  ```

  и `example-inputs/b.txt`:

  ```plaintext
  1234 1234
  567
  890
  ```

  и c.txt не съществува, тогава програмата трябва да изведе:

  ```bash
  $ ./head example-inputs/a.txt example-inputs/b.txt example-inputs/c.txt
  ==> example-inputs/a.txt <==
  one
  two
  three
  four
  five
  six
  seven
  eight
  nine
  ten

  ==> example-inputs/b.txt <==
  1234 1234
  567
  890
  ./head: cannot open 'example-inputs/c.txt' for reading: No such file or directory
  ```

- При наличието само на един аргумент, заглавната секция (секцията от типа
  `==> <име> <==`) се пропуска.

- Ако някой от аргументите на `head` не е файл или файлът не може да се отвори,
  то програмата трябва да изведе съобщение на стандартния изход за грешка (stderr).

  Например, ако бъдат предадени аргументи, които не са файлове:

  ```bash
  ./head aa bb
  ```

  съобщението трябва да бъде оформено по следния начин:

  ```bash
  ./head: cannot open 'aa' for reading: No such file or directory
  ./head: cannot open 'bb' for reading: No such file or directory
  ```

- Ако `./head` не може да запише прочетеното на стандартния изход, то програмата
  трябва да изведе съобщение на стандартния изход за грешка (stderr). Например,
  ако дискът е пълен:

  ```bash
  ./head example-inputs/a.txt > /dev/full
  ```

  съобщението трябва да бъде оформено по следния начин:

  ```bash
  ./head: error writing 'standard output': No space left on device
  ```

- Ако `./head` не може да затвори успешно някой файл, то програмата трябва да изведе
  съобщение на стандартния изход за грешка (stderr). Например:

  ```bash
  ./head example-inputs/b.txt
  ```

  съобщението трябва да бъде оформено по следния начин:

  ```bash
  ./head: error reading 'example-inputs/b.txt': Input/output error
  ```

- При грешка в един файл, то изпълнението трябва да продължи към следващите (програмата
  не трябва да приключи).

- Програмата трябва да поддържа за опционален команден аргумент `-n <number>`,
  който да определя броят на редовете, които да се изведат от всеки файл.

  ```bash
  $ ./head -n 2 example-inputs/a.txt example-inputs/b.txt
  ==> example-inputs/a.txt <==
  one
  two

  ==> example-inputs/b.txt <==
  1234 1234
  567
  ```

- За реализацията на програмата трябва да използвате `lseek` системната функция,
  заедно с `open`, `close`, `read` и `write`.

- Не е позволено използването на библиотечни функции като `fopen`, `fclose`, `fread`,
  `fwrite` и т.н.
