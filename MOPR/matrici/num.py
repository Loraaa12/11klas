import numpy as np

A = np.array([[1,2,3],[4,5,6],[7,8,9]])
print(A)

def adjugate(matrix):
    """
    Calculate the adjugate (adjoint) of a square matrix.
    """
    if matrix.shape[0] != matrix.shape[1]:
        raise ValueError("Matrix must be square.")
    
    n = matrix.shape[0]
    adj = np.zeros_like(matrix, dtype=float)
    
    for i in range(n):
        for j in range(n):
            # Minor matrix excluding row i and column j
            minor = np.delete(np.delete(matrix, i, axis=0), j, axis=1)
            # Cofactor is (-1)^(i+j) * determinant of the minor
            adj[j, i] = ((-1) ** (i + j)) * np.linalg.det(minor)  # Transpose index (j, i)
    
    return adj

# Example usage
matrix = np.array([
    [1, 2, 3],
    [4, 5, 6],
    [7, 8, 9]
])

adj_A = adjugate(matrix)
print("Adjugate of the matrix:\n", adj_A)
