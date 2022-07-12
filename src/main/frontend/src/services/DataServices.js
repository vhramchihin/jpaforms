
const dataServices = {

    getObjectListByRequest: function(requestResult, objectName) {
        var objectList = [];
        objectList.push({
          representation: "",
          link: undefined
        });
        requestResult._embedded[objectName].forEach(function (item) {
          var objectEntry = {
            representation: item.representation,
            link: item._links.self.href
          };
          objectList.push(objectEntry);
        });
        return objectList;
    },

    getNewEntityData: function(entityMetadata) {

      var fields = {};
      var tables = {};

      for (const metadata of entityMetadata.fields) {

        fields[metadata.name] = "";

      }

      for (const metadata of entityMetadata.tables) {

        tables[metadata.name] = [];

      }

      var result = {
        fields: fields,
        tables: tables
      }

      return result;

    }

}

export default dataServices;