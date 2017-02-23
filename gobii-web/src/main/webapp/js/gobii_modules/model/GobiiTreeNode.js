System.register(["./type-entity"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, GobiiTreeNode;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            }
        ],
        execute: function () {
            GobiiTreeNode = (function () {
                function GobiiTreeNode() {
                    this.entityType = type_entity_1.EntityType.UNKNOWN;
                    this.children = [];
                }
                return GobiiTreeNode;
            }());
            exports_1("GobiiTreeNode", GobiiTreeNode);
        }
    };
});
//# sourceMappingURL=GobiiTreeNode.js.map