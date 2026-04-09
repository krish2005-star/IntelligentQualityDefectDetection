import React, { useState, useEffect } from 'react';
import { api } from '../api';

function InspectionPage() {
  const [productId, setProductId] = useState('');
  const [image, setImage] = useState(null);
  const [result, setResult] = useState(null);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const response = await api.getInspectionStats();
      setStats(response.data);
    } catch (err) {
      console.log('Error loading stats');
    }
  };

  const handleInspect = async () => {
    if (!productId || !image) {
      alert('Please fill all fields!');
      return;
    }
    setLoading(true);
    const formData = new FormData();
    formData.append('productId', productId);
    formData.append('image', image);
    try {
      const response = await api.inspectProduct(formData);
      setResult(response.data);
      loadStats();
    } catch (err) {
      alert('Error running inspection!');
    }
    setLoading(false);
  };

  return (
    <div style={styles.container}>
      <h2>Product Inspection</h2>
      {stats && (
        <div style={styles.stats}>
          <div style={styles.statBox}>
            <h3>{stats.total}</h3><p>Total</p>
          </div>
          <div style={{...styles.statBox, backgroundColor:'#28a745'}}>
            <h3>{stats.passed}</h3><p>Passed</p>
          </div>
          <div style={{...styles.statBox, backgroundColor:'#e94560'}}>
            <h3>{stats.failed}</h3><p>Failed</p>
          </div>
        </div>
      )}
      <input placeholder="Product ID" value={productId}
        onChange={(e) => setProductId(e.target.value)}
        style={styles.input}
      />
      <input type="file" accept="image/*"
        onChange={(e) => setImage(e.target.files[0])}
        style={styles.input}
      />
      <button onClick={handleInspect} style={styles.button} disabled={loading}>
        {loading ? 'Inspecting...' : '🔍 Run Inspection'}
      </button>
      {result && (
        <div style={{...styles.result,
          backgroundColor: result.status === 'PASSED' ? '#f0fff0' : '#fff0f0'}}>
          <h3>{result.status === 'PASSED' ? '✅ PASSED' : '❌ FAILED'}</h3>
          <p>Inspection ID: {result.id}</p>
          <p>Confidence: {result.confidenceScore ?
            (result.confidenceScore * 100).toFixed(1) + '%' : 'N/A'}</p>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: { maxWidth:'600px', margin:'40px auto', padding:'20px' },
  stats: { display:'flex', gap:'15px', marginBottom:'25px' },
  statBox: { flex:1, padding:'15px', backgroundColor:'#1a1a2e',
    color:'white', borderRadius:'8px', textAlign:'center' },
  input: { display:'block', width:'100%', padding:'10px',
    marginBottom:'15px', fontSize:'14px', borderRadius:'5px',
    border:'1px solid #ddd' },
  button: { padding:'10px 30px', backgroundColor:'#e94560',
    color:'white', border:'none', borderRadius:'5px',
    fontSize:'16px', cursor:'pointer' },
  result: { marginTop:'20px', padding:'15px', borderRadius:'8px' }
};

export default InspectionPage;