System.register(["./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var gobii_file_item_compound_id_1, GobiiFileItemCriterion;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            GobiiFileItemCriterion = class GobiiFileItemCriterion extends gobii_file_item_compound_id_1.GobiiFileItemCompoundId {
                constructor(gobiiFileItemCompoundId, _isPresent = false) {
                    super(gobiiFileItemCompoundId.getExtractorItemType(), gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getEntitySubType(), gobiiFileItemCompoundId.getCvFilterType(), gobiiFileItemCompoundId.getCvFilterValue());
                    this._isPresent = _isPresent;
                }
                isPresent() {
                    return this._isPresent;
                }
                setIPresent(value) {
                    this._isPresent = value;
                }
            };
            exports_1("GobiiFileItemCriterion", GobiiFileItemCriterion);
        }
    };
});
//# sourceMappingURL=gobii-file-item-criterion.js.map