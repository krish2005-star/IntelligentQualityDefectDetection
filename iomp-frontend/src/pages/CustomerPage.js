import React, { useState } from 'react';
import { api } from '../api';

function CustomerPage() {
  const [description, setDescription] = useState('');
  const [image, setImage] = useState(null);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    if (!description || !image) {
      alert('Please fill all fields!');
      return;
    }
    setLoading(true);
    const formData = new FormData();
    formData.append('description', description);
    formData.append('customerId', 1);
    formData.append('image', image);
    try {
      const response = await api.submitComplaint(formData);
      setResult(response.data);
    } catch (err) {
      alert('Error submitting complaint!');
    }
    setLoading(false);
  };

  return (
    <div style={styles.container}>
      <h2>Submit a Complaint</h2>
      <textarea
        placeholder="Describe the defect..."
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        style={styles.textarea}
      />
      <input type="file" accept="image/*"
        onChange={(e) => setImage(e.target.files[0])}
        style={styles.input}
      />
      <button onClick={handleSubmit} style={styles.button} disabled={loading}>
        {loading ? 'Submitting...' : 'Submit Complaint'}
      </button>
      {result && (
        <div style={styles.result}>
          <h3>✅ Complaint Submitted!</h3>
          <p>ID: {result.id}</p>
          <p>Status: {result.status}</p>
          <p>Defect Detected: {result.defectDetected || 'None'}</p>
          <p>Confidence: {result.confidenceScore ?
            (result.confidenceScore * 100).toFixed(1) + '%' : 'N/A'}</p>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: { maxWidth:'600px', margin:'40px auto', padding:'20px' },
  textarea: { width:'100%', height:'100px', padding:'10px',
    marginBottom:'15px', fontSize:'14px' },
  input: { display:'block', marginBottom:'15px' },
  button: { padding:'10px 30px', backgroundColor:'#e94560',
    color:'white', border:'none', borderRadius:'5px',
    fontSize:'16px', cursor:'pointer' },
  result: { marginTop:'20px', padding:'15px',
    backgroundColor:'#f0fff0', borderRadius:'8px' }
};

export default CustomerPage;