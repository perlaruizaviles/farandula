import React from "react";
import {Control, Form} from "react-redux-form";

class UserForm extends React.Component {
  handleSubmit(user) {
    // Do whatever you like in here.
    // If you connect the UserForm to the Redux store,
    // you can dispatch actions such as:
    // dispatch(actions.submit('user', somePromise));
    // etc.
  }
  render() {
    return (
      <Form model="user"
            onSubmit={(user) => this.handleSubmit(user)}>
        <label>First name:</label>
        <Control.text model="user.firstName" />

        <label>Last name:</label>
        <Control.text model="user.lastName" />

        <button type="submit">
          Finish registration!
        </button>
      </Form>
    );
  }
}

export default UserForm;