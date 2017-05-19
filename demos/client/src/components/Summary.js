import React from "react";
import {Button, Container, Divider, Grid} from "semantic-ui-react";
import SummaryCard from "./SummaryCard";
import SummaryDescription from "./SummaryDescription";
import ContactForm from "./SummaryForm";


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
                <ContactForm onSubmit={this.submit}/>
              </Grid.Row>
              <Grid.Row>
                <Button color='orange'>Book for USD 1847.98</Button>
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