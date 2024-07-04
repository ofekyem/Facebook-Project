import { useEffect, useState } from "react";
import "./NaviBar.css" 
import { useNavigate } from "react-router-dom";
import { getUserData } from "../../ServerCalls/login";
import { acceptFriendRequestServer , deleteFriendRequestServer } from "../../ServerCalls/userCalls";
function NaviBar({userLoggedIn, setUserLoggedIn,toggleDarkMode,darkMode,setMode,token,setToken,refresh,setProfileId}){
    const [friendsRequests, setFriendsRequests] = useState([]);
    const [render, setRender] = useState(false);
    
    useEffect (() => {
        if (token && userLoggedIn) {
            const fetchFriendRequests = async () => {
                const requests = await Promise.all(userLoggedIn.friendRequests.map(id => getUserData(token, id)));
                setFriendsRequests(requests);
            };
            fetchFriendRequests();
        }
    }, [token, userLoggedIn]);

    const navigate = useNavigate();
    const goBack = () => {
        setUserLoggedIn(false);
        setToken(false);
        navigate("/");
    }; 

    const enterProfile = () => {
        setMode(2);
        setProfileId(userLoggedIn._id);
    };

    const acceptFriendRequest = async (friendId) => {
        const res = await acceptFriendRequestServer(token, userLoggedIn._id, friendId);
        if (res === 200) {
            alert("Friend request accepted successfully");
            setRender(!render);
            refresh();
        } else {
            alert("There was a problem with the fetch operation: ", res);
        }
    };

    const deleteFriendRequest = async (friendId) => {
        const res = await deleteFriendRequestServer(token, userLoggedIn._id, friendId);
        if (res === 200) {
            alert("Friend request deleted successfully");
            setRender(!render);
            refresh();
        } else {
            alert("There was a problem with the fetch operation: ", res);
        }
    };

    return(
        <nav className="navBody" date-testid="navibar">
            <div className="nav__left">
                <img src="https://brandlogos.net/wp-content/uploads/2021/04/facebook-icon-512x512.png" onClick={() => setMode(0)} className="nav_left_img"></img> 
                <div className="nav__search">
                    <i className="material-icons">search</i>
                    <input type="text" placeholder="Search Facebook"></input>
                </div>
            </div>
            <div className="nav__mid"> 
                <a className="iconz" onClick={() => setMode(0)}>
                    <i className="material-icons">home</i>
                </a>
                <a className="iconz">
                    <i className="material-icons">slideshow</i>
                </a>
                <a className="iconz">
                    <i className="material-icons">groups</i>
                </a>
                <a className="iconz">
                    <i className="material-icons">gamepad</i>
                </a>
            </div> 
            <div className="nav__right">
                <div className="avatar" onClick={enterProfile} >
                    <img src={userLoggedIn.photo} className="avatar__img"></img>
                    <span><strong>{userLoggedIn.displayName}</strong></span>
                </div> 
                <div className="buttons" > 
                    <a className="button">
                        <i className="material-icons">apps</i>
                    </a>
                    <li className={friendsRequests.length > 0 ? "nav_item_dropdown_no" : "nav_item_dropdown"}>
                        <a className="button" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i className="material-icons">notifications</i>
                            {friendsRequests.length > 0 &&
                                <span className="notification-badge">{friendsRequests.length}</span>
                            }
                        </a>
                        <ul className="dropdown-menu">
                            {friendsRequests.map(friend => (
                                <li key={friend._id} className="dropdown-item">
                                    <img src={friend.photo} alt={friend.displayName} style={{width: '30px', height: '30px', marginRight: '10px'}} />
                                    <span>{friend.displayName}</span>
                                    <button className="button_friend" onClick={() => acceptFriendRequest(friend._id)}><i className="bi bi-person-plus-fill"></i></button>
                                    <button className="button_friend"onClick={() => deleteFriendRequest(friend._id)}><i className="bi bi-person-x"></i> </button>
                                </li>
                            ))}
                        </ul>
                    </li>
                    <a className="button">
                        <i className="material-icons">messenger</i>
                    </a>
                    <li className="nav_item_dropdown">
                        <a className="button" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <i className="material-icons">settings</i>
                        </a>
                        <ul className="dropdown-menu">
                            <li><a className="dropdown-item" onClick={toggleDarkMode}>{darkMode ? 'Light Mode' : 'Dark Mode'}</a></li>
                            <li><a className="dropdown-item" onClick={() => setMode(1)}>Edit User</a></li>
                            <li><a className="dropdown-item" onClick={goBack}>Log Out</a></li>
                        </ul>
                    </li>
                </div>
            </div>
        </nav> 
    );
}
export default NaviBar;