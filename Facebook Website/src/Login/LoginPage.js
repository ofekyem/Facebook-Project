import React from 'react';
import "./LoginPage.css";
import Introduction from './Introduction/Introduction';
import Entrance from './Entrance/Entrance';
/*
this component will render the Entrance component where in the entrance component
and also render the Introduction component
this component get the setUserExists, userList and setUserLoggedIn as props
*/
function LoginPage({ setUserExists ,setToken , setUserLoggedIn}) {
  return (
    <div className="container px-4 text-center">
      <div className="col-4">
        <Entrance setUserExists={setUserExists} setToken={setToken} setUserLoggedIn={setUserLoggedIn}></Entrance>
      </div>

      <div className="col-6">
        <Introduction></Introduction>
      </div>
    </div>
  );
}

export default LoginPage;
