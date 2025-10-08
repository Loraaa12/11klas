def array(input):
    matrix = []
    print("[", end="")

    for i, row in enumerate(input):
        matrix.append(row)
        formatted_row = "[" + " ".join(map(str, row)) + "]"
        if i == 0:
            print(formatted_row, end="")
        else:
            print("\n " + formatted_row, end="")

    print("]")
    return matrix

input = [[1, 2, 3], [4, 5, 6]]
matrix = array(input)
