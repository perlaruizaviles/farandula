import React from "react";
import {Card} from "semantic-ui-react";

const SummaryCard = () => (
  <Card className='orange fixed'>
    <Card.Content>
      <Card.Header>
        Summary
      </Card.Header>
      <Card.Meta>
        Aeromexico, round-trip, economy, 2 adults
      </Card.Meta>
    </Card.Content>
    <Card.Content>
      <p>
        Depart Wed 5/17:<br/>
        HMO > NRT 5:39p-6:20a<br/>
        Flight 713 > Flight 58
      </p>
      <br/>
      <p>
        Return Fri 5/26:<br/>
        NRT > HMO 2:25p – 5:04p<br/>
        Flight 57 > Flight 706
      </p>
    </Card.Content>
    <Card.Content>
      <Card.Header>
        Charges
      </Card.Header>
    </Card.Content>
    <Card.Content>
      2 adults, economy <span style={{float: 'right'}}>USD 1604.00</span>
    </Card.Content>
    <Card.Content>
      Taxes, Fees and Surcharges <span style={{float: 'right'}}>USD 243.98</span>
    </Card.Content>
    <Card.Content>
      <Card.Header>
        Total Cost <span style={{float: 'right'}}>USD 1847.98</span>
      </Card.Header>
    </Card.Content>
    <Card.Content>
      Trip Protection
      <div className="right">Not Selected</div>
    </Card.Content>
  </Card>
);

export default SummaryCard;