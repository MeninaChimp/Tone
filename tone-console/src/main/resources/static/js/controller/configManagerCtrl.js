'use strict';

app.controller('ConfigManagerCtrl', ['$scope', 'toaster', '$modal', '$http', function ($scope, toaster, $modal, $http) {
    $scope.configs = {};
    $scope.app = {};
    $scope.appInfo = {};
    $scope.appInfoShow = false;
    $scope.configShow = false;
    $scope.grayReleaseStatus = false;

    /// see https://stackoverflow.com/questions/12553617/how-can-i-set-a-dynamic-model-name-in-angularjs
    $scope.grayRelease = {};

    initApps();

    $scope.$watch('appName', function () {
        if ($scope.appName !== undefined) {
            app_info();
            config_detail();
        }
    });

    function initApps() {
        var req = {
            method: 'GET',
            url: '/app/all'
        }
        $http(req).then(function (response) {
            response = response.data;
            $scope.apps = response.result;
        });
    }

    $scope.app_create = function () {
        var req = {
            method: 'POST',
            url: '/app/create',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: $scope.app

        }

        $http(req).then(function (response) {
            response = response.data;
            if (response.code == 0) {
                toaster.pop("success", "创建成功", "创建应用成功");
            } else {
                toaster.pop("error", "创建失败", response.msg);
            }
        }, function () {
            toaster.pop('error', '创建失败', '创建应用失败');
        });
    }

    $scope.initConfigs = function () {
        // app_info();
        config_detail();
    }

    function app_info() {
        var req = {
            method: 'GET',
            url: '/app/info/' + $scope.appName,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }

        $http(req).then(function (response) {
            response = response.data;
            $scope.appInfo = response.result;
            $scope.appInfoShow = true;
            if (response.code == 100) {
                toaster.pop("error", "获取应用详情失败", response.msg);
            }
        }, function () {
            toaster.pop('error', '查询失败', '获取应用详情失败');
        });
    }

    function config_detail() {
        var req = {
            method: 'GET',
            url: '/config/detail/' + $scope.appName,
            headers: {
                'Accept': 'application/json'
            }
        }

        $http(req).then(function (response) {
            response = response.data;
            $scope.configs = response.result;
            $scope.configShow = true;
            if (response.code == 100) {
                toaster.pop("error", "获取配置信息失败", response.msg);
            }
        }, function () {
            toaster.pop('error', '查询失败', '获取配置信息失败');
        });
    }

    $scope.config_update = function (config) {
        var req = {
            method: 'POST',
            url: '/config/update',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: {
                app: $scope.appName,
                key: config.key,
                value: config.value
            }
        }

        $http(req).then(function (response) {
            response = response.data;
            $scope.configs = response.result;
            config_detail();
            if (response.code == 0) {
                toaster.pop("success", "更新成功", "更新配置成功");
            } else {
                toaster.pop("error", "更新配置失败", response.msg);
            }
        }, function () {
            toaster.pop('error', '更新失败', '更新配置信息失败');
        });
    }

    $scope.config_remove = function (config) {
        var req = {
            method: 'POST',
            url: '/config/remove',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: {
                app: $scope.appName,
                key: config.key
            }
        }

        $http(req).then(function (response) {
            response = response.data;
            if (response.code == 0) {
                config_detail();
                toaster.pop("success", "删除成功", "删除配置成功");
            } else {
                toaster.pop("error", "删除配置失败", response.msg);
            }
        }, function () {
            toaster.pop('error', '删除失败', '删除配置信息失败');
        });
    }

    $scope.show_config_create = function (app) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'config_create.html',
            controller: 'ConfigCreateModalCtrl',
            resolve: {
                app: function () {
                    return app;
                }
            }
        });

        modalInstance.result.then(function () {
            config_detail();
        });
    };

    $scope.gray_release_switch = function () {
        $scope.grayReleaseStatus = !$scope.grayReleaseStatus;
    };

    $scope.gray_release = function (config, ip) {
        var req = {
            method: 'POST',
            url: '/config/grayRelease',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: {
                app: $scope.appName,
                key: config.key,
                value: config.value,
                ip: ip
            }
        }

        $http(req).then(function (response) {
            response = response.data;
            $scope.configs = response.result;
            config_detail()
            if (response.code == 0) {
                toaster.pop("success", "灰度发布", "灰度发布成功");
            } else {
                toaster.pop("error", "灰度发布", response.msg);
            }
        }, function () {
            toaster.pop('error', '灰度发布', '灰度发布失败');
        });
    };
}]);