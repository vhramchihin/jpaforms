import React, { Component } from "react";
import metadataServices from "./../services/MetadataServices";
import dataServices from "./../services/DataServices";
import Control from "./Control";
import { Redirect } from "react-router-dom";

export default class ObjectEdit extends Component {

  constructor(props) {
    super(props);
    this.state = {
      link: this.props.location.state.link,
      listLink: this.props.location.state.listLink,
      new: this.props.location.state.new,
      objectName: props.match.params.object_name,
      objectId: props.match.params.object_id,
      entityMetadata: {},
      entityData: {},
      completed: false,
      redirectToList: false
    };
    this.handleControlChange = this.handleControlChange.bind(this);
    this.handleSave = this.handleSave.bind(this);
    this.handleNewLine = this.handleNewLine.bind(this);
  }

  componentDidMount() {
    var entityMetadata = metadataServices.getEntityMetadata(
      this.state.objectName
    );
    if (entityMetadata === undefined) {
      fetch(
        //"http://localhost:8080/api/entity_metadata/" + this.state.objectName
        "/api/entity_metadata/" + this.state.objectName
      )
        .then((res) => res.json())
        .then(
          (result) => {
            var entityMetadata = metadataServices.putEntityMetadata(
              this.state.objectName,
              result
            );
            this.completeSetEntityMetadata(entityMetadata);
          },
          (error) => {}
        );
    } else {
      this.completeSetEntityMetadata(entityMetadata);
    }
  }

  completeSetEntityMetadata(entityMetadata) {
    this.setState({
      ...this.state,
      entityMetadata: entityMetadata,
    });

    if (this.state.new === true) {
      var entityData = dataServices.getNewEntityData(entityMetadata);
      this.completeSetEntityData(entityData);
    } else {
      fetch(this.state.link)
        .then((res) => res.json())
        .then(
          (result) => {
            var entityData = this.getEntityData(result);
            this.completeSetEntityData(entityData);
          },
          (error) => {}
        );
    }
  }

  completeSetEntityData(entityData) {
    this.setState({
      ...this.state,
      entityData: entityData,
      completed: true,
    });
  }

  getEntityData(requestResult) {
    var filedsData = {};
    var tablesData = {};

    this.state.entityMetadata.fields.forEach(function (metadata) {
      var value;
      value = requestResult[metadata.name];
      if (value === undefined) {
        value = requestResult._embedded[metadata.name]._links.self.href.replace(
          "{?projection}",
          ""
        );
      }
      if(metadata.controlType === "DATE") {
        filedsData[metadata.name] = value.split("T")[0];
      } else {
        filedsData[metadata.name] = value;
      }
    });

    this.state.entityMetadata.tables.forEach(function (metadata) {
      var lines = [];

      for (const lineData of requestResult[metadata.name]) {
        var line = {};
        for (const fieldMetadata of metadata.fieldsMetadata) {
          var value;
          value = lineData[fieldMetadata.name];
          if (value === undefined) {
            var currentFiledLineData = value = lineData._embedded[fieldMetadata.name];
            if(currentFiledLineData !== undefined) {
              value = currentFiledLineData._links.self.href.replace("{?projection}", "");
            }
          }
          line[fieldMetadata.name] = value;
        }
        lines.push(line);
      }

      tablesData[metadata.name] = lines;
    });

    console.log(tablesData);

    return {
      fields: filedsData,
      tables: tablesData,
    };
  }

  handleControlChange(fieldName, fieldPosition, value) {
    var newEntityData = this.state.entityData;
    if (fieldPosition === undefined) {
      newEntityData.fields[fieldName] = value;
    } else {
      newEntityData.tables[fieldPosition.tableName][fieldPosition.lineIndex][fieldName] = value;
    }
    this.setState({
      ...this.state,
      entityData: newEntityData,
    });

    console.log(this.state);
  }

  handleNewLine(tableName) {
    
    var newLine = {};

    for (const fieldMetadata of this.findTableMetadataByName(tableName).fieldsMetadata) {

      var value = undefined;
      if(fieldMetadata.controlType === "STRING") {
        value = "";
      } else if(fieldMetadata.controlType === "NUMBER") {
        value = 0;
      } else {
        value = null;
      }

      newLine[fieldMetadata.name] = value;
    }

    var newEntityData = this.state.entityData;
    newEntityData.tables[tableName].push(newLine);
    this.setState({
      ...this.state,
      entityData: newEntityData,
    });

  }

  findTableMetadataByName(tableName) {

    for (const tableMetadata of this.state.entityMetadata.tables) {
      if(tableMetadata.name === tableName) return tableMetadata;
    }

    return null;

  }

  handleSave() {
    var method = "PATCH";
    if (this.state.new === true) method = "POST";

    var requestBody = {};
    for (const key in this.state.entityData.fields) {
      requestBody[key] = this.state.entityData.fields[key];
    }
    for (const key in this.state.entityData.tables) {
      requestBody[key] = this.state.entityData.tables[key];
    }

    fetch(this.state.link, {
      method: method,
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestBody),
    })
      .then((res) => res.json())
      .then(
        (result) => {
          console.log(result);
          this.setState({
            ...this.state,
            redirectToList: true
          });
        },
        (error) => {}
      );
  }

  render() {

    if (this.state.redirectToList === true) {
      return <Redirect to={this.state.listLink} />
    }

    if (this.state.completed) {
      var controls = [];
      var tables = [];
      var i = 0;
      for (const metadata of this.state.entityMetadata.fields) {
        controls.push(
          <div key={i}>
            <label>
              {metadata.name}
              <Control
                value={this.state.entityData.fields[metadata.name]}
                controlMetadata={metadata}
                handleControlChange={this.handleControlChange}
              />
            </label>
          </div>
        );
        i++;
      }
      var tableId = 0;
      for (const tableMetadata of this.state.entityMetadata.tables) {
        var header = [];
        var lines = [];
        var headerId = 0;
        for (const fieldMetadata of tableMetadata.fieldsMetadata) {
          header.push(<th key={headerId}>{fieldMetadata.name}</th>);
          headerId++;
        }
        var lineId = 0;
        for (const lineData of this.state.entityData.tables[
          tableMetadata.name
        ]) {
          var cells = [];
          var cellId = 0;
          for (const fieldMetadata of tableMetadata.fieldsMetadata) {
            var fieldPosition = {
              tableName: tableMetadata.name,
              lineIndex: lineId,
            };
            cells.push(
              <td key={cellId}>
                <Control
                  value={lineData[fieldMetadata.name]}
                  controlMetadata={fieldMetadata}
                  fieldPosition={fieldPosition}
                  handleControlChange={this.handleControlChange}
                />
              </td>
            );
            cellId++;
          }
          lines.push(<tr key={lineId}>{cells}</tr>);
          lineId++;
        }

        tables.push(
          <div key={tableId}>
            <button onClick={() => this.handleNewLine(tableMetadata.name)}>new line</button>
            <table>
              <thead>
                <tr>{header}</tr>
              </thead>
              <tbody>{lines}</tbody>
            </table>
          </div>
        );
        tableId++;
      }
      return (
        <React.Fragment>
          <form>{controls}</form>
          {tables}
          <button onClick={this.handleSave}>save</button>
        </React.Fragment>
      );
    } else {
      return <div>rendering....</div>;
    }
  }
}