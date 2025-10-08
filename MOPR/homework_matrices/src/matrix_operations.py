from typing import List, Union

class Matrix:
    def __init__(self, elements: List[List[Union[int, float]]]):
        self.elements = elements # tuka elements of gorniq red stavat za celiq class, ne samo za tozi metod
        self.element_type = self._determine_element_type() 
        self._convert_elements_to_type()

    def _determine_element_type(self) -> type:
        has_floats = any(isinstance(item, float) for row in self.elements for item in row) # proverqva dali ima floatove v inputa
        if has_floats:
            return float

        return int

    def _convert_elements_to_type(self) -> None:
        self.elements = [
            [self.element_type(item) for item in row]
            for row in self.elements
        ] # smenq vsichki elementi na edin tip  kakto pravi numpy

    def __repr__(self) -> str:
        return f"Matrix({self.elements}, element_type={self.element_type.__name__})"

    def display(self) -> None:
        print("[[", end="")
        for i, row in enumerate(self.elements):
            if i > 0:
                print(" [", end="")
            print(" ".join(map(str, row)), end="")
            if i < len(self.elements) - 1:
                print("]")
            else:
                print("]]")  #printi go da prilicha na numpy

    def array():
        return Matrix
    
    def add(self, other: 'Matrix') -> 'Matrix':
        if len(self.elements) != len(other.elements) or len(self.elements[0]) != len(other.elements[0]):
            raise ValueError("matricite trqbva da sa s ednakuv razmer")
        
        sumMatrix = [
            [self.elements[i][j] + other.elements[i][j] for j in range(len(self.elements[0]))]
            for i in range(len(self.elements))
        ]
        return Matrix(sumMatrix)

    def multiply(self, scalar: Union[int, float]) -> 'Matrix':
        scaMatrix = [
            [self.elements[i][j] * scalar for j in range(len(self.elements[0]))]
            for i in range(len(self.elements))
        ]
        return Matrix(scaMatrix)

    def dot_product(self, row: List[Union[int, float]], col: List[Union[int, float]]) -> Union[int, float]:
        return sum(row[i] * col[i] for i in range(len(row)))

    def matmul(self, other: 'Matrix') -> 'Matrix':
        rows_A, cols_A = len(self.elements), len(self.elements[0])
        rows_B, cols_B = len(other.elements), len(other.elements[0])

        if cols_A != rows_B:
            raise ValueError("matricite trqbva da sa Anxs i Bsxm")

        multMatrix = [[0] * cols_B for _ in range(rows_A)]
        for i in range(rows_A):
            for j in range(cols_B):
                row = self.elements[i]
                col = [other.elements[k][j] for k in range(rows_B)]
                multMatrix[i][j] = self.dot_product(row, col)

        return Matrix(multMatrix)
    
    def is_square(self) -> bool:
        return len(self.elements) == len(self.elements[0])
    
    def determinant(self) -> Union[int, float]:
        if not self.is_square(): 
            raise ValueError("matricata trqbva da e kvadratna")

        n = len(self.elements)
        if n == 1:
            return self.elements[0][0]
        if n == 2:
            return (
                float(self.elements[0][0] * self.elements[1][1]) -
                float(self.elements[0][1] * self.elements[1][0])
            )

        det = 0.0
        for j in range(n):
            minor = self._get_minor(0, j)
            det += ((-1) ** j) * self.elements[0][j] * minor.determinant()

        return det


    def _get_minor(self, row: int, col: int) -> 'Matrix':
        minor_elements = [
            [self.elements[i][j] for j in range(len(self.elements[0])) if j != col]
            for i in range(len(self.elements)) if i != row
        ]
        return Matrix(minor_elements)
    
    def transpose(self) -> 'Matrix':
        transposed_elements = [
            [self.elements[j][i] for j in range(len(self.elements))]
            for i in range(len(self.elements[0]))
        ]
        return Matrix(transposed_elements)
    
    def adjugate(self) -> 'Matrix':
        if not self.is_square():
            raise ValueError("matricata trqbva da e kvadratna")

        size = len(self.elements)
        cofactor_matrix = [[0.0] * size for _ in range(size)]

        for i in range(size):
            for j in range(size):
                minor = self._get_minor(i, j)
                cofactor_matrix[i][j] = ((-1) ** (i + j)) * minor.determinant()

        cofactor_matrix_instance = Matrix(cofactor_matrix)
        return cofactor_matrix_instance.transpose()

    
    def rank(self) -> int:
        def helper(matrix: 'Matrix') -> int:
            rows, cols = len(matrix.elements), len(matrix.elements[0])

            if rows == 0 or cols == 0:
                return 0

            if rows == cols:
                if matrix.determinant() != 0: # namira determinantata na nai golqmata kvadratna matrica
                    return rows # ako determinantata ne e nula znachi che matricata e lnz

            max_rank = 0
            for i in range(rows):
                for j in range(cols):
                    minor_elements = matrix._get_minor(i, j).elements
                    if minor_elements:
                        minor_matrix = Matrix(minor_elements)
                        max_rank = max(max_rank, helper(minor_matrix))

            return max_rank

        return helper(self)
    
def invert(matrix: 'Matrix') -> 'Matrix':
    if not matrix.is_square():
        raise ValueError("matricata trqbva da e kvadratna")
    det = matrix.determinant()
    if det == 0:
        raise ValueError("matricata ne moje da bude oburnata")
    return matrix.adjugate().multiply(1 / det) # vrushta obratna matrica

def solve(A: 'Matrix', B: 'Matrix') -> 'Matrix':
    A_inv = invert(A)
    return A_inv.matmul(B)  # pri Ax = B, (A^-1)*B = x  t.e  namirame x kato obrushtame A i umnojavame s B 