import React from 'react';
import {Card} from 'semantic-ui-react';

const SummaryCard = () => (
    <Card>
        <Card.Content>
            <Card.Header>
                Summary
            </Card.Header>
            <Card.Meta>
                Aeromexico, round-trip, economy, 2 adults
            </Card.Meta>
        </Card.Content>
        <Card.Content>
            Depart Wed 5/17:
            HMO > NRT 5:39p-6:20a
            Flight 713 > Flight 58

            Return Fri 5/26: NRT > HMO 2:25p – 5:04p Flight 57 > Flight 706
        </Card.Content>
        <Card.Content>
            <Card.Header>
                Charges
            </Card.Header>
        </Card.Content>
        <Card.Content>
            2 adults, economy USD 1604.00
        </Card.Content>
        <Card.Content>
            Taxes, Fees and Surcharges USD 243.98
        </Card.Content>
        <Card.Content>
            <Card.Header>
                Total Cost USD 1847.98
            </Card.Header>
        </Card.Content>
        <Card.Content>
            Trip Protection <div className="right">Not Selected</div>
        </Card.Content>
    </Card>
);

export default SummaryCard;