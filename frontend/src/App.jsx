import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import HomePage from './pages/HomePage';
import UsersPage from './pages/UsersPage';
import ArticlesPage from './pages/ArticlesPage';
import NYTimesPage from './pages/NYTimesPage';
import './App.css';

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <main>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/articles" element={<ArticlesPage />} />
          <Route path="/nytimes" element={<NYTimesPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
