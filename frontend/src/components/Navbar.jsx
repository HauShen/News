import { NavLink } from 'react-router-dom';
import './Navbar.css';

export default function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-brand">📰 News App</div>
      <div className="navbar-links">
        <NavLink to="/" end>Home</NavLink>
        <NavLink to="/users">Users</NavLink>
        <NavLink to="/articles">Articles</NavLink>
        <NavLink to="/nytimes">NY Times</NavLink>
      </div>
    </nav>
  );
}
