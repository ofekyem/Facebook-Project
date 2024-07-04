import { useNavigate } from "react-router-dom";
import React, { useState , useRef } from "react";
import "./Form_Create.css"
import {validatePassword,isUsernameExists} from "./Authentication";
import { registerServer } from '../ServerCalls/register';
import { convertToBase64 } from "../UsableFunctions/ImageFunctions";
// this function get the userList and setuserList as props.
function Form_Create() {
  /*
    this component will render the form to create a new user
    */
  const [passwordMessage, setPasswordMessage] = useState("");
  const navigate = useNavigate();
  // all the useRef are used to get the value of the input fields
  const usernameBox = useRef("");
  const passwordBox = useRef("");
  const passwordCheckBox = useRef("");
  const displayName = useRef("");
  const photo = useRef("");
  // this state is used to show the password
  const [showPassword, setShowPassword] = useState(false);
  const [showPasswordCheck, setShowPasswordCheck] = useState(false);
  // this function is used to show the password when the user is on the eye icon
  const handleTogglePassword = () => {
    setShowPassword((prevShowPassword) => !prevShowPassword);
  };
  // this function is used to show the password when the user is on the eye icon
  const handleTogglePasswordCheck = () => {
    setShowPasswordCheck((prevShowPassword) => !prevShowPassword);
  };
  // this function shows a message when the user is on the password input field of what is a valid password
  const handlePasswordClick = () => {
    setPasswordMessage(
      "At least 8 digits long, a combination of characters and letters and capital letters."
    );
  };
  // this function is used to hide the message when the user is not on the password input field
  const handlePasswordBlur = () => {
    setPasswordMessage("");
  };
  // this function is used to go back to the login page
  const goBack = () => {
    navigate("/");
  };

  // this function is used to create a new user and add it to the userList
  const createUser = async () => {
    // get the value of the input fields
    const username = usernameBox.current.value;
    const password = passwordBox.current.value;
    const confirmPassword = passwordCheckBox.current.value;
    const displayNameValue = displayName.current.value;
    const photoFile = photo.current.files[0];
    // check if the user filled all the information
    if (
      !username ||
      !password ||
      !confirmPassword ||
      !displayNameValue ||
      !photoFile
    ) {
      alert("need to fill all the information");
      return;
    }
    // check if the username already exists
    // if (usernameExists) {
    //     alert("username already exists");
    //     return;
    // }
    // check if the password is valid
    if (!validatePassword(password)) {
      alert("write valid password");
      return;
    }
    // check if the password and the confirm password are the same
    if (password !== confirmPassword) {
      alert("passwords don't match!");
      return;
    }
    // Convert the photo file to a base64 string
    const photoFilePath = await convertToBase64(photoFile);

    // create a new user if all the information is valid
    const newUser = {
      username: username,
      password: password,
      displayName: displayNameValue,
      photo: photoFilePath,
    };
    const response = await registerServer(newUser);
    if (response === 200) {
      // add the new user to the userList and uodate the userList state and navigate to the login page
      alert("user created!");
      navigate("/");
    }
    if (response === 404) {
      alert("username already exists");
    }
  };
  return (
    <div>
      <div>
        <h1>Sign Up</h1>
        it's quick and easy
      </div>
      <div className="mb-3">
        <label htmlFor="exampleFormControlInput1" className="form-label">
          Username
        </label>
        <input
          type="text"
          className="form-control"
          placeholder="email or phone number"
          aria-label="Username"
          aria-describedby="addon-wrapping"
          ref={usernameBox}
        ></input>
      </div>
      <label htmlFor="exampleFormControlInput1" className="form-label">
        Password
      </label>
      <div className="input-group flex-nowrap p-2">
        <button type="button" className="btn btn-light">
          <i
            className="bi bi-eye-slash"
            onMouseOver={handleTogglePassword}
            onMouseLeave={handleTogglePassword}
            data-testid="eye-slash-icon"
          ></i>
        </button>
        <input
          type={showPassword ? "text" : "password"}
          className="form-control"
          placeholder="enter password"
          aria-label="Password"
          aria-describedby="addon-wrapping"
          ref={passwordBox}
          onClick={handlePasswordClick}
          onBlur={handlePasswordBlur}
        ></input>
      </div>
      {passwordMessage && <div className="message">{passwordMessage}</div>}
      <label htmlFor="exampleFormControlInput1" className="form-label">
        Confirm Password
      </label>
      <div className="input-group flex-nowrap p-2">
        <button type="button" className="btn btn-light">
          <i
            className="bi bi-eye-slash"
            onMouseOver={handleTogglePasswordCheck}
            onMouseLeave={handleTogglePasswordCheck}
            data-testid="eye-slash-icon"
          ></i>
        </button>
        <input
          type={showPasswordCheck ? "text" : "password"}
          className="form-control"
          placeholder="confirm password"
          aria-label="Password"
          aria-describedby="addon-wrapping"
          onClick={handlePasswordClick}
          ref={passwordCheckBox}
          onBlur={handlePasswordBlur}
        />
      </div>
      <div className="mb-3">
        <label htmlFor="formFile" className="form-label">
          Choose pic
        </label>
        <input
          className="form-control"
          type="file"
          id="image"
          accept="image/*"
          ref={photo}
          data-testid="file-input"
        />
      </div>
      <div className="mb-3">
        <label htmlFor="exampleFormControlInput1" className="form-label">
          Display Name
        </label>
        <input
          type="text"
          className="form-control"
          placeholder="enter display name"
          aria-label="Username"
          aria-describedby="addon-wrapping"
          ref={displayName}
        ></input>
      </div>
      <div>
        <button
          type="button"
          className="btn btn-primary p-2 signIn"
          onClick={createUser}
        >
          Sign Up
        </button>
      </div>
      <hr className="hrSize" />
      <div>
        <button
          type="button"
          className="btn btn btn-success p-2 m-2 signUp"
          onClick={goBack}
        >
          Go Back
        </button>
      </div>
    </div>
  );
}

export default Form_Create