import React from "react";
import TextMenu from "../Common/TextMenu";
import travelOptions from "../../data/travelOptions";
import {List} from "immutable";

export default () => (
  <div>
    <h1>Un menú para el tipo de viaje</h1>
    <TextMenu options={travelOptions.get('type')}
              selected="round-trip"
              selectType={(type) => console.log(`Selected ${type} type`)}/>
  </div>
);