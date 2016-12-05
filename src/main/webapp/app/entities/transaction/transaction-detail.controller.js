(function() {
    'use strict';

    angular
        .module('kundelApp')
        .controller('TransactionDetailController', TransactionDetailController);

    TransactionDetailController.$inject = ['$http', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Transaction'];

    function TransactionDetailController($http, $scope, $rootScope, $stateParams, previousState, entity, Transaction) {
        var vm = this;

        vm.transaction = entity;
        vm.previousState = previousState.name;
        vm.url = 'http://helion.pl/eksiazki/angularjs-pierwsze-kroki-dariusz-kalbarczyk-arkadiusz-kalbarczyk,angupk.htm';
        fetchData();


        function fetchData() {
            $http.get('api/books/' + vm.transaction.idBook).success(function(bookData) {
                vm.transaction.book = bookData;
            });
            $http.get('api/users/' + vm.transaction.idUser).success(function(userData) {
                vm.transaction.user = userData;
            });
        }

        var unsubscribe = $rootScope.$on('kundelApp:transactionUpdate', function(event, result) {
            vm.transaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
