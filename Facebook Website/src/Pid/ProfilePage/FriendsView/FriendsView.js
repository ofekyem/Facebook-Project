import "./FriendsView.css"
import React, { useState , useRef , useEffect } from "react";
function FriendsView({userLoggedIn,setProfileId, friends}) {
    const friendEnter = (profileId) =>{
        setProfileId(profileId)
    }
    return (
        <div className="friends-section">
            <div className="title-box"><i className="material-icons">people</i><h3>Friends</h3></div>
            <div className="friends-box">
            {friends && friends.map((friend) => (
                <div className="listf" key={friend._id} onClick={() => friendEnter(friend._id)}>
                    <div className="friend-img">
                        <img src={friend.photo} alt={friend.displayName} className="f-img" />
                    </div>
                    <p className="friend-string">{friend.displayName}</p>
                </div>
            ))}
        </div>
        </div>
    )
}
export default FriendsView;