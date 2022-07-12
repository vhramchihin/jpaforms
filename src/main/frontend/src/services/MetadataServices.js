
const metadataServices = {

    putMetadata: function(requestResult) {
        var result = [];
    
        Object.keys(requestResult._links).forEach(function (key) {
          if (key !== "profile") {
            var resultEntry = {
              name: key,
              link: requestResult._links[key].href.replace('{?projection}', ''),
            };
            result.push(resultEntry);
          }
        });

        localStorage.setItem('metadata_root', JSON.stringify(result));
        localStorage.setItem('metadata_entities', JSON.stringify([]));
        
      },

    getMetadata: function() {
        return JSON.parse(localStorage.getItem('metadata_root'));
    },

    getLinkByRel: function(rel) {
        var metadata = this.getMetadata();
        for (var i = 0; i < metadata.length; i++) {
            if(metadata[i].name === rel) return metadata[i].link;
        }
    },

    getEntityMetadata: function(rel) {

      var metadata_entities = JSON.parse(localStorage.getItem('metadata_entities'));
      for (var i = 0; i < metadata_entities.length; i++) {
          if(metadata_entities[i].name === rel) return metadata_entities[i].metadata;
      }

      return undefined;

    },

    putEntityMetadata: function(rel, requestResult) {
      
      var metadata_entities = JSON.parse(localStorage.getItem('metadata_entities'));
      var entityMetadata = {
        fields: requestResult.fieldsMetadata,
        tables: requestResult.tablesMetadata
      }
      metadata_entities.push(requestResult.rel, entityMetadata);

      return entityMetadata;
      
    }

}

export default metadataServices;