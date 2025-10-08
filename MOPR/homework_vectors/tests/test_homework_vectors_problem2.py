import pytest
from src.homework_vectors_problem2 import find_common_meaning

@pytest.fixture
def model():
    return {
        "man": [1, 2],
        "king": [3, 4],
        "woman": [1, 2.1],
        "queen": [3.1, 4],
        "car": [7, 8]
    }

def test_find_common_meaning(model):
    assert find_common_meaning(model, "man", "king", "woman") == "queen"

def test_find_common_meaning_nonexistent_base_word(model):
    with pytest.raises(KeyError):
        find_common_meaning(model, "alien", "king", "woman")

def test_find_common_meaning_nonexistent_related_word(model):
    with pytest.raises(KeyError):
        find_common_meaning(model, "man", "spaceship", "woman")

def test_find_common_meaning_nonexistent_target_word(model):
    with pytest.raises(KeyError):
        find_common_meaning(model, "man", "king", "spaceship")

def test_find_common_meaning_identical_words(model):
    with pytest.raises(ValueError):
        find_common_meaning(model, "man", "man", "man")

