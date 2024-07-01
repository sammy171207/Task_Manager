import React, { useEffect, useState } from 'react';
import axiosInstance from '../api/axiosInstance';

const Profile = () => {
  const [userProfile, setUserProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axiosInstance.get('/api/users/profile', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        setUserProfile(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching user profile:', error);
        setError('Failed to load profile data.');
        setLoading(false);
      }
    };

    fetchUserProfile();
  }, []);

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  if (error) {
    return <div className="text-red-500 text-center mt-4">{error}</div>;
  }

  if (!userProfile) {
    return <div className="text-center mt-4">No profile data found.</div>;
  }

  return (
    <div className="container mx-auto mt-10 p-4">
      <div className="bg-white p-6 rounded-lg shadow-lg max-w-md mx-auto">
        <h2 className="text-2xl font-bold mb-4 text-center">Profile</h2>
        <p className="mb-2"><span className="font-medium text-gray-700">Username:</span> {userProfile.username}</p>
        <p><span className="font-medium text-gray-700">Email:</span> {userProfile.email}</p>
      </div>
    </div>
  );
};

export default Profile;
