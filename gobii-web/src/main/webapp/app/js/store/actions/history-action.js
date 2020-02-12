System.register([], function (exports_1, context_1) {
    "use strict";
    var ADD_STATUS, ADD_STATUS_MESSAGE, CLEAR_STATUS, ADD_FILTER_RETRIEVED, AddStatusAction, AddStatusMessageAction, AddFilterRetrieved, ClearStatusesAction;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            exports_1("ADD_STATUS", ADD_STATUS = '[GobiiHistory] Add Status');
            exports_1("ADD_STATUS_MESSAGE", ADD_STATUS_MESSAGE = '[GobiiHistory] Add Status Message');
            exports_1("CLEAR_STATUS", CLEAR_STATUS = '[GobiiHistory] Clear Status');
            exports_1("ADD_FILTER_RETRIEVED", ADD_FILTER_RETRIEVED = '[GobiiHistory] Add Filter Submitted');
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in fileItemsReducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            AddStatusAction = class AddStatusAction {
                constructor(payload) {
                    this.payload = payload;
                    this.type = ADD_STATUS;
                }
            };
            exports_1("AddStatusAction", AddStatusAction);
            AddStatusMessageAction = class AddStatusMessageAction {
                constructor(payload) {
                    this.payload = payload;
                    this.type = ADD_STATUS_MESSAGE;
                }
            };
            exports_1("AddStatusMessageAction", AddStatusMessageAction);
            AddFilterRetrieved = class AddFilterRetrieved {
                constructor(payload) {
                    this.payload = payload;
                    this.type = ADD_FILTER_RETRIEVED;
                }
            };
            exports_1("AddFilterRetrieved", AddFilterRetrieved);
            ClearStatusesAction = class ClearStatusesAction {
                constructor() {
                    this.type = CLEAR_STATUS;
                }
            };
            exports_1("ClearStatusesAction", ClearStatusesAction);
        }
    };
});
//# sourceMappingURL=history-action.js.map