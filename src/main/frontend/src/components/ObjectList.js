import React, { Component } from "react";
import { Link } from "react-router-dom";
import metadataServices from "./../services/MetadataServices";
import dataServices from "./../services/DataServices";

export default class ObjectList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      objectName: props.match.params.object_name,
      objectList: [],
    };
  }

  componentDidMount() {
    fetch(metadataServices.getLinkByRel(this.state.objectName))
      .then((res) => res.json())
      .then(
        (result) => {
          this.setState({
            ...this.state,
            objectList: dataServices.getObjectListByRequest(
              result,
              this.state.objectName
            ),
          });
        },
        (error) => {}
      );
  }

  render() {
    var links = [];

    var i = -2;
    links.push(
      <div key={i}>
        <Link
          to={{
            pathname: "/forms/metadata/"
          }}
        >
          home
        </Link>
      </div>
    );

    i = -1;
    links.push(
      <div key={i}>
        <Link
          to={{
            pathname: "/forms/object_edit/" + this.state.objectName + "/",
            state: {
              link: metadataServices.getLinkByRel(this.state.objectName),
              listLink: "/forms/object_list/" + this.state.objectName + "/",
              new: true
            },
          }}
        >
          new
        </Link>
      </div>
    );

    i = 0;
    for (var item of this.state.objectList) {
      links.push(
        <div key={i}>
          <Link
            to={{
              pathname: "/forms/object_edit/" + this.state.objectName + "/" + i,
              state: {
                link: item.link,
                listLink: "/forms/object_list/" + this.state.objectName + "/"
              },
            }}
          >
            {item.representation}
          </Link>
        </div>
      );
      i++;
    }
    return (
      <React.Fragment>
        {links}
      </React.Fragment>
    );
  }
}
