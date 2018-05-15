System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Vertex;
    return {
        setters: [],
        execute: function () {
            Vertex = (function () {
                function Vertex(vertexId, vertexName, entityType, cvGroupname, filterVals) {
                    this.vertexId = vertexId;
                    this.vertexName = vertexName;
                    this.entityType = entityType;
                    this.cvGroupname = cvGroupname;
                    this.filterVals = filterVals;
                }
                return Vertex;
            }());
            exports_1("Vertex", Vertex);
        }
    };
});
//# sourceMappingURL=vertex.js.map