import "./LeftSide.css"
import UpOptions from "./UpOptions/UpOptions"; 
import DownOptions from "./DownOptions/DownOptions";
// this component is the left side of the page, it contains the up and down options
function LeftSide({userLoggedIn , token,setMode,setProfileId}){
    return (
      <div className="leftbar">
        <UpOptions
          setMode={setMode}
          setProfileId={setProfileId}
          userLoggedIn={userLoggedIn}>
        </UpOptions>
        <DownOptions
          setMode={setMode}
          setProfileId={setProfileId}
          userLoggedIn={userLoggedIn}
          token={token}
        ></DownOptions>
      </div>
    );
} 
export default LeftSide;