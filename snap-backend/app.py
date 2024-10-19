import os
import time
from flask import Flask, request
from transformers import pipeline
from PIL import Image

pipe2 = pipeline("image-classification", model="saved_pipeline")

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = './uploads'
app.config['MAX_CONTENT_LENGTH'] = 512 * 1024 * 1024

@app.route('/getFoodNameFromImage', methods=['POST'])
def upload():
    if not request.files.__contains__("file"):
        return "Invalid Request", 400

    file = request.files['file']
    time_millis = int(time.time() * 1000)
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], str(time_millis))
    file.save(file_path)

    try:
        image = Image.open(file_path)
        response = pipe2(image)[0]['label']
        print(response)
        return response
    except ValueError as e:
        print(e)
        return "File not supported", 403
    os.unlink(file_path)

@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
