import React, { Component } from "react";
import metadataServices from './../services/MetadataServices';
import dataServices from './../services/DataServices';

export default class Control extends Component {
  constructor(props) {
    super(props);
    this.state = {
        value: props.value,
        selectList: []
    };
    this.handleChange = this.handleChange.bind(this);
  }

  componentDidMount() {
    if(this.props.controlMetadata.controlType === "REFERENCE") {
        fetch(metadataServices.getLinkByRel(this.props.controlMetadata.typeRel))
        .then((res) => res.json())
        .then(
            (result) => {
                this.setState({
                    ...this.state,
                    selectList: dataServices.getObjectListByRequest(result, this.props.controlMetadata.typeRel)
                  });
            },
            (error) => {}
        );
    }
  }

  handleChange(event) {
    this.setState({ value: event.target.value });
    this.props.handleControlChange(
      this.props.controlMetadata.name,
      this.props.fieldPosition,
      event.target.value
    );
  }

  render() {
    if (this.props.controlMetadata.controlType === "STRING" || this.props.controlMetadata.controlType === "NUMBER") {
      return (
        <input
          type="text"
          defaultValue={this.state.value}
          onChange={this.handleChange}
        />
      );
    } else if (this.props.controlMetadata.controlType === "DATE") {
        return (
          <input type="date"
          defaultValue={this.state.value}
          onChange={this.handleChange}
          />
        );
    } else if (this.props.controlMetadata.controlType === "REFERENCE") {
      var optionsList = [];
      var i = 0;
      for (const option of this.state.selectList) {
        optionsList.push(
            <option key={i} value={option.link}>{option.representation}</option>
        );
        i++;
      };
      return (
        <select value={this.state.value} onChange={this.handleChange}>
          {optionsList}
        </select>
      );
    } else {
      return <label>no render</label>;
    }
  }
}
