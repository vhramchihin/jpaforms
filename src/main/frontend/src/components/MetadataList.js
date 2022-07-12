import React, { Component } from "react";
import metadataServices from './../services/MetadataServices';

export default class MetadataList extends Component {
  constructor(props) {
    super(props);
    this.state = { metadataList: [] };
  }

  componentDidMount() {
    //fetch("http://localhost:8080/api/data")
    fetch("/api/data")
      .then((res) => res.json())
      .then(
        (result) => {
          metadataServices.putMetadata(result);
          this.setState({
            metadataList: metadataServices.getMetadata(),
          });
          console.log(this.state.metadataList);
        },
        (error) => {
        }
      );
  }
  render() {
    var links = [];
    var i = 0;
    this.state.metadataList.forEach(function (key) {
      links.push(
        <div key={i}>
          <a href={`/forms/object_list/${key.name}`}>{key.name}</a>
        </div>
      );
      i++;
    });
    return <React.Fragment>{links}</React.Fragment>;
  }

}
