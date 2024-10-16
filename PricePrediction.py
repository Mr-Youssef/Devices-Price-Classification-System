from flask import Flask, request, jsonify
import pickle
from collections import OrderedDict

app = Flask(__name__)

# Load the model and scaler
model = pickle.load(open('Model/svm_model.pkl', 'rb'))
scaler = pickle.load(open('GeneratedFiles/scaler.pkl', 'rb'))
features = pickle.load(open('GeneratedFiles/selected_features.pkl', 'rb'))

@app.route('/predict', methods=['POST'])
def predict():
    device = request.get_json()

    # Exclude 'priceRange' and 'id'
    device_data = {k: v for k, v in device.items() if k not in {'priceRange', 'id'}}
    
    # Extract the values in the order of the keys
    device_values = [device_data[feature] for feature in device_data]

    # Scale the extracted device data
    scaled_values = scaler.transform([device_values])[0]

    # Reconstruct the dictionary with the scaled values
    scaled_device_data = {feature: scaled_values[i] for i, feature in enumerate(device_data.keys())}
    
    # Convert features to a list if it's not already
    features_list = list(features)

    # Retain only the features that are found in features_list
    filtered_scaled_device_data = {k: v for k, v in scaled_device_data.items() if k in features_list}

    # Sort filtered_scaled_device_data to be in the same order as features_list
    sorted_filtered_scaled_device_data = [filtered_scaled_device_data[k] for k in features_list]

    # return jsonify(sorted_filtered_scaled_device_data)
    price_range = model.predict([sorted_filtered_scaled_device_data])[0]
    return jsonify(int(price_range))

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000, debug=True)
