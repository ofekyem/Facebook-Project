import "./EditUser.css"
import React, { useState , useRef } from "react";
import { validatePassword } from "../../Creating/Authentication";
import { deleteUser , updateUser} from "../../ServerCalls/userCalls";
import { useNavigate } from "react-router-dom";
import { convertToBase64 } from "../../UsableFunctions/ImageFunctions";
function EditUser({userLoggedIn, setUserLoggedIn, token, setToken , setMode,refresh}) {
    const usernameBox = useRef(userLoggedIn.username);
    const passwordBox = useRef("");
    const passwordCheckBox = useRef("");
    const displayName = useRef(userLoggedIn.displayName);
    const photo = useRef(userLoggedIn.photo);
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [showPasswordCheck, setShowPasswordCheck] = useState(false);
    const handleTogglePassword = () => {
        setShowPassword((prevShowPassword) => !prevShowPassword);
      };
      // this function is used to show the password when the user is on the eye icon
      const handleTogglePasswordCheck = () => {
        setShowPasswordCheck((prevShowPassword) => !prevShowPassword);
      };
    const deleteUserAndLogOut = async () => {
        const status = await deleteUser(token, userLoggedIn._id);
        if (status === 200) {
            setUserLoggedIn(false);
            alert("User deleted");
            setToken("");
            navigate("/");
        }
        else {
            alert("There was a problem deleting the user");
        }
    }
    const editUser = async () => {
        const username = usernameBox.current.value;
        const password = passwordBox.current.value;
        const confirmPassword = passwordCheckBox.current.value;
        const display = displayName.current.value;
        let photoUrl = userLoggedIn.photo;
        if (photo.current.files[0]) { // if a new file is selected
            photoUrl = await convertToBase64(photo.current.files[0]);
        }
        if(password !== "" || confirmPassword !== ""){
            if (password !== confirmPassword) {
                alert("Passwords do not match");
                return;
            }
            if (!validatePassword(password)) {
                alert("Password is not valid, you need at least 8 digits long, a combination of characters and letters and capital letters");
                return;
            }
        }
        if (username === "" || display === "") {
            alert("Username and display name are required");
            return;
        }
        const user = {
            _id: userLoggedIn._id,
            username: username,
            displayName: display,
            password: password,
            photo: photoUrl,
            postList: userLoggedIn.postList,
            friendsList: userLoggedIn.friendsList,
            friendRequests: userLoggedIn.friendRequests,
            friendRequestsSent : userLoggedIn.friendRequestsSent,
        };
        const status = await updateUser(token, user);
        
        if (status === 200) {
            setUserLoggedIn(user);
            setMode(0);
            refresh();
            alert("User updated");
        }
        else {
            alert("There was a problem updating the user / username already exists");
        }
    }
    
   
    return(
        <div>
            <h1>Edit User</h1>
            <div className="mb-3">
                <div className="form-group">
                    <label>Username</label>
                    <input type="text" className="form-control" ref={usernameBox} placeholder="Enter username" defaultValue={userLoggedIn.username} />
                </div>
                
                <div className="form-group">
                    <label>New Password (if not remain old one)</label> 
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
                            placeholder="password"
                            aria-label="Password"
                            aria-describedby="addon-wrapping"
                            ref={passwordBox}
                        />
                    </div>
                </div>
                <div className="form-group">
                    <label>Confirm Password</label>
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
                            placeholder="check password"
                            aria-label="Password"
                            aria-describedby="addon-wrapping"
                            ref={passwordCheckBox}
                        />
                    </div>
                </div>
                <div className="form-group">
                    <label>Display Name</label>
                    <input type="text" className="form-control" ref={displayName} placeholder="Enter display name" defaultValue={userLoggedIn.displayName} />
                </div>
                <div className="form-group">
                    <label>Photo (if not remain old one)</label>
                    {userLoggedIn.photo && <img src={userLoggedIn.photo} className="avatar__img" alt="Current" />}
                    <input type="file" className="form-control" id = "image" accept="image/*" ref={photo} />
                </div>
                <div>
                    <button type="button" className="btn btn-primary editb" onClick ={editUser} >Submit</button>
                </div>
                <div>
                    <button type="button" className="btn btn-primary editb" onClick={deleteUserAndLogOut} >Delete User</button>
                </div>
                <div>
                    <button type="button" className="btn btn-primary editb" onClick={() => setMode(0)}>Cancel</button>
                </div>
            </div>
        </div>
    )
}
export default EditUser;