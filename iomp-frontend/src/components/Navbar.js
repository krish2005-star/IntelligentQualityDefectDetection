import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav style={styles.nav}>
      <h2 style={styles.logo}>📱 Defect Detection System</h2>
      <div style={styles.links}>
        <Link to="/" style={styles.link}>Customer</Link>
        <Link to="/admin" style={styles.link}>Admin</Link>
        <Link to="/inspection" style={styles.link}>Inspection</Link>
      </div>
    </nav>
  );
}

const styles = {
  nav: { display:'flex', justifyContent:'space-between', alignItems:'center',
    padding:'10px 30px', backgroundColor:'#1a1a2e', color:'white' },
  logo: { margin:0, color:'#e94560' },
  links: { display:'flex', gap:'20px' },
  link: { color:'white', textDecoration:'none', fontSize:'16px' }
};

export default Navbar;