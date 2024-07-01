import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider, createRoutesFromElements, Route } from 'react-router-dom';
import App from './App';
import Login from './pages/Login';
import Dashboard from './components/Dashboard/Dashboard';
import Profile from './components/Profile';
import TaskList from './components/Task/TaskList';
import AddTask from './components/Task/AddTask';
import Register from './pages/Register';
import './index.css';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<App />}>
      <Route path="login" element={<Login />} />
      <Route path="signup" element={<Register/>} />
      <Route path="dashboard" element={<Dashboard />} />
      <Route path="profile" element={<Profile />} />
      <Route path="tasks" element={<TaskList />} />
      <Route path="add-task" element={<AddTask />} />
    </Route>
  )
);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
