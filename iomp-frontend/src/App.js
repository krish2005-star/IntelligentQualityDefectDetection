import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import CustomerPage from './pages/CustomerPage';
import AdminPage from './pages/AdminPage';
import InspectionPage from './pages/InspectionPage';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<CustomerPage />} />
        <Route path="/admin" element={<AdminPage />} />
        <Route path="/inspection" element={<InspectionPage />} />
      </Routes>
    </Router>
  );
}

export default App;
