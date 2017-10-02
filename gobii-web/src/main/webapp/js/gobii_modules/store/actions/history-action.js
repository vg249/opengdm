System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ADD_STATUS, CLEAR_STATUS, AddStatusAction, ClearStatusesAction;
    return {
        setters: [],
        execute: function () {
            exports_1("ADD_STATUS", ADD_STATUS = '[GobiiStatusMessage] Add Status');
            exports_1("CLEAR_STATUS", CLEAR_STATUS = '[GobiiStatusMessage] Clear Status');
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in fileItemsReducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            AddStatusAction = (function () {
                function AddStatusAction(payload) {
                    this.payload = payload;
                    this.type = ADD_STATUS;
                }
                return AddStatusAction;
            }());
            exports_1("AddStatusAction", AddStatusAction);
            ClearStatusesAction = (function () {
                function ClearStatusesAction() {
                    this.type = CLEAR_STATUS;
                }
                return ClearStatusesAction;
            }());
            exports_1("ClearStatusesAction", ClearStatusesAction);
        }
    };
});
//# sourceMappingURL=history-action.js.map