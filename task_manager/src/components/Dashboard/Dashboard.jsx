import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance';

const Dashboard = () => {
  const [userProfile, setUserProfile] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axiosInstance.get('/api/users/profile');
        setUserProfile(response.data);
      } catch (error) {
        console.error('Error fetching user profile:', error);
        setError('Failed to load profile data.');
      }
    };

    const fetchTasks = async () => {
      try {
        const response = await axiosInstance.get('/api/task');
        setTasks(response.data);
      } catch (error) {
        console.error('Error fetching tasks:', error);
        setError('Failed to load tasks.');
      }
    };

    fetchUserProfile();
    fetchTasks();
  }, []);

  const handleAddTask = async (e) => {
    e.preventDefault();
    try {
      const response = await axiosInstance.post('/api/task', { title: newTask, description: '', completed: false });
      setTasks([...tasks, response.data]);
      setNewTask('');
    } catch (error) {
      console.error('Error adding task:', error);
      setError('Failed to add task.');
    }
  };

  if (error) {
    return <div className="text-red-500 text-center mt-4">{error}</div>;
  }

  return (
    <div className="container mx-auto mt-10 p-4">
      <h2 className="text-3xl font-bold mb-8 text-center">Dashboard</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Profile Section */}
        <div className="bg-white p-6 rounded-lg shadow-lg">
          <h3 className="text-xl font-semibold mb-4">Profile</h3>
          {userProfile ? (
            <div>
              <p className="mb-2"><span className="font-medium text-gray-700">Username:</span> {userProfile.username}</p>
              <p><span className="font-medium text-gray-700">Email:</span> {userProfile.email}</p>
            </div>
          ) : (
            <p className="text-gray-500">Loading profile...</p>
          )}
        </div>

        {/* Task List Section */}
        <div className="bg-white p-6 rounded-lg shadow-lg">
          <h3 className="text-xl font-semibold mb-4">Task List</h3>
          <ul className="list-disc pl-5 space-y-2">
            {tasks.map((task) => (
              <li key={task.id} className="text-gray-700">{task.title}</li>
            ))}
          </ul>
        </div>

        {/* Add Task Section */}
        <div className="bg-white p-6 rounded-lg shadow-lg">
          <h3 className="text-xl font-semibold mb-4">Add Task</h3>
          <form onSubmit={handleAddTask}>
            <div className="mb-4">
              <label htmlFor="taskName" className="block text-sm font-medium text-gray-700">
                Task Name
              </label>
              <input
                type="text"
                id="taskName"
                name="taskName"
                value={newTask}
                onChange={(e) => setNewTask(e.target.value)}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
              />
            </div>
            <button
              type="submit"
              className="inline-flex items-center px-4 py-2 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Add Task
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
