from flask import Flask, request, jsonify, render_template
from flask_sqlalchemy import SQLAlchemy
from jsonpath_ng import parse
import json

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://json_user:NEW_PASSWORD@localhost/json_checker'
app.config['SECRET_KEY'] = 'secret'
db = SQLAlchemy(app)

class JSONData(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.Text, nullable=False)

with app.app_context():
    db.create_all()

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        json_data = request.form.get("json_data")
        jsonpath_query = request.form.get("jsonpath_query")
        
        if not json_data or not jsonpath_query:
            return render_template("index.html", error="Missing JSON or JSONPath query")
        
        try:
            json_obj = json.loads(json_data)
            jsonpath_expr = parse(jsonpath_query)
            matches = [match.value for match in jsonpath_expr.find(json_obj)]
            return render_template("index.html", result=matches)
        except Exception as e:
            return render_template("index.html", error=str(e))
    
    return render_template("index.html")

if __name__ == '__main__':
    app.run(debug=True)
