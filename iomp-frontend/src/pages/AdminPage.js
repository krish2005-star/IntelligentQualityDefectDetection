import React, { useState, useEffect } from 'react';
import { api } from '../api';

function AdminPage() {
  const [complaints, setComplaints] = useState([]);
  const [loading, setLoading] = useState(true);
  const [view, setView] = useState('OPEN'); // OPEN or RESOLVED

  useEffect(() => {
    loadComplaints();
  }, []);

  const loadComplaints = async () => {
    try {
      const response = await api.getAllComplaints();
      // normalize and sort: newest first by submittedAt (if available), fallback to id desc
      const data = Array.isArray(response.data) ? response.data.slice() : [];
      data.sort((a, b) => {
        const ta = a.submittedAt ? new Date(a.submittedAt).getTime() : 0;
        const tb = b.submittedAt ? new Date(b.submittedAt).getTime() : 0;
        if (ta !== tb) return tb - ta;
        return (b.id || 0) - (a.id || 0);
      });
      setComplaints(data);
    } catch (err) {
      alert('Error loading complaints!');
    }
    setLoading(false);
  };

  const updateStatus = async (id, status) => {
    try {
      await api.updateComplaintStatus(id, status);
      // reload fresh list after update to keep server as source of truth
      await loadComplaints();
    } catch (err) {
      alert('Error updating status!');
    }
  };

  if (loading) return <h3 style={{textAlign:'center'}}>Loading...</h3>;

  // derive filtered lists
  const openComplaints = complaints.filter(c => !c.status || c.status !== 'RESOLVED');
  const resolvedComplaints = complaints.filter(c => c.status === 'RESOLVED');

  return (
    <div style={styles.container}>
      <h2>Admin - Complaints</h2>

      <div style={{display:'flex', gap:10, marginBottom:20, alignItems:'center'}}>
        <button
          onClick={() => setView('OPEN')}
          style={{...styles.toggleBtn, backgroundColor: view === 'OPEN' ? '#007bff' : '#e0e0e0', color: view === 'OPEN' ? 'white' : 'black'}}
        >
          Open Complaints ({openComplaints.length})
        </button>
        <button
          onClick={() => setView('RESOLVED')}
          style={{...styles.toggleBtn, backgroundColor: view === 'RESOLVED' ? '#28a745' : '#e0e0e0', color: view === 'RESOLVED' ? 'white' : 'black'}}
        >
          Resolved Complaints ({resolvedComplaints.length})
        </button>
      </div>

      {complaints.length === 0 && <p>No complaints found.</p>}

      {(view === 'OPEN' ? openComplaints : resolvedComplaints).map(c => (
        <div key={c.id} style={styles.card}>
          <h4>Complaint #{c.id}</h4>
          <p><b>Description:</b> {c.description}</p>
          <p><b>Status:</b> {c.status}</p>
          <p><b>Defect:</b> {c.defectDetected || 'None'}</p>
          <p><b>Confidence:</b> {c.confidenceScore ?
            (c.confidenceScore * 100).toFixed(1) + '%' : 'N/A'}</p>
          <p><b>Submitted:</b> {c.submittedAt ? new Date(c.submittedAt).toLocaleString() : 'N/A'}</p>
          <div style={styles.buttons}>
            <button onClick={() => updateStatus(c.id, 'UNDER_REVIEW')}
              style={{...styles.btn, backgroundColor:'#f0a500'}}>
              Under Review
            </button>
            {c.status !== 'RESOLVED' && (
              <button onClick={() => updateStatus(c.id, 'RESOLVED')}
                style={{...styles.btn, backgroundColor:'#28a745'}}>
                Resolve
              </button>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}

const styles = {
  container: { maxWidth:'800px', margin:'40px auto', padding:'20px' },
  card: { backgroundColor:'#f9f9f9', padding:'20px',
    marginBottom:'15px', borderRadius:'8px',
    boxShadow:'0 2px 4px rgba(0,0,0,0.1)' },
  buttons: { display:'flex', gap:'10px', marginTop:'10px' },
  btn: { padding:'10px 22px', color:'white', border:'none',
    borderRadius:'5px', cursor:'pointer', fontSize:'16px' },
  toggleBtn: { padding: '8px 14px', border: '1px solid #ccc', borderRadius: '4px', cursor: 'pointer', fontSize: '15px' }
};

export default AdminPage;