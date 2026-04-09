import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

export const api = {
  // Products
  getAllProducts: () => axios.get(`${API_BASE}/products/all`),
  addProduct: (product) => axios.post(`${API_BASE}/products/add`, product),

  // Complaints
  submitComplaint: (formData) => axios.post(`${API_BASE}/complaints/submit`, formData),
  getAllComplaints: () => axios.get(`${API_BASE}/complaints/all`),
  updateComplaintStatus: (id, status) => axios.put(`${API_BASE}/complaints/${id}/status?status=${status}`),

  // Inspections
  inspectProduct: (formData) => axios.post(`${API_BASE}/inspections/inspect`, formData),
  getAllInspections: () => axios.get(`${API_BASE}/inspections/all`),
  getInspectionStats: () => axios.get(`${API_BASE}/inspections/stats`),
};