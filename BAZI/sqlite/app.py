from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from flask_wtf import FlaskForm
from wtforms import TextAreaField, StringField, SubmitField
from wtforms.validators import DataRequired
import json
from jsonpath_ng import parse

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///data.db'
app.config['SECRET_KEY'] = 'secret'
db = SQLAlchemy(app)

class JSONData(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.Text, nullable=False)

class JSONForm(FlaskForm):
    json_input = TextAreaField('JSON данни', validators=[DataRequired()])
    submit_json = SubmitField('Запази JSON')

class QueryForm(FlaskForm):
    jsonpath_query = StringField('JSONPath заявка', validators=[DataRequired()])
    submit_query = SubmitField('Изпълни заявката')

# Създаване на таблиците при стартиране
with app.app_context():
    db.create_all()

@app.route('/', methods=['GET', 'POST'])
def index():
    json_form = JSONForm()
    query_form = QueryForm()
    results = None

    if json_form.validate_on_submit():
        try:
            json_obj = json.loads(json_form.json_input.data)
            db.session.add(JSONData(content=json.dumps(json_obj, indent=4)))
            db.session.commit()
            return redirect(url_for('index'))
        except json.JSONDecodeError:
            json_form.json_input.errors.append('Невалиден JSON!')

    if query_form.validate_on_submit():
        jsonpath_expr = parse(query_form.jsonpath_query.data)
        stored_jsons = JSONData.query.all()
        results = []
        
        for entry in stored_jsons:
            json_obj = json.loads(entry.content)
            matches = [match.value for match in jsonpath_expr.find(json_obj)]
            results.append({'id': entry.id, 'matches': matches})

    return render_template('index.html', json_form=json_form, query_form=query_form, results=results)

if __name__ == '__main__':
    app.run(debug=True)
