import json, numpy as np

def load_model(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        model = json.load(file)
    return model

# Example usage:
file_path = 'word_embeddings.json'
model = load_model(file_path)
print(model)



def calculate_similarity(model, base_word, target_word):
    base_vector = np.array(model[base_word])
    target_vector = np.array(model[target_word])
    cosine_similarity = np.dot(base_vector, target_vector) / (np.linalg.norm(base_vector) * np.linalg.norm(target_vector))
    return cosine_similarity


def find_most_similar_to_given(model, given_word, target_words):
    similarities = [(word, calculate_similarity(model, given_word, word)) for word in target_words]
    most_similar_word = max(similarities, key=lambda x: x[1])[0]
    return most_similar_word


def doesnt_match(model, given_words):
    similarities = {word: np.mean([calculate_similarity(model, word, other_word) for other_word in given_words if word != other_word]) for word in given_words}
    least_similar_word = min(similarities, key=similarities.get)
    return least_similar_word


def find_common_meaning(model, base_word, related_word, target_word):
    base_vector = np.array(model[base_word])
    related_vector = np.array(model[related_word])
    target_vector = np.array(model[target_word])
    relation_vector = related_vector - base_vector + target_vector

    most_similar_word = None
    max_similarity = -1
    for word, vector in model.items():
        if word not in {base_word, related_word, target_word}:
            similarity = np.dot(vector, relation_vector) / (np.linalg.norm(vector) * np.linalg.norm(relation_vector))
            if similarity > max_similarity:
                max_similarity = similarity
                most_similar_word = word
    return most_similar_word
