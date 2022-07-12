import React from 'react';
import { BrowserRouter as Router, Route } from "react-router-dom";
import MetadataList from './components/MetadataList';
import ObjectList from './components/ObjectList';
import ObjectEdit from './components/ObjectEdit';

function App() {
  return (
    <Router>
      <Route exact path="/forms/metadata" component={MetadataList} />
      <Route exact path="/forms/object_list/:object_name" component={ObjectList} />
      <Route exact path="/forms/object_edit/:object_name/:object_id" component={ObjectEdit} />
      <Route exact path="/forms/object_edit/:object_name" component={ObjectEdit} />
    </Router>
  );
}


export default App;