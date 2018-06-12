System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Vertex;
    return {
        setters: [],
        execute: function () {
            Vertex = (function () {
                function Vertex(vertexId, vertexNameType, gobiiVertexType, vertexName, entityType, entitySubType, cvGroup, cvTerm, filterVals) {
                    this.vertexId = vertexId;
                    this.vertexNameType = vertexNameType;
                    this.gobiiVertexType = gobiiVertexType;
                    this.vertexName = vertexName;
                    this.entityType = entityType;
                    this.entitySubType = entitySubType;
                    this.cvGroup = cvGroup;
                    this.cvTerm = cvTerm;
                    this.filterVals = filterVals;
                }
                return Vertex;
            }());
            exports_1("Vertex", Vertex);
        }
    };
});
//# sourceMappingURL=vertex.js.map