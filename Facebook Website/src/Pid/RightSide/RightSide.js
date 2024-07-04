import "./RightSide.css" 
/*
this component is the right side of the page, it contains the events and the advertisments
*/
function RightSide(){
    return(
        <div className="rightbar">
            <div className="rightbar_title">
                <h4>Events</h4> 
                <p className="see">See All</p>
            </div> 
            <div className="events"> 
                <div className="left_events">
                    <h3>3</h3>
                    <span>March</span>  
                </div>
                <div className="right_events">
                    <h6>Advanced Programming Lesson</h6>
                    <p className="location"><i className="material-icons">location_on</i>Bar Ilan University</p>
                    <p className="info">More Info</p>
                </div>
            </div>
            <div className="events"> 
            <div className="left_events">
                    <h3>10</h3>
                    <span>March</span>  
                </div>
                <div className="right_events">
                    <h6>Advanced Programming Lesson</h6>
                    <p className="location"><i className="material-icons">location_on</i>Bar Ilan University</p>
                    <p className="info">More Info</p>
                </div>
            </div>
            <div className="rightbar_title">
                <h4>Advertisments</h4> 
                <p className="see">Close</p>
            </div> 
            <img src="https://www.monetizemore.com/wp-content/uploads/2020/10/What-are-HTML5-ads_.jpg" className="adpic"></img>
            <img src="https://0701.static.prezi.com/preview/v2/dwhvt5nucrwtqpwpzs5svisvjl6jc3sachvcdoaizecfr3dnitcq_1_0.png" className="adpic"></img>
        </div>
    );
}
export default RightSide;