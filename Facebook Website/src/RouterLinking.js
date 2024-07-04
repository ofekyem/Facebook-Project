import React, { useState, useEffect } from "react";
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import LoginPage from "./Login/LoginPage";
import Pid from "./Pid/Pid";
import Creating from "./Creating/Creating"; 
// import Posts from "./data/db.json"
import { getAllPosts } from "./ServerCalls/postsCalls";

export default function Main() {
  /*
  get all the posts from the db.json file
  and set userlist to an empty arr, and userExists to false
  and also set userLoggedIn to false
  */
  //const [postList, setPostList] = useState([]);
  const [userExists, setUserExists] = useState(false);
  const [idComment,setIdComment]=useState(16);
  const [token,setToken]=useState(false);
  const [userLoggedIn, setUserLoggedIn] = useState(false);

  // useEffect(() => {
  //   if (token) {
  //     getAllPosts(token).then((result) => setPostList(result.data));
  //   }
  // }, [token]);
  
  return (
    /*
    use the BrowserRouter to wrap the Routes
    */
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <LoginPage
              userExists={userExists}
              setToken={setToken}
              setUserExists={setUserExists}
              setUserLoggedIn={setUserLoggedIn}
            ></LoginPage>
          }
        ></Route>
        <Route
          path="/pid"
          element={userExists ? <Pid
            setUserLoggedIn={setUserLoggedIn}
            userLoggedIn={userLoggedIn}
            idComment={idComment}
            setIdComment={setIdComment}
            token={token}
            setToken={setToken}
          ></Pid> : <Navigate to="/"></Navigate>} // if userExists is false, then navigate to the login page
        ></Route>

        <Route
          path="/creating"
          element=
            {<Creating
                >
            </Creating>}
        ></Route>
      </Routes>
    </BrowserRouter>
  );
}
