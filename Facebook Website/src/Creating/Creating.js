
import "./Creating.css"
import FormCreate from "./Form_Create"

import Introduction from '../Login/Introduction/Introduction';

function Creating() {
  /*
  this component will render the FormCreate component where in form create component
  we create a form to add a new user to the userList
  and in the introduction component we will render the introduction component
  */
  return (
    <div className="container px-4 text-center">
      <div className="col-4">
          <FormCreate></FormCreate>
      </div>
      <div className="col-6">
          <Introduction></Introduction>
      </div>
    </div>
  );
}

export default Creating;
