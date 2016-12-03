(function() {
    'use strict';

    angular
        .module('kundelApp')
        .controller('TransactionController', TransactionController);

    TransactionController.$inject = ['$http', '$scope', '$state', 'Transaction', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function TransactionController ($http, $scope, $state, Transaction, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll () {
            Transaction.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.transactions = data;
                vm.transactionsFiltered = null;
                vm.page = pagingParams.page;
                $http.get('api/account/').success(function(accountData) {
                    vm.account = accountData;
                });
                angular.forEach(vm.transactions, function(object){
                    object.book;
                    object.user;
                    $http.get('api/books/' + object.idBook).success(function(bookData) {
                        object.book = bookData;
                    });
                    $http.get('api/users/' + object.idUser).success(function(userData) {
                        object.user = userData;
                    });

                    //if(object.user.login == vm.account.login){
                        //vm.transactions.remove(object);
                    //}
                });
                //vm.transactions = vm.transactionsFiltered;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
