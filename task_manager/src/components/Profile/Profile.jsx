import React from 'react';

const Profile = () => {
  // Fetch user profile data from the backend or local storage
  const userProfile = {
    username: 'User',
    email: 'user@example.com'
  };

  return (
    <div>
      <h2>Profile</h2>
      <p>Username: {userProfile.username}</p>
      <p>Email: {userProfile.email}</p>
    </div>
  );
};

export default Profile;
