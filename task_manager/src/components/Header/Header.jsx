import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <header className="bg-gray-800 text-white p-4 shadow-lg">
      <div className="container mx-auto flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold hover:text-gray-400 transition duration-300">
          My Project
        </Link>
        <nav>
          <ul className="flex space-x-4">
            <li>
              <Link to="/login" className="hover:text-gray-400 transition duration-300">
                Login
              </Link>
            </li>
            <li>
              <Link to="/signup" className="hover:text-gray-400 transition duration-300">
                Register
              </Link>
            </li>
            <li>
              <Link to="/dashboard" className="hover:text-gray-400 transition duration-300">
                Dashboard
              </Link>
            </li>
            <li>
              <Link to="/profile" className="hover:text-gray-400 transition duration-300">
                Profile
              </Link>
            </li>
            <li>
              <Link to="/tasks" className="hover:text-gray-400 transition duration-300">
                Tasks
              </Link>
            </li>
            <li>
              <Link to="/add-task" className="hover:text-gray-400 transition duration-300">
                Add Task
              </Link>
            </li>
            {/* Add more links as needed */}
          </ul>
        </nav>
      </div>
    </header>
  );
};

export default Header;
