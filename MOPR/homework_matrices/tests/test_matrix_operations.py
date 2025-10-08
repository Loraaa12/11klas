from typing import List, Union
import sys
import os

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
import src.matrix_operations as mat

TEST_NUMPY = False
if TEST_NUMPY:
    import numpy as np

def test_initialization_and_type_conversion():
    m1 = mat.Matrix([[1, 2], [3, 4]])
    if TEST_NUMPY:
        np_m1 = np.array([[1, 2], [3, 4]])
        assert m1.elements == np_m1.tolist()
    
    m2 = mat.Matrix([[1.0, 2], [3, 4.5]])
    if TEST_NUMPY:
        np_m2 = np.array([[1.0, 2.0], [3.0, 4.5]])
        assert m2.elements == np_m2.tolist()

def test_add():
    m1 = mat.Matrix([[1, 2], [3, 4]])
    m2 = mat.Matrix([[5, 6], [7, 8]])
    result = m1.add(m2)
    if TEST_NUMPY:
        np_m1 = np.array(m1.elements)
        np_m2 = np.array(m2.elements)
        np_result = np_m1 + np_m2
        assert result.elements == np_result.tolist()

def test_multiply():
    m = mat.Matrix([[1, 2], [3, 4]])
    scalar = 2
    result = m.multiply(scalar)
    if TEST_NUMPY:
        np_m = np.array(m.elements)
        np_result = np_m * scalar
        assert result.elements == np_result.tolist()

def test_matmul():
    m1 = mat.Matrix([[1, 2], [3, 4]])
    m2 = mat.Matrix([[5, 6], [7, 8]])
    result = m1.matmul(m2)
    if TEST_NUMPY:
        np_m1 = np.array(m1.elements)
        np_m2 = np.array(m2.elements)
        np_result = np.matmul(np_m1, np_m2)
        assert result.elements == np_result.tolist()

def test_determinant():
    m = mat.Matrix([[1, 2], [3, 4]])
    result = m.determinant()
    if TEST_NUMPY:
        np_m = np.array(m.elements)
        np_result = np.linalg.det(np_m)
        assert np.allclose(result, np_result)

    #def test_adjugate():
    #    m = mat.Matrix([[1, 2], [3, 4]])
    #    result = m.adjugate()
    #    np_m = np.array(m.elements)
    #    np_adjugate = np.linalg.inv(np_m).T * np.linalg.det(np_m)
    #    assert np.allclose(result.elements, np_adjugate.tolist()), f"Matrix.adjugate: {result.elements}\nExpected adjugate: {np_adjugate.tolist()}"

def test_transpose():
    m = mat.Matrix([[1, 2], [3, 4]])
    result = m.transpose()
    if TEST_NUMPY:
        np_m = np.array(m.elements)
        np_result = np_m.T
        assert result.elements == np_result.tolist()

def test_invert():
    m = mat.Matrix([[1, 2], [3, 4]])
    result = mat.invert(m)
    if TEST_NUMPY:
        np_m = np.array(m.elements)
        np_result = np.linalg.inv(np_m)
        assert np.allclose(result.elements, np_result.tolist())

def test_solve():
    A = mat.Matrix([[2, 1], [1, 3]])
    B = mat.Matrix([[1], [2]])
    result = mat.solve(A, B)
    if TEST_NUMPY:
        np_A = np.array(A.elements)
        np_B = np.array(B.elements)
        np_result = np.linalg.solve(np_A, np_B)
        assert np.allclose(result.elements, np_result.tolist())

def test_is_square():
    m1 = mat.Matrix([[1, 2], [3, 4]])
    assert m1.is_square()

    m2 = mat.Matrix([[1, 2, 3], [4, 5, 6]])
    assert not m2.is_square()

def test_errors():
    m = mat.Matrix([[1, 2], [3, 4]])
    non_square = mat.Matrix([[1, 2, 3], [4, 5, 6]])

    try:
        non_square.determinant()
    except ValueError as e:
        print("determinant error:", e)

    try:
        mat.invert(non_square)
    except ValueError as e:
        print("invert error:", e)

    try:
        m.matmul(non_square)
    except ValueError as e:
        print("matmul error:", e)

    try:
        m.add(non_square)
    except ValueError as e:
        print("add error:", e)

def test_rank():
    m = mat.Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
    result = m.rank()
    if TEST_NUMPY:
        np_m = np.array(m.elements)
        np_result = np.linalg.matrix_rank(np_m)
        assert result == np_result
