import math

def dot_product(vector1, vector2):
    return sum(x * y for x, y in zip(vector1, vector2))

def vector_norm(vector):
    return math.sqrt(sum(x ** 2 for x in vector))

def calculate_similarity(model, base_word, target_word):
    base_vector = model[base_word]
    target_vector = model[target_word]
    cosine_similarity = dot_product(base_vector, target_vector) / (vector_norm(base_vector) * vector_norm(target_vector))
    return cosine_similarity

def find_most_similar_to_given(model, given_word, target_words):
    similarities = [(word, calculate_similarity(model, given_word, word)) for word in target_words]
    most_similar_word = max(similarities, key=lambda x: x[1])[0]
    return most_similar_word

def mean(values):
    return sum(values) / len(values) if values else 0

def doesnt_match(model, given_words):
    if len(given_words) < 2:
        raise ValueError("Pass at least 2 words")
        
    similarities = {
        word: sum(calculate_similarity(model, word, other_word) for other_word in given_words if word != other_word) / (len(given_words) - 1)
        for word in given_words
    }
    least_similar_word = min(similarities, key=similarities.get)
    return least_similar_word

