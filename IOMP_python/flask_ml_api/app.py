from flask import Flask, request, jsonify
from flask_cors import CORS
from ultralytics import YOLO
import cv2
import os
from werkzeug.utils import secure_filename

app = Flask(__name__)
CORS(app)

# Load YOLO model
model = YOLO('models/yolov8n.pt')

# Configure upload folder
UPLOAD_FOLDER = 'uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "healthy", "message": "ML API is running"})

@app.route('/detect', methods=['POST'])
def detect_defects():
    if 'image' not in request.files:
        return jsonify({"error": "No image provided"}), 400
    
    file = request.files['image']
    if file.filename == '':
        return jsonify({"error": "Empty filename"}), 400
    
    # Save uploaded image
    filename = secure_filename(file.filename)
    filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    file.save(filepath)
    
    # Run YOLO detection
    results = model.predict(filepath, conf=0.5)
    
    # Extract results
    detections = []
    for result in results:
        for box in result.boxes:
            detection = {
                "class": result.names[int(box.cls[0])],
                "confidence": float(box.conf[0]),
                "bounding_box": box.xyxy[0].tolist()
            }
            detections.append(detection)
    
    return jsonify({
        "success": True,
        "detections_count": len(detections),
        "detections": detections
    })

if __name__ == '__main__':
    print("🚀 Starting ML API on http://localhost:5001")
    app.run(host='0.0.0.0', port=5001, debug=True)