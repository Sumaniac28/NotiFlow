from flask import Flask, request, jsonify
from flask_cors import CORS # type: ignore
from transformers import pipeline

app = Flask(__name__)
CORS(app, supports_credentials=True)

#Example Words
toxicWord = "You're such a lazy bitch, always messing things up. How the hell do you expect to get anywhere in life when you don't give a fuck about anything? You're nothing but a useless hoe, and it's pathetic watching you try to act like you matter."
mediumWord = "You're so clueless sometimes, it's like you don't even try. Honestly, it's frustrating dealing with someone who can never get anything right. You're always messing things up and dragging everyone else down with your careless attitude."
nonToxicWord = "You've been doing a great job lately, and it's clear that you're putting in a lot of effort. Everyone has moments where things don't go perfectly, but the important thing is that you're learning and growing. Keep up the good work, and don't be afraid to ask for help when needed."

stored_word = ""

pipe = pipeline("text-classification", model='unitary/toxic-bert')

@app.route('/')
def hello():
    return "Welcome to Flask Server"

@app.route("/submit", methods=['POST'])
def submit_word():
    global stored_word
    data = request.get_json()  # Input = {"word": "This is a sample sentence."}
    
    if "word" in data:
        stored_word = data['word']
        return jsonify({"message": "Word stored successfully"}), 200
    else:
        return jsonify({"error": "No word provided"}), 400

@app.route('/detect', methods=['GET'])
def detect():
    global stored_word

    threshold = 0.5

    if stored_word is None:
        resp = jsonify({"message": 'No word stored yet',"proceed":False}),400
    
    result = pipe(stored_word)[0]
    score = result['score']

    if score <= threshold:
        resp = jsonify({
                    "message": 'Word processed',
                    "label" : result['label'],
                    "score" : result['score'],
                    "proceed": True,
        }),200
        return resp
    
    else:
        resp = jsonify({
                    "message": 'Word processed',
                    "label" : result['label'],
                    "score" : result['score'],
                    "proceed": False,
        }),200
        return resp


if __name__ == "__main__":
    app.run(debug=True)