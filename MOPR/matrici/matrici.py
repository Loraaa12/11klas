def array(input_list):
    #rows= len(input)
    #cols = len(input[0])
    #matrix = [[0]*cols]*rows

    matrixRow = []

    print("[")
    for i in input_list:
        matrixRow = input_list[i]
        print(matrixRow)
    
    print("]")

            


input_list = [[1,2,3], [4,5,6]]
matrix =array(input_list)
print(matrix)