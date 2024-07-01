import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance';

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const response = await axiosInstance.get('/api/task', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`, // Adjust as per your token storage method
          },
        });
        setTasks(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching tasks:', error);
        setError('Failed to fetch tasks.');
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const handleToggleCompletion = async (id, completed) => {
    try {
      const response = await axiosInstance.patch(`/api/tasks/${id}`, {
        completed: !completed, // Toggle the completion status
      }, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });

      // Update the tasks state to reflect the updated status
      const updatedTasks = tasks.map(task => {
        if (task.id === id) {
          return { ...task, completed: !completed };
        }
        return task;
      });
      setTasks(updatedTasks);
    } catch (error) {
      console.error('Error updating task status:', error);
      setError('Failed to update task status.');
    }
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  if (error) {
    return <div className="text-red-500 text-center mt-4">{error}</div>;
  }

  if (tasks.length === 0) {
    return <div className="text-center mt-4">No tasks found.</div>;
  }

  return (
    <div className="container mx-auto mt-10 p-4">
      <h2 className="text-3xl font-bold mb-8 text-center">Task List</h2>
      <ul className="space-y-4">
        {tasks.map((task) => (
          <li key={task.id} className="bg-white p-6 rounded-lg shadow-md">
            <h3 className="text-xl font-semibold mb-2">{task.title}</h3>
            <p className="text-gray-700 mb-2">{task.description}</p>
            <p className={`font-medium ${task.completed ? 'text-green-600' : 'text-red-600'}`}>
              Completed: {task.completed ? 'Yes' : 'No'}
            </p>
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-2"
              onClick={() => handleToggleCompletion(task.id, task.completed)}
            >
              Toggle Completion
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TaskList;
