app.controller('ConfigCreateModalCtrl', ['$scope', '$stateParams', 'toaster', '$modalInstance', 'app',
    '$http', function ($scope, $stateParams, toaster, $modalInstance, app, $http) {
        $scope.config = {};
        $scope.config.app = app;
        $scope.config_create = function () {
            var req = {
                method: 'POST',
                url: '/config/create',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: $scope.config
            }

            $http(req).then(function (response) {
                response = response.data;
                if (response.code == 0) {
                    toaster.pop("success", "创建成功", "创建配置成功");
                    $modalInstance.close();
                } else {
                    toaster.pop("error", "创建失败", response.msg);
                }
            }, function () {
                toaster.pop('error', '创建失败', '创建配置失败');
            });
        }

        $scope.close_modal = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);