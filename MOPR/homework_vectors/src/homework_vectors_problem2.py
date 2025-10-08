import math

def vector_subtract(vector1, vector2):
    return [x - y for x, y in zip(vector1, vector2)]

def vector_add(vector1, vector2):
    return [x + y for x, y in zip(vector1, vector2)]

def dot_product(vector1, vector2):
    return sum(x * y for x, y in zip(vector1, vector2))

def vector_norm(vector):
    return math.sqrt(sum(x ** 2 for x in vector))


def find_common_meaning(model, base_word, related_word, target_word):
    if base_word == related_word == target_word:
        raise ValueError("base_word, related_word, and target_word can not all be the same.")
    
    base_vector = model[base_word]
    related_vector = model[related_word]
    target_vector = model[target_word]
    relation_vector = [r - b + t for r, b, t in zip(related_vector, base_vector, target_vector)]

    most_similar_word = None
    max_similarity = -1
    for word, vector in model.items():
        if word not in {base_word, related_word, target_word}:
            similarity = sum(a * b for a, b in zip(vector, relation_vector)) / (
                (sum(a ** 2 for a in vector) ** 0.5) * (sum(b ** 2 for b in relation_vector) ** 0.5))
            if similarity > max_similarity:
                max_similarity = similarity
                most_similar_word = word
    return most_similar_word

