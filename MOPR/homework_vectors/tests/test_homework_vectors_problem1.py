import pytest
from src.homework_vectors_problem1 import calculate_similarity, find_most_similar_to_given, doesnt_match

@pytest.fixture
def model():
    return {
        "man": [1, 2],
        "king": [3, 4],
        "woman": [1, 2.1],
        "queen": [3.1, 4],
        "car": [7, 8],
        "bridge": [4, 5]
    }

def floats_are_close(a, b, tolerance=1e-5):
    return abs(a - b) < tolerance

def test_calculate_similarity(model):
    assert floats_are_close(calculate_similarity(model, "man", "woman"), 0.999, tolerance=0.1)

def test_calculate_similarity_nonexistent_words():
    with pytest.raises(KeyError):
        calculate_similarity({"man": [1, 2]}, "alien", "king")

def test_calculate_similarity_identical_words(model):
    assert floats_are_close(calculate_similarity(model, "king", "king"), 1.0)

def test_find_most_similar_to_given(model):
    assert find_most_similar_to_given(model, "bridge", ["car", "man", "queen"]) == "queen"

def test_find_most_similar_to_given_nonexistent_given_word(model):
    with pytest.raises(KeyError):
        find_most_similar_to_given(model, "alien", ["car", "man", "queen"])

def test_find_most_similar_to_given_empty_target_words(model):
    with pytest.raises(ValueError):
        find_most_similar_to_given(model, "bridge", [])

def test_doesnt_match(model):
    assert doesnt_match(model, ["man", "king", "woman", "car"]) == "car"

def test_doesnt_match_single_word():
    with pytest.raises(ValueError):
        doesnt_match({"man": [1, 2]}, ["man"])

def test_doesnt_match_nonexistent_word(model):
    with pytest.raises(KeyError):
        doesnt_match(model, ["man", "king", "nonexistentword"])
