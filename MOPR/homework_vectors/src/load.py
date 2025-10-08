import json

def load_model(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        model = json.load(file)
    return model
