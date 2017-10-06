app.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/app/config');
    $stateProvider.state('index', {
        url: '/app',
        templateUrl: "tpl/app.html"
    })
        .state('index.config', {
            url: '/config',
            templateUrl: "tpl/tone/config.index.html",
            resolve: {
                deps: ['uiLoad', '$ocLazyLoad', function(uiLoad, $ocLazyLoad) {
                    return $ocLazyLoad.load([
                        'ui.select',
                        'textAngular'
                    ]).then(function() {
                        return uiLoad.load([
                            'js/controller/configManagerCtrl.js',
                            'js/controller/configCreateModalCtrl.js'
                        ]);
                    });
                }]
            }
        })
        .state('index.app', {
            url: '/info',
            templateUrl: "tpl/tone/config.app.html"
        })
})