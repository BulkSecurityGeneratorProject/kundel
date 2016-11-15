(function() {
    'use strict';

    angular
        .module('kundelApp')
        .controller('AuthorsDetailController', AuthorsDetailController);

    AuthorsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Authors'];

    function AuthorsDetailController($scope, $rootScope, $stateParams, previousState, entity, Authors) {
        var vm = this;

        vm.authors = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kundelApp:authorsUpdate', function(event, result) {
            vm.authors = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
