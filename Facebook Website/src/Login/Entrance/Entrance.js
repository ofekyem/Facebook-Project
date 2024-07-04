import React, { useState } from "react";
import { useRef } from "react";
import SignInButton from "../SignInButton/SignInButton";
import SignUpButton from "../SignUpButton/SignUpButton";
import "./Entrance.css";
import { useNavigate } from "react-router-dom";
import {loginServer , getUserData} from "../../ServerCalls/login";

// this component will render the entrance page
function Entrance({ setUserExists , setToken , setUserLoggedIn}) {
    // this state is used to show a message when the user is on the password input field
    const [passwordMessage, setPasswordMessage] = useState("");
    // all the useRef are used to get the value of the input fields 
    const usernameBox = useRef(null);
    const passwordBox = useRef(null);
    const navigate = useNavigate();
    // this state is used to show the password 
    const [showPassword, setShowPassword] = useState(false);
    // this function is used to show the password when the user is on the eye icon
    const handlePasswordClick = () => {
      setPasswordMessage(
        "At least 8 digits long, a combination of characters and letters and capital letters."
      );
    };
    // this function is used to hide the message when the user is not on the password input field
    const handlePasswordBlur = () => {
      setPasswordMessage("");
    };
    // this function is used to check if the user is in the userList
    const handleSignIn = async() => {

      const username = usernameBox.current.value;
      const password = passwordBox.current.value;
      if (username === "" || password === "") {
        alert("Please enter a username and password");
        return;
      }
      const data = { username, password };
      const [statusNum, token, userID] = await loginServer(data);
      if(statusNum === 200){
        const userData = await getUserData(token,userID);
        setUserExists(true);
        setToken(token);
        setUserLoggedIn(userData);
        navigate("/pid");
    } else {
      alert("Invalid username or password");
    }
  };
    // this function is used to go to the creating page to create a new user
    const handleSignUp = () => {
      // Pass userList and setuserList to the Creating component
      navigate("/Creating");
    };
    // this function is used to show the password when the user is on the eye icon
    const togglePasswordVisibility = () => {
      setShowPassword(!showPassword);
    };
    return (
      <div className="entrance">
        <div className="input-group flex-nowrap p-2">
          <input
            type="text"
            className="form-control"
            placeholder="email or phone number"
            aria-label="Username"
            aria-describedby="addon-wrapping"
            ref={usernameBox}
          />
        </div>
        <div className="input-group flex-nowrap p-2">
          <button type="button" className="btn btn-light">
            <i
              className="bi bi-eye-slash"
              onMouseOver={togglePasswordVisibility}
              onMouseLeave={togglePasswordVisibility}
              data-testid="eye-slash-icon"
            ></i>
          </button>
          <input
            type={showPassword ? "text" : "password"}
            className="form-control"
            placeholder="password"
            aria-label="Password"
            aria-describedby="addon-wrapping"
            ref={passwordBox}
            onClick={handlePasswordClick}
            onBlur={handlePasswordBlur}
          />
        </div>
        {passwordMessage && <div className="message">{passwordMessage}</div>}
        <SignInButton onClick={handleSignIn} />
        <hr className="hrSize" />
        <SignUpButton onClick={handleSignUp}></SignUpButton>
      </div>
    );
  }

export default Entrance;
