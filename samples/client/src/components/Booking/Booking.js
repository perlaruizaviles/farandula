import React from "react";
import {Container, Divider, Grid} from "semantic-ui-react";
import SummaryCard from "./SummaryCard";
import SummaryDescription from "./SummaryDescription";
import BookingForm from "./BookingForm";


class Summary extends React.Component {

  submit = (values) => {
    // Do something with the form values
    console.log(values);
  };

  render() {
    return (
      <Grid>
        <Grid.Row>
          <Grid.Column width={11}>
            <Container text>
              <Grid.Row>
                <SummaryDescription/>
              </Grid.Row>
              <Divider />
              <Grid.Row>
                <BookingForm onSubmit={this.submit}/>
              </Grid.Row>
            </Container>

          </Grid.Column>
          <Grid.Column width={5}>
            <SummaryCard />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    );
  }
}

export default Summary;