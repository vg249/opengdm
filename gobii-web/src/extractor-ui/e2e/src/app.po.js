System.register(["protractor"], function (exports_1, context_1) {
    "use strict";
    var protractor_1, AppPage;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (protractor_1_1) {
                protractor_1 = protractor_1_1;
            }
        ],
        execute: function () {
            AppPage = /** @class */ (function () {
                function AppPage() {
                }
                AppPage.prototype.navigateTo = function () {
                    return protractor_1.browser.get(protractor_1.browser.baseUrl);
                };
                AppPage.prototype.getTitleText = function () {
                    return protractor_1.element(protractor_1.by.css('app-root .content span')).getText();
                };
                return AppPage;
            }());
            exports_1("AppPage", AppPage);
        }
    };
});
//# sourceMappingURL=app.po.js.map