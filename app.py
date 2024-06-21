import os
from flask import Flask, request, jsonify
import tensorflow as tf
import pandas as pd
import numpy as np

app = Flask(__name__)

# Load the TFLite model and allocate tensors
interpreter = tf.lite.Interpreter(model_path="models/model_with_metadata.tflite")
interpreter.allocate_tensors()

# Get input and output tensors
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Load the CSV file into a pandas DataFrame
df = pd.read_csv("data/flipkart_com-ecommerce_sample.csv")

def preprocess_input_ids(product_id):
    vector = np.zeros((1, 768), dtype=np.float32)
    hash_value = hash(product_id) % 768
    vector[0, hash_value] = 1.0  
    return vector

@app.route('/recommend', methods=['GET'])
def recommend():
    try:
        input_ids = request.args.get('input_ids')

        if not input_ids:
            return jsonify({"error": "input_ids is required"}), 400
        input_data = preprocess_input_ids(input_ids)
        interpreter.set_tensor(input_details[0]['index'], input_data)
        interpreter.invoke()
        output_data = interpreter.get_tensor(output_details[0]['index'])
        output_score = float(output_data[0][0])
        product_info = df[df['uniq_id'] == input_ids]

        if product_info.empty:
            return jsonify({"error": "Product not found"}), 404

        product_name = product_info.iloc[0]['product_name']
        product_url = product_info.iloc[0]['product_url']
        response = {
            "input_ids": input_ids,
            "product_name": product_name,
            "product_url": product_url,
            "output_score": output_score
        }

        return jsonify(response), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port)
